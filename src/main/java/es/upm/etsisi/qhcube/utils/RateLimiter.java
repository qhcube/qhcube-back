package es.upm.etsisi.qhcube.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {

    private final Map<String, Window> windows = new ConcurrentHashMap<>();

    public boolean tryAcquire(String key, int maxAttempts, Duration duration) {
        Instant now = Instant.now();
        Window window = this.windows.compute(key, (k, current) -> {
            if (current == null || current.isExpired(now)) {
                return new Window(now.plus(duration));
            }
            current.count++;
            return current;
        });
        return window.count <= maxAttempts;
    }

    @Scheduled(fixedRate = 600000)
    public void removeExpiredWindows() {
        Instant now = Instant.now();
        this.windows.values().removeIf(window -> window.isExpired(now));
    }

    private static class Window {
        private int count = 1;
        private final Instant expiresAt;

        Window(Instant expiresAt) {
            this.expiresAt = expiresAt;
        }

        boolean isExpired(Instant now) {
            return !now.isBefore(this.expiresAt);
        }
    }
}
