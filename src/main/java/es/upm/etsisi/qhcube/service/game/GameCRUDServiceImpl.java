package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.entity.Game;
import es.upm.etsisi.qhcube.repository.GameRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class GameCRUDServiceImpl implements GameCRUDService {

    private final GameRepository partidaRepository;

    public GameCRUDServiceImpl(GameRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    @Override
    public Game findById(int id) {
        return this.partidaRepository.findById(id);
    }

    @Override
    public JpaRepository<Game, Long> getRepository() {
        return this.partidaRepository;
    }
}
