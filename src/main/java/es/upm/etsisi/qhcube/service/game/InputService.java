package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.dto.PlayerDTO;

public interface InputService {
    GameDTO createNewGame(PlayerDTO playerDTO);

    GameDTO getGame(PlayerDTO playerDTO);

    GameDTO reinitGame(PlayerDTO playerDTO);

    GameDTO replayGameFromMovsList(GameDTO gaeDTO);
}
