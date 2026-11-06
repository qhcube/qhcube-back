package es.upm.etsisi.qhcube.service.player;

import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class PlayerCRUDServiceImplTest {

    private PlayerRepository repository;
    private PlayerMapper mapper;
    private PlayerCRUDService playerCRUDService;

    @BeforeEach
    void setUp() {
        this.repository = mock(PlayerRepository.class);
        this.mapper = mock(PlayerMapper.class);
        this.playerCRUDService = new PlayerCRUDServiceImpl(this.repository, this.mapper);
    }

    @Test
    void logoutClearsStoredToken() {
        Player player = new Player();
        when(this.repository.findByEmail("ana@upm.es")).thenReturn(Optional.of(player));

        this.playerCRUDService.logout("ana@upm.es");

        assertNull(player.getJwttoken());
        verify(this.repository).save(player);
    }

    @Test
    void logoutOfUnknownPlayerDoesNothing() {
        when(this.repository.findByEmail("ana@upm.es")).thenReturn(Optional.empty());

        this.playerCRUDService.logout("ana@upm.es");

        verify(this.repository, never()).save(any());
    }
}
