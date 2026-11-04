package es.upm.etsisi.qhcube.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RateLimiterTest {

    private static final Duration WINDOW = Duration.ofMinutes(15);
    private RateLimiter rateLimiter;

    @BeforeEach
    void setup() {
        this.rateLimiter = new RateLimiter();
    }

    @Test
    void allowsUpToMaxAttemptsAndThenBlocks() {
        for (int i = 0; i < 3; i++) {
            assertTrue(this.rateLimiter.tryAcquire("login:ip:1.2.3.4", 3, WINDOW));
        }

        assertFalse(this.rateLimiter.tryAcquire("login:ip:1.2.3.4", 3, WINDOW));
    }

    @Test
    void differentKeysHaveSeparateLimits() {
        this.rateLimiter.tryAcquire("login:ip:1.2.3.4", 1, WINDOW);
        assertFalse(this.rateLimiter.tryAcquire("login:ip:1.2.3.4", 1, WINDOW));

        assertTrue(this.rateLimiter.tryAcquire("login:ip:5.6.7.8", 1, WINDOW));
    }

    @Test
    void allowsAgainAfterWindowExpires() throws InterruptedException {
        Duration shortWindow = Duration.ofMillis(50);
        assertTrue(this.rateLimiter.tryAcquire("login:ip:1.2.3.4", 1, shortWindow));
        assertFalse(this.rateLimiter.tryAcquire("login:ip:1.2.3.4", 1, shortWindow));

        Thread.sleep(100);

        assertTrue(this.rateLimiter.tryAcquire("login:ip:1.2.3.4", 1, shortWindow));
    }
}
