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

    public GameDTO createNewGame(PlayerDTO playerDTO) {
        PlayerDTO playerInDB = this.playerCRUDService.findByEmail(playerDTO.getEmail());

        if (playerInDB == null) {
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("El usuario no existe.");
            return GameDTO.builder().build().softClean().setPlayer(playerDTO);
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

                playerInDB.setRespStatus("OK");
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

    public GameDTO getGame(PlayerDTO playerDTO) {
        PlayerDTO playerInDB = this.playerCRUDService.findByEmail(playerDTO.getEmail());
        String respMessage = "";
        GameDTO gameInDB;

        if (playerInDB == null) {
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("El usuario no existe.");
            return GameDTO.builder().build().softClean().setPlayer(playerDTO);
        } else {
            gameInDB = this.gameMapper.toDTO(this.gameCRUDService.findById(playerDTO.getGames().get(0).getId()));

            if (gameInDB == null) {
                respMessage = "No se encuentra el juego a abrir.";
            }
        }

        if (!respMessage.equals("")) {
            playerInDB.softClean().setRespStatus("ERROR").setRespMessage(respMessage);
            return GameDTO.builder().build().softClean().setPlayer(playerInDB);
        } else {
            playerInDB.softClean().setRespStatus("OK").setRespMessage("Juego abierto encontrado.").setGames(null);

            return gameInDB.setPlayer(playerInDB).setCurrentInput(
                    (this.getCurrentInput(gameInDB.getInput(), gameInDB.getMovsList())));

        }
    }

    @Override
    public GameDTO reinitGame(PlayerDTO playerDTO) {
        PlayerDTO playerInDB = this.playerCRUDService.findByEmail(playerDTO.getEmail());
        String respMessage = "";
        GameDTO gameInDB;

        if (playerInDB == null) {
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("El usuario no existe.");
            return GameDTO.builder().build().softClean().setPlayer(playerDTO);
        } else {
            gameInDB = this.gameMapper.toDTO(this.gameCRUDService.findById(playerDTO.getGames().get(0).getId()));
            if (gameInDB == null) {
                respMessage = "No se encuentra el juego a abrir.";
            }
        }

        if (!respMessage.equals("")) {
            playerInDB.softClean().setRespStatus("ERROR").setRespMessage(respMessage);
            return GameDTO.builder().build().softClean().setPlayer(playerInDB);
        } else {
            gameInDB.setMovsList("[4,0.7483314773547882,0.6324555320336759,0.2]");
            gameInDB.setInitialTime(new java.sql.Timestamp(System.currentTimeMillis()));
            gameInDB.setEndTime(Timestamp.valueOf("2000-01-01 00:00:00"));
            playerInDB.softClean().setRespStatus("OK").setRespMessage("Juego reiniciado.").setGames(null);

            this.gameCRUDService.save(this.gameMapper.toEntity(gameInDB));

            return gameInDB.setPlayer(playerInDB).setCurrentInput(
                    (this.getCurrentInput(gameInDB.getInput(), gameInDB.getMovsList())));

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
