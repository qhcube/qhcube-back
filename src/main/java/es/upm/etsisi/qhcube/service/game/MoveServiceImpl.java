package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.entity.Game;
import es.upm.etsisi.qhcube.mapper.GameMapper;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import es.upm.etsisi.qhcube.utils.action.ActionsOfTheGame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MoveServiceImpl implements MoveService {

    private final PlayerCRUDService playerCRUDService;
    private final GameCRUDService gameCRUDService;
    private final GameMapper gameMapper;

    public MoveServiceImpl(PlayerCRUDService playerCRUDService, GameCRUDService gameCRUDService, GameMapper gameMapper) {
        this.playerCRUDService = playerCRUDService;
        this.gameCRUDService = gameCRUDService;
        this.gameMapper = gameMapper;
    }

    @Override
    public GameDTO addMove(String jwttoken, String move) {
        PlayerDTO playerDTO = this.playerCRUDService.findByJwttoken(jwttoken.substring(7));

        List<GameDTO> games = playerDTO.getGames();
        for (GameDTO game : games) {
            ActionsOfTheGame actionsOfTheGame = new ActionsOfTheGame(game.getMovsList());
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
