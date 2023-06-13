package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.utils.input.Input;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.service.player.PlayerService;
import es.upm.etsisi.qhcube.utils.input.InputManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class InputServiceImpl implements InputService {

    private final PlayerService playerService;
    private final GameService gameService;
    private final GameMapper gameMapper;

    public InputServiceImpl(PlayerService playerService, GameService gameService, GameMapper gameMapper) {
        this.playerService = playerService;
        this.gameService = gameService;
        this.gameMapper = gameMapper;
    }

    @Override
    public GameDTO initRegisteredGame(PlayerDTO playerDTO) {
        PlayerDTO playerInDB = this.playerService.findByEmail(playerDTO.getEmail());
        GameDTO newGameDTO;

        if (playerInDB == null || !Objects.equals(playerInDB.getJwttoken(), playerDTO.getJwttoken())) {
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("El usuario no existe.");
            newGameDTO = GameDTO.builder().build().softClean().setPlayer(playerDTO);
        } else {
            log.debug("initRegisteredGame.playerInDB: {}", playerInDB);
            List<GameDTO> games = playerInDB.getGames();

            Input initialInput = new Input();
            initialInput.setInitialPracticeInput();

            for (int i = 0; i < 9; i++) {
                InputManager.randomPermute(initialInput);
                InputManager.randomExchange(initialInput);
            }

            newGameDTO = GameDTO.builder()
                    .initialTime(new java.sql.Timestamp(System.currentTimeMillis()))
                    .input(initialInput.toString())
                    .player(playerDTO)
                    .build();


                newGameDTO.setPlayer(playerInDB);
                newGameDTO.getPlayer().setRespStatus("OK");
                newGameDTO.getPlayer().setRespMessage("Juego iniciado correctamente.");
                this.gameService.save(this.gameMapper.toEntity(newGameDTO));
                newGameDTO.getPlayer().softClean();
            }

        return newGameDTO;
    }

    @Override
    public GameDTO addMove(String jwttoken, String move) {
        log.debug("addMove.move: {}", move);
        log.debug("addMove.token: {}", jwttoken.substring(7));
        PlayerDTO playerDTO = this.playerService.findByJwttoken(jwttoken.substring(7));

        GameDTO gameDTO = playerDTO.getGames().get(0);
        gameDTO.setMovsList(gameDTO.getMovsList() + move);
        gameDTO.setPlayer(PlayerDTO.builder().build().setId(82));
        log.debug("addMove.gameDTO: {}", gameDTO);

        this.gameService.save(this.gameMapper.toEntity(gameDTO));

        return null;
    }

}
