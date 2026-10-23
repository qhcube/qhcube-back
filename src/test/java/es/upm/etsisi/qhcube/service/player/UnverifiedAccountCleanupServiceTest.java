package es.upm.etsisi.qhcube.service.player;

import es.upm.etsisi.qhcube.repository.PlayerRepository;
import es.upm.etsisi.qhcube.utils.EnvUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UnverifiedAccountCleanupServiceTest {
    private PlayerRepository repository;
    private EnvUtil envUtil;
    private UnverifiedAccountCleanupService service;

    @BeforeEach
    void setUp() {
        this.repository = mock(PlayerRepository.class);
        this.envUtil = mock(EnvUtil.class);
        this.service = new UnverifiedAccountCleanupService(repository, envUtil);
    }

    @Test
    void deletesUnverifiedAccountsOlderThanMaxDays() {
        when(this.envUtil.getUnverifiedAccountsMaxDays()).thenReturn(3L);
        ArgumentCaptor<Timestamp> captor = ArgumentCaptor.forClass(Timestamp.class);

        this.service.deleteUnverifiedAccounts();

        verify(this.repository).deleteByEnabledFalseAndRegisteredAtBefore(captor.capture());
        Instant expected = Instant.now().minus(3, ChronoUnit.DAYS);
        Instant limit = captor.getValue().toInstant();
        assertTrue(limit.isAfter(expected.minus(1, ChronoUnit.MINUTES)));
        assertTrue(limit.isBefore(expected.plus(1, ChronoUnit.MINUTES)));
    }
}
