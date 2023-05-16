package es.upm.etsisi.qhcube.service.player;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.repository.PlayerRepository;
import es.upm.etsisi.qhcube.service.security.JwtUserDetailsService;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private JavaMailSender emailSender;
    private final PlayerRepository playerRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PlayerMapper playerMapper;

    public PlayerServiceImpl(PlayerRepository playerRepository, JwtTokenUtil jwtTokenUtil, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.playerMapper = playerMapper;
    }

    @Override
    public JpaRepository<Player, Long> getRepository() {
        return this.playerRepository;
    }

    @Override
    public PlayerDTO register(PlayerDTO player) {

        PlayerDTO result;
        String checkPlayer = checkPlayerToRegister(player);

        if (checkPlayer.equals("OK")) {
            UserDetails userDetails = new JwtUserDetailsService(this.playerRepository).loadUserByUsername(player.getEmail());
            result = PlayerDTO.builder()
                    .nombre(player.getNombre())
                    .apellidos(player.getApellidos())
                    .email(player.getEmail())
                    .jwttoken(this.jwtTokenUtil.generateToken(userDetails))
                    .respStatus("OK")
                    .respMessage("Jugador registrado correctamente.")
                    .build();
            Player playerToSave = this.playerMapper.toEntity(result);
            playerToSave.setPassword(new BCryptPasswordEncoder().encode(player.getPassword()));
            this.playerRepository.save(playerToSave);
            try {
                this.sendVerificationEmail(playerToSave, "http://localhost:8080");
            } catch (MessagingException | UnsupportedEncodingException e) {
                log.debug("Error al enviar el correo de verificación.");
                log.debug(e.getMessage());
            }
        } else {
            result = PlayerDTO.builder()
                    .respStatus("ERROR")
                    .respMessage(checkPlayer)
                    .build();
        }

        return result;
    }

    private String checkPlayerToRegister(PlayerDTO player) {
        String result = "OK";

        if (player.getNombre().isEmpty()) result = "El nombre no puede estar vacío.";
        else if (player.getApellidos().isEmpty()) result = "Los apellidos no pueden estar vacíos.";
        else if (player.getEmail().isEmpty()) result = "El e-mail no puede estar vacío.";
        else if (player.getPassword().isEmpty()) result = "La contraseña no puede estar vacía.";
        else if (player.getPassword().length() < 6) result = "La contraseña debe tener al menos 6 caracteres.";
        else if (!EmailValidator.getInstance().isValid(player.getEmail())) result = "El e-mail no es válido.";
        else if (this.playerRepository.findByEmail(player.getEmail()).isPresent()) result = "Ya existe un jugador con ese e-mail.";

        return result;
    }

    @Override
    public PlayerDTO findByEmail(String email) {
        PlayerDTO result;
        Player player = this.playerRepository.findByEmail(email).orElse(null);
        if (player != null) {
            result = this.playerMapper.toDTO(player);
            result.setRespStatus("OK");
            result.setRespMessage("Jugador encontrado.");
        } else {
            result = PlayerDTO.builder()
                    .respStatus("ERROR")
                    .respMessage("No se ha encontrado ningún jugador con ese e-mail.")
                    .build();
        }
        return result;
    }

    private void sendVerificationEmail(Player user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {

        String toAddress = user.getEmail();
        String fromAddress = "juegohipercubocuantico@gmail.com";
        String senderName = "ETSISI - UPM";
        String subject = "Por favor, verifica tu registro";
        // TODO: Poner la direción de la APIREST en el enlace
        String content = "Estimado " + user.getNombre() + " " + user.getApellidos() + ",<br>"
                + "Haz click al enlace de abajo para verificar tu registro:<br>"
                + "<h3><a href='[[URL]]' target='_self'>VIRIFICAR</a></h3>"
                + "¡GRACIAS!<br />"
                + "QHCube Juego by Jesús García.";

        MimeMessage message = this.emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        this.emailSender.send(message);

    }
}
