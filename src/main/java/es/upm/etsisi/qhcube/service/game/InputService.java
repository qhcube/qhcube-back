package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.dto.GameDTO;
import es.upm.etsisi.qhcube.dto.PlayerDTO;

public interface InputService {
    GameDTO initRegisteredGame(PlayerDTO playerDTO);

    GameDTO initRegisteredGameForced(PlayerDTO playerDTO);
}
