package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.service.security.LoginService;
import es.upm.etsisi.qhcube.service.security.RegistrationService;
import es.upm.etsisi.qhcube.utils.EnvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
public class JwtAuthenticationRESTController {

    private final LoginService loginService;
    private final RegistrationService registrationService;
    private final EnvUtil envUtil;

    public JwtAuthenticationRESTController(LoginService loginService, RegistrationService registrationService, EnvUtil envUtil) {
        this.loginService = loginService;
        this.registrationService = registrationService;
        this.envUtil = envUtil;
    }

    @PostMapping(value = "${apiFreePrefix}/login")
    public ResponseEntity<PlayerDTO> loginJugador (@RequestBody PlayerDTO player) {
        return ResponseEntity.ok(this.loginService.login(player));
    }

    @PostMapping(value = "${apiFreePrefix}/register")
    public ResponseEntity<PlayerDTO> registrarJugadorNuevo (@RequestBody PlayerDTO player) {
        return ResponseEntity.ok(this.registrationService.register(player));
    }

    @GetMapping(value = "${apiFreePrefix}/emailvalidation")
    public ResponseEntity<Void> validarEmail (
            @RequestParam("code") String validationCode,
            @RequestParam("email") String email) {

        PlayerDTO miPlayer = this.registrationService.validateEmail(validationCode, email);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(this.envUtil.getFrontendUrl()
                        + "/activated?status="
                        + URLEncoder.encode(miPlayer.getRespStatus(), StandardCharsets.UTF_8)
                        + "&message="
                        + URLEncoder.encode(miPlayer.getRespMessage(), StandardCharsets.UTF_8)))
                .build();
    }
}
