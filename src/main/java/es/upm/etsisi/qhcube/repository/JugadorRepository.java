package es.upm.etsisi.qhcube.repository;

import es.upm.etsisi.qhcube.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
}