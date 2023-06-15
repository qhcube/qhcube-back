package es.upm.etsisi.qhcube.service.player;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.service.CRUDAbstractService;

public interface PlayerCRUDService extends CRUDAbstractService<Player> {
    PlayerDTO findByEmail(String email);

    PlayerDTO findByJwttoken(String jwttoken);
}
