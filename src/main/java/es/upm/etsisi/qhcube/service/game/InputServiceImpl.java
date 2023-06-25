package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.entity.Game;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.utils.action.Action;
import es.upm.etsisi.qhcube.utils.action.ActionsOfTheGame;
import es.upm.etsisi.qhcube.utils.input.Input;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import es.upm.etsisi.qhcube.utils.input.InputManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

        if (playerInDB == null) {
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("El usuario no existe.");
            return GameDTO.builder().build().softClean().setPlayer(playerDTO);
        } else {

            GameDTO game = playerInDB.getGames().stream()
                    .filter(aux -> new ActionsOfTheGame(aux.getMovsList()).isGameOpen())
                    .findFirst()
                    .orElse(null);

            if (game == null) {
                return this.createNewGame(playerInDB);
            } else {
                return this.gameOpenExist(playerInDB, game);
            }
        }
    }

    @Override
    public GameDTO initRegisteredGameForced(PlayerDTO playerDTO) {
        PlayerDTO playerInDB = this.playerCRUDService.findByEmail(playerDTO.getEmail());

        if (playerInDB == null || !Objects.equals(playerInDB.getJwttoken(), playerDTO.getJwttoken())) {
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("El usuario no existe.");
            return GameDTO.builder().build().softClean().setPlayer(playerDTO);
        } else {
            log.debug("Juego forzado.");
            //log.debug("ID GAME: " + playerDTO.getGames().get(0).getId());
            return this.createNewGame(playerInDB);
        }
    }

    private GameDTO createNewGame (PlayerDTO playerInDB) {
        Input initialInput = new Input();
        initialInput.setInitialPracticeInput();

        for (int i = 0; i < 9; i++) {
            InputManager.randomPermute(initialInput);
            InputManager.randomExchange(initialInput);
        }

        playerInDB.setRespStatus("OK 0");
        playerInDB.setRespMessage("Juego iniciado correctamente.");

        GameDTO newGameDTO = GameDTO.builder()
                .initialTime(new java.sql.Timestamp(System.currentTimeMillis()))
                .input(initialInput.toString())
                .movsList("[4,0.7483314773547882,0.6324555320336759,0.2]")
                .player(playerInDB)
                .build();


        Game gameToSave = this.gameMapper.toEntity(newGameDTO);
        this.gameCRUDService.save(gameToSave);

        newGameDTO.setId(gameToSave.getId()).getPlayer().softClean().setGames(null);

        return newGameDTO;
    }


    private GameDTO gameOpenExist(PlayerDTO playerInDB, GameDTO game) {
        playerInDB.softClean().setRespStatus("OK 1").setRespMessage("Juego abierto encontrado.").setGames(null);

        Input input = new Input(game.getInput());

        ActionsOfTheGame actions = new ActionsOfTheGame(game.getMovsList());
        for (Action action : actions.getActions()) {
            input.executeAction(action);
        }

        return game.setPlayer(playerInDB).setInput(input.toString());
    }
}
