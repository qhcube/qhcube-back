package es.upm.etsisi.qhcube.service.game;

import es.upm.etsisi.qhcube.entity.Game;
import es.upm.etsisi.qhcube.repository.GameRepository;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameCRUDServiceImpl implements GameCRUDService {

    private final GameRepository gameRepository;

    public GameCRUDServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game findById(long id) {
        return this.gameRepository.findById(id);
    }

    @Override
    public List<Game> findAllByPlayerId(@NonNull Long playerId) {
        return this.gameRepository.findAllByPlayerId(playerId);
    }

    @Override
    public int countByPlayerId(@NonNull Long playerId) {
        return this.gameRepository.countByPlayerId(playerId);
    }

    @Override
    public JpaRepository<Game, Long> getRepository() {
        return this.gameRepository;
    }
}
