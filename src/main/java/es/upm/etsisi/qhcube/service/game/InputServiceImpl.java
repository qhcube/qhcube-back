package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.utils.action.ActionsOfTheGame;
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
        log.debug("initRegisteredGame: {}", playerDTO);

        PlayerDTO playerInDB = this.playerCRUDService.findByEmail(playerDTO.getEmail());

        if (playerInDB == null || !Objects.equals(playerInDB.getJwttoken(), playerDTO.getJwttoken())) {
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("El usuario no existe.");
            return GameDTO.builder().build().softClean().setPlayer(playerDTO);
        } else {

            List<GameDTO> games = playerInDB.getGames().stream()
                    .filter(game -> new ActionsOfTheGame(game.getMovsList()).isGameOpen()).toList();

            if (games.size() == 0) {
                log.debug("No hay juegos.");
                return this.createNewGame(playerInDB);
            } else {
                log.debug("Juego abierto.");
                return this.gameOpenExist(playerInDB, games.get(0));
            }
        }
    }

    private GameDTO createNewGame (PlayerDTO playerInDB) {
        Input initialInput = new Input();
        initialInput.setInitialPracticeInput();

        for (int i = 0; i < 9; i++) {
            InputManager.randomPermute(initialInput);
            InputManager.randomExchange(initialInput);
        }

        playerInDB.setRespStatus("OK");
        playerInDB.setRespMessage("Juego iniciado correctamente.");

        GameDTO newGameDTO = GameDTO.builder()
                .initialTime(new java.sql.Timestamp(System.currentTimeMillis()))
                .input(initialInput.toString())
                .movsList("[0.7483314773547882, 0.6324555320336759, 0.2]")
                .player(playerInDB)
                .build();


        this.gameCRUDService.save(this.gameMapper.toEntity(newGameDTO));
        newGameDTO.getPlayer().softClean();

        return newGameDTO;
    }


    private GameDTO gameOpenExist(PlayerDTO playerInDB, GameDTO game) {
        log.debug("Juego abierto encontrado --> " + game.toJavaScript());
        playerInDB.softClean().setRespStatus("OK").setRespMessage("Juego abierto encontrado.").setGames(null);

        return game.setPlayer(playerInDB);
    }
}
