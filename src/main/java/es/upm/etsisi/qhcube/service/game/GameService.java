package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.entity.Game;
import es.upm.etsisi.qhcube.service.CRUDAbstractService;

public interface GameService extends CRUDAbstractService<Game> {
    Game findById(int id);
}
