package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.dto.PlayerDTO;

public interface InputService {
    GameDTO createNewGame(PlayerDTO playerDTO);

    GameDTO getOpenGame(PlayerDTO playerDTO);
}
