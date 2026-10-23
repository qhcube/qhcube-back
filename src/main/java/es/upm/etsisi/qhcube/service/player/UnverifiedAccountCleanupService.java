package es.upm.etsisi.qhcube.service.player;

import es.upm.etsisi.qhcube.repository.PlayerRepository;
import es.upm.etsisi.qhcube.utils.EnvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class UnverifiedAccountCleanupService {
    private final PlayerRepository playerRepository;
    private final EnvUtil envUtil;

    public UnverifiedAccountCleanupService(PlayerRepository playerRepository, EnvUtil envUtil) {
        this.playerRepository = playerRepository;
        this.envUtil = envUtil;
    }

    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void deleteUnverifiedAccounts() {
        Timestamp limit = Timestamp.from(
            Instant.now().minus(this.envUtil.getUnverifiedAccountsMaxDays(), ChronoUnit.DAYS));

        long deleted = this.playerRepository.deleteByEnabledFalseAndRegisteredAtBefore(limit);

        if (deleted > 0) {
            log.info("Eliminadas {} cuentas sin activar registradas antes de {}", deleted, limit);
        }
    }
}
