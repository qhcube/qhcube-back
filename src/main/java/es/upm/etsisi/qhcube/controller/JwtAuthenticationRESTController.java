package es.upm.etsisi.qhcube.controller;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.service.player.PlayerService;
import es.upm.etsisi.qhcube.service.security.JwtUserDetailsService;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class JwtAuthenticationRESTController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;
    private final PlayerService playerService;

    public JwtAuthenticationRESTController(AuthenticationManager authenticationManager,
                                           JwtTokenUtil jwtTokenUtil,
                                           JwtUserDetailsService userDetailsService,
                                           PlayerService playerService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.playerService = playerService;
    }


    @RequestMapping(value = "${apiFreePrefix}/login", method = RequestMethod.POST)
    public ResponseEntity<PlayerDTO> loginJugador (@RequestBody PlayerDTO player) throws Exception {
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    player.getEmail(), player.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(player.getEmail());
        final String token = this.jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(this.playerService.findByEmail(player.getEmail())
                .setJwttoken(token)
                .setRespMessage("Login correcto."));
    }
}
