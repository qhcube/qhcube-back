package es.upm.etsisi.qhcube.controller.free;

import es.upm.etsisi.qhcube.dto.RegistroResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import es.upm.etsisi.qhcube.controller.JugadorRESTController;
import es.upm.etsisi.qhcube.dto.JugadorRegisterDTO;
import es.upm.etsisi.qhcube.mapper.JugadorMapper;
import es.upm.etsisi.qhcube.service.player.PlayerrService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${apiFreePrefix}/jugador")
public class JugadorFreeRESTController extends JugadorRESTController {

    public JugadorFreeRESTController(PlayerrService jugadorService, JugadorMapper jugadorMapper) {
        super(jugadorService, jugadorMapper);
    }

    @RequestMapping(value = "/registrar", method = RequestMethod.POST)
    public ResponseEntity<RegistroResponseDTO> registrarJugadorNuevo (@RequestBody JugadorRegisterDTO jugador) {
        RegistroResponseDTO response = jugadorService.register(jugador);
        log.debug("Response: " + response.toString());
        return ResponseEntity.ok(response);
    }
}
