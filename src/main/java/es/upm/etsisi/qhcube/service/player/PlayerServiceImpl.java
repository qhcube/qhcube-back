package es.upm.etsisi.qhcube.service.player;

import es.upm.etsisi.qhcube.dto.NewRegisterDTO;
import es.upm.etsisi.qhcube.dto.NewRegisterResponseDTO;
import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.repository.PlayerRepository;
import es.upm.etsisi.qhcube.service.security.JwtUserDetailsService;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public PlayerServiceImpl(PlayerRepository playerRepository, JwtTokenUtil jwtTokenUtil) {
        this.playerRepository = playerRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public JpaRepository<Player, Long> getRepository() {
        return this.playerRepository;
    }

    @Override
    public NewRegisterResponseDTO register(NewRegisterDTO player) {

        NewRegisterResponseDTO result;
        String checkPlayer = checkValidJugadorToRegister(player);

        if (checkPlayer.equals("OK")) {
            UserDetails userDetails = new JwtUserDetailsService().loadUserByUsername(player.getEmail());
            result = new NewRegisterResponseDTO(
                    "OK",
                    player.getNombre(),
                    player.getApellidos(),
                    player.getEmail(),
                    this.jwtTokenUtil.generateToken(userDetails));
        } else {
            result = new NewRegisterResponseDTO("ERROR", checkPlayer, checkPlayer, checkPlayer, checkPlayer);
        }

        return result;
    }

    private String checkValidJugadorToRegister(NewRegisterDTO player) {
        String result = "OK";
        EmailValidator emailValidator = EmailValidator.getInstance();

        log.debug("Checking player: " + player.toString());

        if (player.getNombre().isEmpty()) result = "El nombre no puede estar vacío.";
        else if (player.getApellidos().isEmpty()) result = "Los apellidos no pueden estar vacíos.";
        else if (player.getEmail().isEmpty()) result = "El e-mail no puede estar vacío.";
        else if (player.getPassword().isEmpty()) result = "La contraseña no puede estar vacía.";
        else if (player.getPasswordConfirm().isEmpty())
            result = "La confirmación de la contraseña no puede estar vacía.";
        else if (!player.getPassword().equals(player.getPasswordConfirm())) result = "Las contraseñas no coinciden.";
        else if (player.getPassword().length() < 6) result = "La contraseña debe tener al menos 6 caracteres.";
        else if (!emailValidator.isValid(player.getEmail())) {
            log.debug("Email is not valid: " + player.getEmail());
            result = "El e-mail no es válido.";
        } else {
            log.debug("Email is valid: " + player.getEmail());
        }

        return result;
    }
}
