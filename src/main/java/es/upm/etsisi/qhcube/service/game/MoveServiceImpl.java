package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.entity.Game;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import es.upm.etsisi.qhcube.utils.EnvUtil;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import es.upm.etsisi.qhcube.utils.action.MoveValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    //TODO: Hacer esta función transaccional.
    @Override
    @Transactional
    public GameDTO addMove(String jwttoken, GameDTO gameDTO) {

        String email = jwtTokenUtil.getUsernameFromToken(jwttoken.substring(7));
        String moveToAdd = gameDTO.getMovsList();
        long gameID = gameDTO.getId();

        if(!MoveValidator.isValidMove(moveToAdd)){
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
        int numMoves = StringUtils.countOccurrencesOf(gameTUpdate.getMovsList(), "],[") + 1;
        if (numMoves >= envUtil.getMaxMovesPerGame()) {
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("La partida ha alcanzado el máximo de movimientos.");
            return GameDTO.builder().build().softClean().setPlayer(playerDTO);
        }

        gameTUpdate.setMovsList(gameTUpdate.getMovsList() + "," + moveToAdd);
        this.gameCRUDService.save(gameTUpdate);
        playerDTO.softClean().setRespStatus("OK").setRespMessage("Movimiento añadido correctamente.")
            .setGames(null);
        return this.gameMapper.toDTO(gameTUpdate).setPlayer(playerDTO).setMovsList(null);
    }
}
