package es.upm.etsisi.qhcube.controller.free;

import es.upm.etsisi.qhcube.dto.RegistroResponseDTO;
import es.upm.etsisi.qhcube.service.security.JwtUserDetailsService;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import es.upm.etsisi.qhcube.controller.JugadorRESTController;
import es.upm.etsisi.qhcube.dto.JugadorRegisterDTO;
import es.upm.etsisi.qhcube.mapper.JugadorMapper;
import es.upm.etsisi.qhcube.service.jugador.JugadorService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${apiFreePrefix}/jugador")
public class JugadorFreeRESTController extends JugadorRESTController {

    public JugadorFreeRESTController(JugadorService jugadorService, JugadorMapper jugadorMapper, JwtTokenUtil jwtTokenUtil) {
        super(jugadorService, jugadorMapper, jwtTokenUtil);
    }

    @RequestMapping(value = "/registrar", method = RequestMethod.POST)
    public ResponseEntity<RegistroResponseDTO> registrarJugadorNuevo (@RequestBody JugadorRegisterDTO jugador) {

        UserDetails userDetails = new JwtUserDetailsService().loadUserByUsername(jugador.getEmail());
        if (this.passwordIsValid(jugador)) {
            RegistroResponseDTO resp = new RegistroResponseDTO(
                    jugador.getNombre(),
                    jugador.getEmail(),
                    jugador.getEmail(),
                    this.jwtTokenUtil.generateToken(userDetails));

            return ResponseEntity.ok(resp);
        } else {
            String msg = "Invalid password";
            return ResponseEntity.ok(new RegistroResponseDTO(msg, msg, msg, msg));
        }
    }

    private boolean passwordIsValid(JugadorRegisterDTO jugador) {
        return (jugador.getPassword().length() > 5 && jugador.getPassword().equals(jugador.getPasswordConfirm()));
    }
}
