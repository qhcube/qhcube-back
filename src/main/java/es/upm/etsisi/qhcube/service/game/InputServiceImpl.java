package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.entity.Game;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.utils.EnvUtil;
import es.upm.etsisi.qhcube.utils.action.Action;
import es.upm.etsisi.qhcube.utils.action.ActionsOfTheGame;
import es.upm.etsisi.qhcube.utils.input.Input;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import es.upm.etsisi.qhcube.utils.input.InputManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
public class InputServiceImpl implements InputService {

    private final EnvUtil envUtil;
    private final PlayerCRUDService playerCRUDService;
    private final GameCRUDService gameCRUDService;
    private final GameMapper gameMapper;

    public InputServiceImpl(EnvUtil envUtil, PlayerCRUDService playerCRUDService, GameCRUDService gameCRUDService, GameMapper gameMapper) {
        this.envUtil = envUtil;
        this.playerCRUDService = playerCRUDService;
        this.gameCRUDService = gameCRUDService;
        this.gameMapper = gameMapper;
    }

    private PlayerDTO getCheckedPlayerDTO (PlayerDTO playerDTO) {
        List<GameDTO> gamesFromPlayer = playerDTO.getGames();
        PlayerDTO playerInDB = this.playerCRUDService.findByEmail(playerDTO.getEmail());

        if (playerInDB == null) {
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("El usuario no existe.");
            return playerDTO;
        } else {
            playerInDB.setRespStatus("OK").setGames(gamesFromPlayer);
            return playerInDB;
        }
    }

    public GameDTO createNewGame(PlayerDTO playerDTO) {
        PlayerDTO playerInDB = this.getCheckedPlayerDTO(playerDTO);

        if (playerInDB.getRespStatus().equals("ERROR")) {
            return GameDTO.builder().build().softClean().setPlayer(playerInDB);
        } else {

            if (this.gameCRUDService.countByPlayerIdOpenGames(playerInDB.getId()) > this.envUtil.getMaxGamesPerPlayer()) {
                playerInDB.softClean().setGames(null);
                playerInDB.setRespStatus("ERROR").setRespMessage("Límite de juegos sin resolver alcanzado.");
                return GameDTO.builder().build().softClean().setPlayer(playerInDB);
            } else {
                Input initialInput = new Input();
                initialInput.setInitialPracticeInput();

                for (int i = 0; i < 9; i++) {
                    InputManager.randomPermute(initialInput);
                    InputManager.randomExchange(initialInput);
                }

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
        }
    }

    public GameDTO replayGameFromMovsList(GameDTO gameDTO) {
        PlayerDTO playerDTO = gameDTO.getPlayer();
        GameDTO gameInDB = this.gameMapper.toDTO(this.gameCRUDService.findGameByIdAndPlayerEmail(gameDTO.getId(), playerDTO.getEmail()));
        if (gameInDB == null) {
            gameDTO.getPlayer().softClean().setRespStatus("ERROR").setRespMessage("No se encuentra el juego a continuar.");
        } else {
            String movsFromGameInDB = gameInDB.getMovsList();
            String movsFromGameDTO = gameDTO.getMovsList();
            if (movsFromGameDTO.startsWith("[[")) {
                movsFromGameDTO = movsFromGameDTO.substring(1, movsFromGameDTO.length() - 1);
            }

            if (movsFromGameInDB.startsWith(movsFromGameDTO)) {
                gameDTO.getPlayer().setRespStatus("OK").setRespMessage("Juego encontrado para continuar").setGames(null);
                gameInDB.setMovsList(movsFromGameDTO);
                this.gameCRUDService.save(this.gameMapper.toEntity(gameInDB));
            }
        }
        return gameDTO;
    }

    private GameDTO getGameFromFirstOnList(PlayerDTO playerDTO){
        GameDTO gameInDB = this.gameMapper.toDTO(this.gameCRUDService.findGameByIdAndPlayerEmail(playerDTO.getGames().get(0).getId(), playerDTO.getEmail()));
        if (gameInDB == null) {
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("No se encuentra el juego solicitado.");
            return GameDTO.builder().build().softClean().setPlayer(playerDTO);
        } else {
            playerDTO.setRespStatus("OK").setRespMessage("Juego encontrado").setGames(null);
            return gameInDB.setPlayer(playerDTO).setCurrentInput(
                    (this.getCurrentInput(gameInDB.getInput(), gameInDB.getMovsList())));
        }
    }

    public GameDTO getGame(PlayerDTO playerDTO) {
        PlayerDTO playerInDB = this.getCheckedPlayerDTO(playerDTO);

        if (playerInDB.getRespStatus().equals("ERROR")) {
            return GameDTO.builder().build().softClean().setPlayer(playerInDB);
        } else {
            playerInDB.softClean();
            return this.getGameFromFirstOnList(playerInDB);
        }
    }

    @Override
    public GameDTO reinitGame(PlayerDTO playerDTO) {
        PlayerDTO playerInDB = this.getCheckedPlayerDTO(playerDTO);

        if (playerInDB.getRespStatus().equals("ERROR")) {
            return GameDTO.builder().build().softClean().setPlayer(playerDTO);
        } else {
            GameDTO gameInDB = getGameFromFirstOnList(playerInDB);

            if (gameInDB.getPlayer().getRespStatus().equals("OK")) {
                gameInDB.setMovsList("[4,0.7483314773547882,0.6324555320336759,0.2]");
                gameInDB.setInitialTime(new java.sql.Timestamp(System.currentTimeMillis()));
                gameInDB.setEndTime(Timestamp.valueOf("2000-01-01 00:00:00"));

                this.gameCRUDService.save(this.gameMapper.toEntity(gameInDB));

                playerInDB.softClean().setRespMessage("Juego reiniciado");
                gameInDB.setPlayer(playerInDB).setCurrentInput(
                        (this.getCurrentInput(gameInDB.getInput(), gameInDB.getMovsList())));
            }
            return gameInDB;
        }
    }

    private String getCurrentInput(String input, String movsList) {
        Input currentInput = new Input(input);
        ActionsOfTheGame actions = new ActionsOfTheGame(movsList);
        for (Action action : actions.getActions()) {
            currentInput.executeAction(action);
        }
        return currentInput.toString();
    }
}
