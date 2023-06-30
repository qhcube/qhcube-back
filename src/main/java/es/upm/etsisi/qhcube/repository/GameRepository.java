package es.upm.etsisi.qhcube.repository;

import es.upm.etsisi.qhcube.entity.Game;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    Game findById(long id);

    List<Game> findByPlayerIdOrderByIdDesc(@NonNull Long playerId);

    int countByPlayerId(@NonNull Long playerId);

    @Query(value = "select count(player_id) from game where player_id = :playerId and NOT REGEXP_LIKE(movs_list, '\\\\[5,1,1,1]\\\\]$')", nativeQuery = true)
    int countByPlayerIdOpenGames(@NonNull Long playerId);
}