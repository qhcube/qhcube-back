package es.upm.etsisi.qhcube.controller.api;

import es.upm.etsisi.qhcube.controller.GameRESTController;
import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.service.game.GameCRUDService;
import es.upm.etsisi.qhcube.service.game.InputService;
import es.upm.etsisi.qhcube.service.game.MoveService;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${apiPrefix}/game")
public class GamePVTRESTController extends GameRESTController {

    private final InputService inputService;
    private final MoveService moveService;

    private final PlayerCRUDService playerCRUDService;

    public GamePVTRESTController(GameCRUDService gameCRUDService, GameMapper gameMapper, InputService inputService, MoveService moveService, PlayerCRUDService playerCRUDService) {
        super(gameCRUDService, gameMapper);
        this.inputService = inputService;
        this.moveService = moveService;
        this.playerCRUDService = playerCRUDService;
    }

    @GetMapping(path="/get-all")
    public ResponseEntity<List<GameDTO>> getAll(){
        return ResponseEntity.ok(this.gameMapper.toDTOs(this.gameCRUDService.findAll()));
    }

    @GetMapping(path="/get-by-id/{id}")
    public ResponseEntity<GameDTO> getById(@PathVariable("id") int id){
        return ResponseEntity.ok(this.gameMapper.toDTO(this.gameCRUDService.findById(id)));
    }

    @PostMapping(path="/test-game")
    public ResponseEntity<String> testGame(@RequestBody String input) {
        log.info("testGame PVT: {}", input);
        return ResponseEntity.ok("OK");
    }

    @PostMapping(path={"/init-registered-game", "/load-last-registered-game"})
    public ResponseEntity<GameDTO> initGame(@RequestBody PlayerDTO playerDTO) {
        return ResponseEntity.ok(this.inputService.initRegisteredGame(playerDTO));
    }

    @PostMapping(path="/add-move")
    public ResponseEntity<GameDTO> addMove(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwttoken, @RequestBody String move) {
        return ResponseEntity.ok(this.moveService.addMove(jwttoken, move));
    }

    @PostMapping(path="/get-games-by-player")
    public ResponseEntity<List<GameDTO>> getGamesByPlayer(@RequestBody PlayerDTO playerDTO) {
        return ResponseEntity.ok(this.gameMapper.toDTOsWithoutPlayers(
                this.gameCRUDService.findAllByPlayerId(
                        this.playerCRUDService.findByEmail(playerDTO.getEmail()).getId()
                )));
    }
}
