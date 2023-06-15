package es.upm.etsisi.qhcube.controller.free;

import es.upm.etsisi.qhcube.controller.GameRESTController;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.service.game.GameCRUDService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${apiFreePrefix}/game")
public class GameFreeRESTController extends GameRESTController {

    public GameFreeRESTController(GameCRUDService gameCRUDService, GameMapper gameMapper) {
        super(gameCRUDService, gameMapper);
    }

    @PostMapping(path="/test-game")
    public ResponseEntity<String> testGame(@RequestBody String input) {
        log.info("testGame: {}", input);
        return ResponseEntity.ok("OK");
    }

}
