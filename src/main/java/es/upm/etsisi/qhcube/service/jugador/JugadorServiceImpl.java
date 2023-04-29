package es.upm.etsisi.qhcube.service.jugador;

import es.upm.etsisi.qhcube.entity.Jugador;
import es.upm.etsisi.qhcube.repository.JugadorRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class JugadorServiceImpl implements JugadorService {

    private final JugadorRepository jugadorRepository;

    public JugadorServiceImpl(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    @Override
    public JpaRepository<Jugador, Long> getRepository() {
        return this.jugadorRepository;
    }
}
