package es.upm.etsisi.qhcube.service.player;

import es.upm.etsisi.qhcube.dto.NewRegisterDTO;
import es.upm.etsisi.qhcube.dto.NewRegisterResponseDTO;
import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.service.AbstractService;

public interface PlayerService extends AbstractService<Player> {
    NewRegisterResponseDTO register(NewRegisterDTO jugador);
}
