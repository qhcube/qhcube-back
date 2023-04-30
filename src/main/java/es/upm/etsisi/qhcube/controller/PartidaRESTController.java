package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.mapper.PartidaMapper;
import es.upm.etsisi.qhcube.service.partida.PartidaService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PartidaRESTController {

    protected final PartidaService partidaService;
    protected final PartidaMapper partidaMapper;

    public PartidaRESTController(PartidaService partidaService, PartidaMapper partidaMapper){
        this.partidaService = partidaService;
        this.partidaMapper = partidaMapper;
    }
}
