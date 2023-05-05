package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.service.game.GameService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameRESTController {

    protected final GameService partidaService;
    protected final GameMapper partidaMapper;

    public GameRESTController(GameService partidaService, GameMapper partidaMapper){
        this.partidaService = partidaService;
        this.partidaMapper = partidaMapper;
    }
}
