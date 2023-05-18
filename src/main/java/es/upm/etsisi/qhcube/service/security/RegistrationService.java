package es.upm.etsisi.qhcube.service.security;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.repository.PlayerRepository;
import es.upm.etsisi.qhcube.utils.EmailUtil;
import es.upm.etsisi.qhcube.utils.RandomStringUtil;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class RegistrationService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final EmailUtil emailUtil;


    public RegistrationService(PlayerRepository playerRepository, PlayerMapper playerMapper, EmailUtil emailUtil) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.emailUtil = emailUtil;
    }


    public PlayerDTO register(PlayerDTO player) {
        String checkPlayer = checkPlayerToRegister(player);

        if (checkPlayer.equals("OK")) {
            player.setPassword(new BCryptPasswordEncoder().encode(player.getPassword()));
            player.setVericode(RandomStringUtil.getAlphaNumericString(64));
            this.playerRepository.save(this.playerMapper.toEntity(player));
            player.setRespStatus("OK");
            player.setRespMessage("Jugador registrado correctamente.");

            try {
                this.emailUtil.sendVerificationEmail(player);
            } catch (MessagingException | UnsupportedEncodingException e) {
                player.setRespStatus("ERROR");
                player.setRespMessage("No se ha podido enviar el e-mail de verificación.");
            }
        } else {
            player.setRespStatus("ERROR");
            player.setRespMessage(checkPlayer);
        }

        return player;
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
}
