package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.entity.Game;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MoveServiceImpl implements MoveService {

    private final PlayerCRUDService playerCRUDService;
    private final GameCRUDService gameCRUDService;
    private final GameMapper gameMapper;
    private final JwtTokenUtil jwtTokenUtil;

    public MoveServiceImpl(PlayerCRUDService playerCRUDService, GameCRUDService gameCRUDService, GameMapper gameMapper, JwtTokenUtil jwtTokenUtil) {
        this.playerCRUDService = playerCRUDService;
        this.gameCRUDService = gameCRUDService;
        this.gameMapper = gameMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * @param jwttoken JWT token from HTTP request (header) to get the email of the player
     * @param move     String with the move to add to the game in the format: "[m, p1, p2, p3] gameID"
     * @return GameDTO with the game updated
     */
    @Override
    public GameDTO addMove(String jwttoken, String move) {

        String email = jwtTokenUtil.getUsernameFromToken(jwttoken.substring(7));
        String moveToAdd = move.split(" ")[0];
        long gameID = Long.parseLong(move.split(" ")[1]);

        PlayerDTO playerDTO = this.playerCRUDService.findByEmail(email);

        GameDTO gameIdFromPlayer = playerDTO.getGames().stream()
                .filter(aux -> aux.getId() == gameID)
                .findFirst()
                .orElse(null);

        if (gameIdFromPlayer == null) {
            // Game ID from request does not match with any game from the player. ERROR.
            playerDTO.softClean().setRespStatus("ERROR").setRespMessage("El juego no existe.");
            return GameDTO.builder().build().softClean().setPlayer(playerDTO);
        } else {
            Game gameTUpdate = this.gameCRUDService.findById(gameID);
            gameTUpdate.setMovsList(gameTUpdate.getMovsList() + "," + moveToAdd);
            this.gameCRUDService.save(gameTUpdate);
            playerDTO.softClean().setRespStatus("OK").setRespMessage("Movimiento añadido correctamente.")
                    .setGames(null);
            return this.gameMapper.toDTO(gameTUpdate).setPlayer(playerDTO).setMovsList(null);
        }
    }
}
