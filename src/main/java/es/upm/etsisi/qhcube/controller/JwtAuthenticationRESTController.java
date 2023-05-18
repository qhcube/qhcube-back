package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.service.security.LoginService;
import es.upm.etsisi.qhcube.service.security.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class JwtAuthenticationRESTController {

    private final LoginService loginService;
    private final RegistrationService registrationService;

    public JwtAuthenticationRESTController(LoginService loginService, RegistrationService registrationService) {
        this.loginService = loginService;
        this.registrationService = registrationService;
    }

    @RequestMapping(value = "${apiFreePrefix}/login", method = RequestMethod.POST)
    public ResponseEntity<PlayerDTO> loginJugador (@RequestBody PlayerDTO player) {
        return ResponseEntity.ok(this.loginService.login(player));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<PlayerDTO> registrarJugadorNuevo (@RequestBody PlayerDTO player) {
        return ResponseEntity.ok(this.registrationService.register(player));
    }
}
