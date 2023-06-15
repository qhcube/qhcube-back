package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.entity.Game;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.utils.action.GameMoves;
import es.upm.etsisi.qhcube.utils.input.Input;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import es.upm.etsisi.qhcube.utils.input.InputManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class InputServiceImpl implements InputService {

    private final PlayerCRUDService playerCRUDService;
    private final GameCRUDService gameCRUDService;
    private final GameMapper gameMapper;

    public InputServiceImpl(PlayerCRUDService playerCRUDService, GameCRUDService gameCRUDService, GameMapper gameMapper) {
        this.playerCRUDService = playerCRUDService;
        this.gameCRUDService = gameCRUDService;
        this.gameMapper = gameMapper;
    }

    @Override
    public GameDTO initRegisteredGame(PlayerDTO playerDTO) {
        PlayerDTO playerInDB = this.playerCRUDService.findByEmail(playerDTO.getEmail());
        GameDTO newGameDTO;

        if (playerInDB == null || !Objects.equals(playerInDB.getJwttoken(), playerDTO.getJwttoken())) {
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("El usuario no existe.");
            newGameDTO = GameDTO.builder().build().softClean().setPlayer(playerDTO);
        } else {
            List<GameDTO> games = playerInDB.getGames();
            // chack if exist started game
            if (games != null && games.size() > 0) {
                for (GameDTO game : games) {
                    GameMoves gameMoves = new GameMoves(game.getMovsList());
                    if (gameMoves.isGameOpen()) {
                        log.debug("Juego " + game.getId() + " abierto.");
                    } else {
                        log.debug("Juego " + game.getId() + " cerrado.");
                    }
                }
            }

            Input initialInput = new Input();
            initialInput.setInitialPracticeInput();

            for (int i = 0; i < 9; i++) {
                InputManager.randomPermute(initialInput);
                InputManager.randomExchange(initialInput);
            }

            newGameDTO = GameDTO.builder()
                    .initialTime(new java.sql.Timestamp(System.currentTimeMillis()))
                    .input(initialInput.toString())
                    .movsList("[4,-0.7182091216365892,0.6891022880589591,0.09650748255916076]")
                    .player(playerDTO)
                    .build();


                newGameDTO.setPlayer(playerInDB);
                newGameDTO.getPlayer().setRespStatus("OK");
                newGameDTO.getPlayer().setRespMessage("Juego iniciado correctamente.");
                this.gameCRUDService.save(this.gameMapper.toEntity(newGameDTO));
                newGameDTO.getPlayer().softClean();
            }

        return newGameDTO;
    }

    @Override
    public GameDTO addMove(String jwttoken, String move) {
        PlayerDTO playerDTO = this.playerCRUDService.findByJwttoken(jwttoken.substring(7));

        List<GameDTO> games = playerDTO.getGames();
        for (GameDTO game : games) {
            GameMoves gameMoves = new GameMoves(game.getMovsList());
        }


        GameDTO gameDTO = playerDTO.getGames().get(0);
        gameDTO.setMovsList(gameDTO.getMovsList() + "," + move);
        playerDTO.setGames(null);
        gameDTO.setPlayer(playerDTO);
        log.debug("addMove.gameDTO: {}", gameDTO.toJSON());

        Game gameEntity = this.gameMapper.toEntity(gameDTO);
        log.debug("addMove.gameEntity: {}", gameEntity);
        this.gameCRUDService.save(gameEntity);

        return gameDTO;
    }

}
