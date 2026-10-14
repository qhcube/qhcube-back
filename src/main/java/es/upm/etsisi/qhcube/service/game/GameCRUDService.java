package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.entity.Game;
import es.upm.etsisi.qhcube.service.CRUDAbstractService;
import lombok.NonNull;

import java.util.List;

public interface GameCRUDService extends CRUDAbstractService<Game> {
    Game findById(long id);

    Game findGameByIdAndPlayerEmail(long id, String email);

    List<Game> findByPlayerIdOrderByIdDesc(@NonNull Long playerId);

    int countByPlayerId(@NonNull Long playerId);

    int countByPlayerIdOpenGames(@NonNull Long playerId);
}
