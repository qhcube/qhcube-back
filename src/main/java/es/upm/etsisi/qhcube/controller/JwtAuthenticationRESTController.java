package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.service.security.LoginService;
import es.upm.etsisi.qhcube.service.security.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class JwtAuthenticationRESTController {

    private final LoginService loginService;
    private final RegistrationService registrationService;

    public JwtAuthenticationRESTController(LoginService loginService, RegistrationService registrationService) {
        this.loginService = loginService;
        this.registrationService = registrationService;
    }

    @PostMapping(value = "${apiFreePrefix}/login")
    public ResponseEntity<PlayerDTO> loginJugador (@RequestBody PlayerDTO player) {
        return ResponseEntity.ok(this.loginService.login(player));
    }

    @PostMapping(value = "${apiFreePrefix}/register")
    public ResponseEntity<PlayerDTO> registrarJugadorNuevo (@RequestBody PlayerDTO player) {
        return ResponseEntity.ok(this.registrationService.register(player));
    }

    @PostMapping(value = "${apiFreePrefix}/emailvalidation")
    public ResponseEntity<PlayerDTO> validarEmail (@RequestBody Map<String, String> params) {
        return ResponseEntity.ok(this.registrationService.validateEmail(params.get("code"), params.get("email")));
    }
}
