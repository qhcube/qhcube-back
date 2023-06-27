package es.upm.etsisi.qhcube.repository;

import es.upm.etsisi.qhcube.entity.Game;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    Game findById(long id);

    List<Game> findAllByPlayerId(@NonNull Long playerId);

    int countByPlayerId(@NonNull Long playerId);
}