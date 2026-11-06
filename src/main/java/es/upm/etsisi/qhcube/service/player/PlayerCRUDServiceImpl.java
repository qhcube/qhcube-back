package es.upm.etsisi.qhcube.service.player;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PlayerCRUDServiceImpl implements PlayerCRUDService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public PlayerCRUDServiceImpl(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    @Override
    public JpaRepository<Player, Long> getRepository() {
        return this.playerRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public PlayerDTO findByEmail(String email) {
        Player player = this.playerRepository.findByEmail(email).orElse(null);
        return this.setRespToPlayerFound(player);
    }

    private PlayerDTO setRespToPlayerFound(Player player) {
        PlayerDTO result;
        if (player != null) {
            result = this.playerMapper.toDTO(player);
            result.setRespStatus("OK");
            result.setRespMessage("Jugador encontrado.");
        } else {
            result = PlayerDTO.builder()
                .respStatus("ERROR")
                .respMessage("No se ha encontrado ningún jugador con ese token.")
                .build();
        }
        return result;
    }

    @Transactional
    @Override
    public void logout(String email) {
        this.playerRepository.findByEmail(email)
            .ifPresent(player -> this.playerRepository.save(player.setJwttoken(null)));
    }
}
