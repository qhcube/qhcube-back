package es.upm.etsisi.qhcube.controller.free;

import lombok.extern.slf4j.Slf4j;
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
}
