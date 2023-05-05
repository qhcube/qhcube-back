package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.service.game.GameService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameRESTController {

    protected final GameService gameService;
    protected final GameMapper gameMapper;

    public GameRESTController(GameService gameService, GameMapper gameMapper){
        this.gameService = gameService;
        this.gameMapper = gameMapper;
    }
}
