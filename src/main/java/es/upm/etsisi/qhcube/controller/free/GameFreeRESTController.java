package es.upm.etsisi.qhcube.controller.free;

import es.upm.etsisi.qhcube.controller.GameRESTController;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.service.game.GameService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${apiFreePrefix}/partida")
public class GameFreeRESTController extends GameRESTController {

    public GameFreeRESTController(GameService partidaService, GameMapper partidaMapper) {
        super(partidaService, partidaMapper);
    }
}
