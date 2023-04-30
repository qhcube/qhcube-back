package es.upm.etsisi.qhcube.service.partida;

import es.upm.etsisi.qhcube.repository.PartidaRepository;
import es.upm.etsisi.qhcube.entity.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class PastidaServiceImpl implements PartidaService {

    private final PartidaRepository partidaRepository;

    public PastidaServiceImpl(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    @Override
    public Partida findById(int id) {
        return this.partidaRepository.findById(id);
    }

    @Override
    public JpaRepository<Partida, Long> getRepository() {
        return this.partidaRepository;
    }
}
