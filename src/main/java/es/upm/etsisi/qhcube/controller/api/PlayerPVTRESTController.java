package es.upm.etsisi.qhcube.controller.api;

import es.upm.etsisi.qhcube.controller.PlayerRESTController;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${apiPrefix}/player")
public class PlayerPVTRESTController extends PlayerRESTController {

    public PlayerPVTRESTController(PlayerCRUDService jugadorService, PlayerMapper jugadorMapper) {
        super(jugadorService, jugadorMapper);
    }

    @PostMapping(path = "/delete")
    public ResponseEntity<String> deletePlayer(Authentication authentication) {
        String result = "OK";
        PlayerDTO player = this.playerCRUDService.findByEmail(authentication.getName());
        if (player != null) {
            this.playerCRUDService.delete(player.getId());
        } else {
            result = "ERROR";
        }
        return ResponseEntity.ok(result);
    }
}
