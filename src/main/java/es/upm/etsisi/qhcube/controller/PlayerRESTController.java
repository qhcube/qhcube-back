package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlayerRESTController {
    protected final PlayerCRUDService playerCRUDService;
    protected final PlayerMapper playerMapper;

    public PlayerRESTController(PlayerCRUDService playerCRUDService, PlayerMapper playerMapper) {
        this.playerCRUDService = playerCRUDService;
        this.playerMapper = playerMapper;
    }
}
