package es.upm.etsisi.qhcube.controller.free;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import es.upm.etsisi.qhcube.controller.PlayerRESTController;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.service.player.PlayerService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${apiFreePrefix}/player")
public class PlayerFreeRESTController extends PlayerRESTController {

    public PlayerFreeRESTController(PlayerService jugadorService, PlayerMapper jugadorMapper) {
        super(jugadorService, jugadorMapper);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<PlayerDTO> registrarJugadorNuevo (@RequestBody PlayerDTO jugador) {
        PlayerDTO response = playerService.register(jugador);
        log.debug("Response: " + response.toString());
        return ResponseEntity.ok(response);
    }
}
