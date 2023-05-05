package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.service.player.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerRESTController {
    protected final PlayerService playerService;
    protected final PlayerMapper playerMapper;
    public PlayerRESTController(PlayerService playerService, PlayerMapper playerMapper){
        this.playerService = playerService;
        this.playerMapper = playerMapper;
    }
}
