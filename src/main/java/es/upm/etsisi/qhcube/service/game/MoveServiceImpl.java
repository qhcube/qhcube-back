package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.entity.Game;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import es.upm.etsisi.qhcube.utils.EnvUtil;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import es.upm.etsisi.qhcube.utils.action.ActionsOfTheGame;
import es.upm.etsisi.qhcube.utils.action.MoveValidator;
import es.upm.etsisi.qhcube.utils.input.Input;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;

@Slf4j
@Service
public class MoveServiceImpl implements MoveService {

    private final PlayerCRUDService playerCRUDService;
    private final GameCRUDService gameCRUDService;
    private final GameMapper gameMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final EnvUtil envUtil;

    public MoveServiceImpl(PlayerCRUDService playerCRUDService, GameCRUDService gameCRUDService, GameMapper gameMapper, JwtTokenUtil jwtTokenUtil, EnvUtil envUtil) {
        this.playerCRUDService = playerCRUDService;
        this.gameCRUDService = gameCRUDService;
        this.gameMapper = gameMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.envUtil = envUtil;
    }

    @Override
    @Transactional
    public GameDTO addMove(String jwttoken, GameDTO gameDTO) {

        String email = jwtTokenUtil.getUsernameFromToken(jwttoken.substring(7));
        String moveToAdd = gameDTO.getMovsList();
        long gameID = gameDTO.getId();

        if (!MoveValidator.isValidMove(moveToAdd)) {
            return GameDTO.builder().build().softClean()
                .setPlayer(new PlayerDTO().setRespStatus("ERROR").setRespMessage("Movimiento no válido."));
        }

        PlayerDTO playerDTO = this.playerCRUDService.findByEmail(email);

        GameDTO gameIdFromPlayer = playerDTO.getGames().stream()
            .filter(aux -> aux.getId() == gameID)
            .findFirst()
            .orElse(null);

        if (gameIdFromPlayer == null) {
            // Game ID from request does not match with any game from the player. ERROR.
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("El juego no existe.");
            return GameDTO.builder().build().softClean().setPlayer(playerDTO);
        }

        Game gameTUpdate = this.gameCRUDService.findById(gameID);

        // Si la partida ya ha terminado (su último movimiento es el marcador de fin), no se admiten más.
        if (!new ActionsOfTheGame(gameTUpdate.getMovsList()).isGameOpen()) {
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("La partida ya ha terminado.");
            return GameDTO.builder().build().softClean().setPlayer(playerDTO);
        }

        int numMoves = StringUtils.countOccurrencesOf(gameTUpdate.getMovsList(), "],[") + 1;
        if (numMoves >= envUtil.getMaxMovesPerGame()) {
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("La partida ha alcanzado el máximo de movimientos.");
            return GameDTO.builder().build().softClean().setPlayer(playerDTO);
        }

        gameTUpdate.setMovsList(gameTUpdate.getMovsList() + "," + moveToAdd);

        // ¿Ha quedado resuelta con este movimiento?
        //     Se calcula el estado actual: el estado inicial + todos los movimientos (incluido el nuevo).
        Input estadoActual = new Input(gameTUpdate.getInput());
        estadoActual.executeActions(gameTUpdate.getMovsList());
        boolean resuelta = estadoActual.isSolved();

        // Si está resuelta, se cierra: marcador de fin y hora de fin.
        if (resuelta) {
            gameTUpdate.setMovsList(gameTUpdate.getMovsList() + ",[5,1,1,1]");
            gameTUpdate.setEndTime(new Timestamp(System.currentTimeMillis()));
        }

        this.gameCRUDService.save(gameTUpdate);

        // La respuesta avisa de si ha quedado resuelta.
        playerDTO.softClean().setRespStatus("OK")
            .setRespMessage(resuelta ? "¡Partida resuelta!" : "Movimiento añadido correctamente.")
            .setGames(null);
        return this.gameMapper.toDTO(gameTUpdate).setPlayer(playerDTO).setMovsList(null).setSolved(resuelta);
    }
}
