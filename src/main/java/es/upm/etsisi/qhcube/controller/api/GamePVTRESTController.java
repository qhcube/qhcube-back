package es.upm.etsisi.qhcube.controller.api;

import es.upm.etsisi.qhcube.controller.GameRESTController;
import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.service.game.GameCRUDService;
import es.upm.etsisi.qhcube.service.game.InputService;
import es.upm.etsisi.qhcube.service.game.MoveService;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("${apiPrefix}/game")
public class GamePVTRESTController extends GameRESTController {

    private final InputService inputService;
    private final MoveService moveService;

    private final JwtTokenUtil jwtTokenUtil;
    private final PlayerCRUDService playerCRUDService;

    public GamePVTRESTController(GameCRUDService gameCRUDService, GameMapper gameMapper, InputService inputService, MoveService moveService, JwtTokenUtil jwtTokenUtil, PlayerCRUDService playerCRUDService) {
        super(gameCRUDService, gameMapper);
        this.inputService = inputService;
        this.moveService = moveService;
        this.jwtTokenUtil = jwtTokenUtil;
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

    @PostMapping(path="/create-new")
    public ResponseEntity<GameDTO>create(@RequestBody PlayerDTO playerDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwttoken) {
        return Objects.requireNonNullElseGet(this.checkPlayer(playerDTO, jwttoken), () -> ResponseEntity.ok(this.inputService.createNewGame(playerDTO)));
    }

    @PostMapping(path="/get-game")
    public ResponseEntity<GameDTO> getOpen(@RequestBody PlayerDTO playerDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwttoken) {
        return Objects.requireNonNullElseGet(this.checkPlayer(playerDTO, jwttoken), () -> ResponseEntity.ok(this.inputService.getGame(playerDTO)));
    }

    @PostMapping(path="/reinit-game")
    public ResponseEntity<GameDTO> reinitGame(@RequestBody PlayerDTO playerDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwttoken) {
        return Objects.requireNonNullElseGet(this.checkPlayer(playerDTO, jwttoken), () -> ResponseEntity.ok(this.inputService.reinitGame(playerDTO)));
    }

    @PostMapping(path="/add-move")
    public ResponseEntity<GameDTO> addMove(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwttoken, @RequestBody String move) {
        return ResponseEntity.ok(this.moveService.addMove(jwttoken, move.replace("\"", "")));
    }

    @PostMapping(path="/replay-game-from-movs-list")
    public ResponseEntity<GameDTO> replayGameFromMovsList(@RequestBody GameDTO gameDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwttoken) {
        log.debug("replayGameFromMovsList: " + gameDTO.getMovsList());
        return new ResponseEntity<GameDTO>(HttpStatus.NOT_IMPLEMENTED);
        //return Objects.requireNonNullElseGet(this.checkGame(gameDTO, jwttoken), () -> ResponseEntity.ok(this.moveService.replayGameFromMovsList(gameDTO)));
    }

    @PostMapping(path="/get-games-by-player")
    public ResponseEntity<List<GameDTO>> getGamesByPlayer(@RequestBody PlayerDTO playerDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwttoken) {
        if (this.checkPlayer(playerDTO, jwttoken) == null) {
            return ResponseEntity.ok(this.gameMapper.toDTOsWithoutPlayers(
                    this.gameCRUDService.findByPlayerIdOrderByIdDesc(
                            this.playerCRUDService.findByEmail(playerDTO.getEmail()).getId()
                    )));
        } else {
            playerDTO.setRespStatus("ERROR").setRespMessage("Usuario no autorizado");
            ArrayList<GameDTO> gameDTOS = new ArrayList<>();
            gameDTOS.add(GameDTO.builder().build().setPlayer(playerDTO));
            return new ResponseEntity<>(gameDTOS, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path="/delete")
    ResponseEntity<GameDTO> delete(@RequestBody PlayerDTO playerDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwttoken) {
        GameDTO gameDTO = GameDTO.builder().build().setPlayer(playerDTO);
        if (this.checkPlayer(playerDTO, jwttoken) == null) {
            this.gameCRUDService.delete(playerDTO.getGames().get(0).getId());
            gameDTO.getPlayer().setRespStatus("OK").setRespMessage("Juego eliminado");
            return ResponseEntity.ok(gameDTO);
        } else {
            gameDTO.getPlayer().setRespStatus("ERROR").setRespMessage("Usuario no autorizado");
            return new ResponseEntity<>(gameDTO, HttpStatus.UNAUTHORIZED);
        }
    }

    private ResponseEntity<GameDTO> checkPlayer(PlayerDTO playerDTO, String jwttoken) {
        if (jwtTokenUtil.getUsernameFromToken(jwttoken.substring(7)).equals(playerDTO.getEmail())) {
            return null;
        } else {
            playerDTO.setRespStatus("ERROR").setRespMessage("Usuario no autorizado");
            GameDTO gameDTO = GameDTO.builder().build().setPlayer(playerDTO);
            return new ResponseEntity<>(gameDTO, HttpStatus.UNAUTHORIZED);
        }
    }
}
