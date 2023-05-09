package es.upm.etsisi.qhcube.service.player;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.service.AbstractService;

public interface PlayerService extends AbstractService<Player> {
    PlayerDTO register(PlayerDTO jugador);
}
