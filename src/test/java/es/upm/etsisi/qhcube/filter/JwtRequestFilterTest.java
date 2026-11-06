package es.upm.etsisi.qhcube.filter;

import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import es.upm.etsisi.qhcube.service.security.JwtUserDetailsService;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtRequestFilterTest {

    private static final String SECRET = "clave-de-pruebas-de-al-menos-32-caracteres";

    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setup() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(SECRET, 60000);
        this.jwtRequestFilter = new JwtRequestFilter(jwtTokenUtil, mock(JwtUserDetailsService.class), mock(PlayerCRUDService.class));
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

}
