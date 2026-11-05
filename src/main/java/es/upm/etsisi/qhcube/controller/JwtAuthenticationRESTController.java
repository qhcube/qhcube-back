package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.dto.RegisterRequestDTO;
import es.upm.etsisi.qhcube.service.security.LoginService;
import es.upm.etsisi.qhcube.service.security.RegistrationService;
import es.upm.etsisi.qhcube.utils.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RestController
public class JwtAuthenticationRESTController {

    private static final Duration FIFTEEN_MINUTES = Duration.ofMinutes(15);
    private static final Duration ONE_HOUR = Duration.ofHours(1);

    private final LoginService loginService;
    private final RegistrationService registrationService;
    private final RateLimiter rateLimiter;

    public JwtAuthenticationRESTController(LoginService loginService, RegistrationService registrationService, RateLimiter rateLimiter) {
        this.loginService = loginService;
        this.registrationService = registrationService;
        this.rateLimiter = rateLimiter;
    }

    @PostMapping(value = "${apiFreePrefix}/login")
    public ResponseEntity<PlayerDTO> loginJugador(@RequestBody PlayerDTO player, HttpServletRequest request) {
        if (!this.rateLimiter.tryAcquire("login:ip:" + request.getRemoteAddr(), 30, FIFTEEN_MINUTES)
            || !this.rateLimiter.tryAcquire("login:email:" + player.getEmail(), 5, FIFTEEN_MINUTES)) {
            return this.tooManyRequests();
        }
        return ResponseEntity.ok(this.loginService.login(player));
    }

    @PostMapping(value = "${apiFreePrefix}/resend-email")
    public ResponseEntity<PlayerDTO> reenviarEmail(@RequestBody PlayerDTO player, HttpServletRequest request) {
        if (!this.rateLimiter.tryAcquire("resend:ip:" + request.getRemoteAddr(), 10, ONE_HOUR)
            || !this.rateLimiter.tryAcquire("resend:email:" + player.getEmail(), 3, ONE_HOUR)) {
            return this.tooManyRequests();
        }
        return ResponseEntity.ok(this.registrationService.resendVerificationEmail(player));
    }

    @PostMapping(value = "${apiFreePrefix}/register")
    public ResponseEntity<PlayerDTO> registrarJugadorNuevo(@RequestBody RegisterRequestDTO registerRequest, HttpServletRequest request) {
        if (!this.rateLimiter.tryAcquire("register:ip:" + request.getRemoteAddr(), 10, ONE_HOUR)) {
            return this.tooManyRequests();
        }
        return ResponseEntity.ok(this.registrationService.register(registerRequest));
    }

    @PostMapping(value = "${apiFreePrefix}/emailvalidation")
    public ResponseEntity<PlayerDTO> validarEmail(@RequestBody Map<String, String> params, HttpServletRequest request) {
        if (!this.rateLimiter.tryAcquire("validation:ip:" + request.getRemoteAddr(), 10, FIFTEEN_MINUTES)) {
            return this.tooManyRequests();
        }
        return ResponseEntity.ok(this.registrationService.validateEmail(params.get("code"), params.get("email")));
    }

    private ResponseEntity<PlayerDTO> tooManyRequests() {
        PlayerDTO response = new PlayerDTO()
            .setRespStatus("ERROR")
            .setRespMessage("Demasiados intentos. Espera unos minutos y vuelve a intentarlo.");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
    }
}
