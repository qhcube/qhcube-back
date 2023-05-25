package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.entity.Game;
import es.upm.etsisi.qhcube.service.AbstractService;

public interface GameService extends AbstractService<Game> {
    Game findById(int id);
}
