package es.upm.etsisi.qhcube.service.player;

import es.upm.etsisi.qhcube.dto.JugadorRegisterDTO;
import es.upm.etsisi.qhcube.dto.RegistroResponseDTO;
import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.service.AbstractService;

public interface PlayerrService extends AbstractService<Player> {
    public RegistroResponseDTO register(JugadorRegisterDTO jugador);
}
