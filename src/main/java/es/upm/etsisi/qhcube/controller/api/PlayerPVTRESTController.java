package es.upm.etsisi.qhcube.controller.api;

import es.upm.etsisi.qhcube.controller.PlayerRESTController;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.service.player.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("${apiPrefix}/player")
public class PlayerPVTRESTController extends PlayerRESTController {

    public PlayerPVTRESTController(PlayerService jugadorService, PlayerMapper jugadorMapper) {
        super(jugadorService, jugadorMapper);
    }

    @PostMapping(path="/delete")
    public ResponseEntity<String> deletePlayer (@RequestBody PlayerDTO player) {
        String result = "OK";
        player = this.playerService.findByEmail(player.getEmail());
        if (player != null) {
            this.playerService.delete(player.getId());
        } else {
            result = "ERROR";
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(path="/get-all")
    public ResponseEntity<List<PlayerDTO>> getAll(){
        return ResponseEntity.ok(this.playerMapper.toDTOs(this.playerService.findAll()));
    }
}
