package es.upm.etsisi.qhcube.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Getter
@Component
public class EnvUtil {

    private final Environment env;

    private final String port;
    private String host;
    public EnvUtil(Environment env) {
        this.env = env;
        this.port = env.getProperty("server.port");
        try {
            this.host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.debug("Error al obtener la dirección IP del servidor.");
            log.debug(e.getMessage());
            this.host = "UNKNOWN";
        }
    }

}
