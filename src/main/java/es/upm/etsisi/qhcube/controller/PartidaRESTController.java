package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.mapper.PartidaMapper;
import es.upm.etsisi.qhcube.service.game.GameService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PartidaRESTController {

    protected final GameService partidaService;
    protected final PartidaMapper partidaMapper;

    public PartidaRESTController(GameService partidaService, PartidaMapper partidaMapper){
        this.partidaService = partidaService;
        this.partidaMapper = partidaMapper;
    }
}
