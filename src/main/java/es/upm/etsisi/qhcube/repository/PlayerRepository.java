package es.upm.etsisi.qhcube.repository;

import es.upm.etsisi.qhcube.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}