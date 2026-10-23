package es.upm.etsisi.qhcube.repository;

import es.upm.etsisi.qhcube.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByEmail(String email);

    Optional<Player> findByJwttoken(String jwttoken);

    long deleteByEnabledFalseAndRegisteredAtBefore(Timestamp limit);
}
