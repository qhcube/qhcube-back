package es.upm.etsisi.qhcube.repository;

import es.upm.etsisi.qhcube.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

    Game findById(int id);
}