package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.service.game.GameCRUDService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameRESTController {

    protected final GameCRUDService gameCRUDService;
    protected final GameMapper gameMapper;

    public GameRESTController(GameCRUDService gameCRUDService, GameMapper gameMapper) {
        this.gameCRUDService = gameCRUDService;
        this.gameMapper = gameMapper;
    }
}
