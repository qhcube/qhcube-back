package es.upm.etsisi.qhcube.controller.free;

import es.upm.etsisi.qhcube.dto.RegistroResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import es.upm.etsisi.qhcube.controller.JugadorRESTController;
import es.upm.etsisi.qhcube.dto.JugadorRegisterDTO;
import es.upm.etsisi.qhcube.mapper.JugadorMapper;
import es.upm.etsisi.qhcube.service.jugador.JugadorService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${apiFreePrefix}/jugador")
public class JugadorFreeRESTController extends JugadorRESTController {

    public JugadorFreeRESTController(JugadorService jugadorService, JugadorMapper jugadorMapper) {
        super(jugadorService, jugadorMapper);
    }

    @RequestMapping(value = "/registrar", method = RequestMethod.POST)
    public ResponseEntity<RegistroResponseDTO> registrarJugadorNuevo (@RequestBody JugadorRegisterDTO jugador) {

        if (this.passwordIsValid(jugador)) {
            RegistroResponseDTO resp = new RegistroResponseDTO(
                    jugador.getNombre(),
                    jugador.getEmail(),
                    jugador.getEmail(),
                    "Este es tu token que ya se generará");

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
