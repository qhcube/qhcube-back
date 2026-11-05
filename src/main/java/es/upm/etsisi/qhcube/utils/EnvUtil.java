package es.upm.etsisi.qhcube.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Getter
@Component
public class EnvUtil {

    private final Environment env;

    private final String serverPort;
    private final String serverHost;
    private final String serverUrl;
    private final String frontendUrl;
    private final String apiPrefix;
    private final String apiFreePrefix;
    private final String mailHost;
    private final int mailPort;
    private final String mailUsername;
    private final String mailPassword;
    private final String mailEncoding;
    private final String mailSenderName;
    private final String mailFromAddress;
    private final String mailTrasnportProtocol;
    private final boolean mailSmtpAuth;
    private final boolean mailSmtpStarttlsEnable;
    private final int maxGamesPerPlayer;
    private final int maxMovesPerGame;
    private final boolean almostSolvedGame;
    private final long unverifiedAccountsMaxDays;

    public EnvUtil(Environment env) {
        this.env = env;

        this.serverPort = env.getProperty("server.port");
        this.serverHost = env.getProperty("server.host");
        this.serverUrl = env.getProperty("server.url");
        this.frontendUrl = env.getProperty("frontend.url");

        this.apiPrefix = env.getProperty("apiPrefix");
        this.apiFreePrefix = env.getProperty("apiFreePrefix");

        this.mailHost = env.getProperty("spring.mail.host");
        this.mailPort = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.mail.port")));
        this.mailUsername = env.getProperty("spring.mail.username");
        this.mailPassword = env.getProperty("spring.mail.password");
        this.mailEncoding = env.getProperty("spring.mail.default-encoding");
        this.mailSenderName = env.getProperty("qhcube.mail.sender.name");
        this.mailFromAddress = env.getProperty("qhcube.mail.from.address");
        this.mailTrasnportProtocol = env.getProperty("spring.mail.transport.protocol");
        this.mailSmtpAuth = Boolean.parseBoolean(env.getProperty("spring.mail.properties.mail.smtp.auth"));
        this.mailSmtpStarttlsEnable = Boolean.parseBoolean(env.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));

        this.maxGamesPerPlayer = Integer.parseInt(Objects.requireNonNull(env.getProperty("max.games.per.player")));
        this.maxMovesPerGame = Integer.parseInt(Objects.requireNonNull(env.getProperty("max.moves.per.game")));
        this.almostSolvedGame = Boolean.parseBoolean(env.getProperty("qhcube.dev.almost-solved-game", "false"));
        this.unverifiedAccountsMaxDays = Long.parseLong(Objects.requireNonNull(env.getProperty("qhcube.unverified-accounts.max-days")));
    }
}
