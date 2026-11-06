package es.upm.etsisi.qhcube.filter;

import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import es.upm.etsisi.qhcube.service.security.JwtUserDetailsService;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import es.upm.etsisi.qhcube.utils.QHCubeUserDetails;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtRequestFilterTest {

    private static final String SECRET = "clave-de-pruebas-de-al-menos-32-caracteres";

    private JwtTokenUtil jwtTokenUtil;
    private JwtUserDetailsService jwtUserDetailsService;
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setup() {
        this.jwtTokenUtil = new JwtTokenUtil(SECRET, 60000);
        this.jwtUserDetailsService = mock(JwtUserDetailsService.class);
        this.jwtRequestFilter = new JwtRequestFilter(this.jwtTokenUtil, this.jwtUserDetailsService, mock(PlayerCRUDService.class));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void givenStoredToken(String storedToken) {
        Player player = new Player();
        player.setEmail("ana@upm.es");
        player.setJwttoken(storedToken);
        when(this.jwtUserDetailsService.loadUserByUsername("ana@upm.es"))
            .thenReturn(new QHCubeUserDetails(player, new ArrayList<>()));
    }

    @Test
    void invalidTokenReturnsUnauthorized() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer este-es-un-token-invalido");

        MockHttpServletResponse resp = new MockHttpServletResponse();

        FilterChain chain = mock(FilterChain.class);

        this.jwtRequestFilter.doFilter(req, resp, chain);

        assertEquals(401, resp.getStatus());
        assertEquals("Invalid token", resp.getContentAsString());
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void tokenFromAnotherSessionReturnsSessionClosed() throws Exception {
        UserDetails user = User.withUsername("ana@upm.es").password("x").build();
        long now = System.currentTimeMillis();
        String oldSessionToken = this.jwtTokenUtil.generateToken(user, new Date(now - 60000));  // sesión del ordenador
        String newSessionToken = this.jwtTokenUtil.generateToken(user, new Date(now));          // login en el móvil
        this.givenStoredToken(newSessionToken);                     // en la BD está la del móvil

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer " + oldSessionToken);   // pide el ordenador
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        this.jwtRequestFilter.doFilter(req, resp, chain);

        assertEquals(401, resp.getStatus());
        assertEquals("Session closed", resp.getContentAsString());
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void tokenFromCurrentSessionContinues() throws Exception {
        UserDetails user = User.withUsername("ana@upm.es").password("x").build();
        long now = System.currentTimeMillis();
        String token = this.jwtTokenUtil.generateToken(user, new Date(now - 60000));
        this.givenStoredToken(token);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse resp = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        this.jwtRequestFilter.doFilter(req, resp, chain);

        assertEquals(200, resp.getStatus());
        verify(chain).doFilter(any(), any());
    }
}
