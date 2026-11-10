package es.upm.etsisi.qhcube.service.security;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import es.upm.etsisi.qhcube.utils.QHCubeUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private JwtUserDetailsService userDetailsService;
    private PlayerCRUDService playerCRUDService;
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        this.authenticationManager = mock(AuthenticationManager.class);
        this.jwtTokenUtil = mock(JwtTokenUtil.class);
        this.userDetailsService = mock(JwtUserDetailsService.class);
        this.playerCRUDService = mock(PlayerCRUDService.class);
        this.loginService = new LoginService(this.authenticationManager, this.jwtTokenUtil,
            this.userDetailsService, this.playerCRUDService);
    }

    @Test
    void loginReturnsTokenWithoutGames() {
        // Preparar: un jugador activado y el token que "generará" JwtTokenUtil
        Player player = new Player().setEmail("ana@upm.es").setNombre("Ana").setApellidos("López")
            .setPassword("hash").setEnabled(true);
        QHCubeUserDetails userDetails = new QHCubeUserDetails(player, new ArrayList<>());
        when(this.userDetailsService.loadUserByUsername("ana@upm.es")).thenReturn(userDetails);
        when(this.jwtTokenUtil.generateToken(userDetails)).thenReturn("token");

        // Ejecutar
        PlayerDTO result = this.loginService.login(new PlayerDTO().setEmail("ana@upm.es").setPassword("clave"));

        // Comprobar
        assertEquals("OK", result.getRespStatus());
        assertEquals("token", result.getJwttoken());
        assertEquals("Ana", result.getNombre());
        assertNull(result.getGames());
        assertNull(result.getPassword());
        verify(this.playerCRUDService).updateToken("ana@upm.es", "token");
        verify(this.playerCRUDService, never()).findByEmail(any());
    }

    @Test
    void disabledPlayerWithCorrectPasswordGetsNotActivatedMessage() {
        // Preparar: jugador sin activar cuya contraseña cifrada corresponde a "clave"
        Player player = new Player().setEmail("ana@upm.es")
            .setPassword(new BCryptPasswordEncoder().encode("clave"));
        when(this.authenticationManager.authenticate(any())).thenThrow(new DisabledException("disabled"));
        when(this.userDetailsService.loadUserByUsername("ana@upm.es"))
            .thenReturn(new QHCubeUserDetails(player, new ArrayList<>()));

        // Ejecutar
        PlayerDTO result = this.loginService.login(new PlayerDTO().setEmail("ana@upm.es").setPassword("clave"));

        // Comprobar
        assertEquals("ERROR", result.getRespStatus());
        assertEquals("Usuario no activado.", result.getRespMessage());
        verify(this.playerCRUDService, never()).findByEmail(any());
    }
}
