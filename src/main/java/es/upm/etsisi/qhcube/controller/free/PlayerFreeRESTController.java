package es.upm.etsisi.qhcube.controller.free;

import es.upm.etsisi.qhcube.dto.NewRegisterResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import es.upm.etsisi.qhcube.controller.PlayerRESTController;
import es.upm.etsisi.qhcube.dto.NewRegisterDTO;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.service.player.PlayerrService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${apiFreePrefix}/jugador")
public class PlayerFreeRESTController extends PlayerRESTController {

    public PlayerFreeRESTController(PlayerrService jugadorService, PlayerMapper jugadorMapper) {
        super(jugadorService, jugadorMapper);
    }

    @RequestMapping(value = "/registrar", method = RequestMethod.POST)
    public ResponseEntity<NewRegisterResponseDTO> registrarJugadorNuevo (@RequestBody NewRegisterDTO jugador) {
        NewRegisterResponseDTO response = jugadorService.register(jugador);
        log.debug("Response: " + response.toString());
        return ResponseEntity.ok(response);
    }
}
