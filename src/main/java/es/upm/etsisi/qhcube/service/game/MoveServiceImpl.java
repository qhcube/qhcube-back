package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.entity.Game;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //TODO: Hacer esta función transaccional.
    @Override
    @Transactional
    public GameDTO addMove(String jwttoken, GameDTO gameDTO) {

        String email = jwtTokenUtil.getUsernameFromToken(jwttoken.substring(7));
        String moveToAdd = gameDTO.getMovsList();
        long gameID = gameDTO.getId();

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
