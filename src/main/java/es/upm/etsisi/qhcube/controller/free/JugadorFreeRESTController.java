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

    // TODO: Sustituir por ResponseEntity y quitar el return
    /*
    Datos que llegan:
    user: {
                email: "",
                nombre: "",
                apellidos: "",
                password: ""
            }

            @RequestMapping(value = "${apiFreePrefix}/authenticate", method = RequestMethod.POST)
    public ResponseEntity<JwtResponseDTO> createAuthenticationToken(@RequestBody JwtRequestDTO request) throws Exception {
    */
    @RequestMapping(value = "/registrar", method = RequestMethod.POST)
    public ResponseEntity<RegistroResponseDTO> registrarJugadorNuevo (@RequestBody JugadorRegisterDTO jugador) {

        // TODO: Confirmar que el password es igual que el confirm y que es válido.
        log.debug("Registrando jugador nuevo");
        log.debug(jugador.toString());

        RegistroResponseDTO resp = new RegistroResponseDTO(
                jugador.getNombre(),
                jugador.getEmail(),
                jugador.getEmail(),
                "Este es tu token que ya se generará");

        log.debug("Respuesta... " + resp);
        return ResponseEntity.ok(resp);
    }

}
