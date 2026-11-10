package es.upm.etsisi.qhcube.utils;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenUtilTest {

    private static final String SECRET = "clave-de-pruebas-de-al-menos-32-caracteres";
    private final UserDetails user = User.withUsername("ana@upm.es").password("x").build();
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setup() {
        this.jwtTokenUtil = new JwtTokenUtil(SECRET, 60000);
    }

    @Test
    void generatedTokenContainsUsername() {
        String token = this.jwtTokenUtil.generateToken(this.user);
        assertEquals("ana@upm.es", this.jwtTokenUtil.getUsernameFromToken(token));
    }

    @Test
    void tokenSignedWithAnotherKeyIsRejected() {
        JwtTokenUtil jwtTokenUtil2 = new JwtTokenUtil("esta-es-otra-clave-invalida-de-mas-de-32-caracteres", 60000);
        String token2 = jwtTokenUtil2.generateToken(this.user);
        assertThrows(JwtException.class, () -> this.jwtTokenUtil.getUsernameFromToken(token2));
    }

    @Test
    void refreshedTokenKeepsIssuedAt() {
        long now = System.currentTimeMillis();
        Date oneHourAgo = new Date(now - 3600000);
        String token = this.jwtTokenUtil.generateToken(this.user, oneHourAgo);
        assertEquals(oneHourAgo.getTime() / 1000, this.jwtTokenUtil.getIssuedAtDateFromToken(token).getTime() / 1000);
    }

    @Test
    void refreshedTokenIsSameSession() {
        long now = System.currentTimeMillis();
        String token1 = this.jwtTokenUtil.generateToken(this.user, new Date(now - 50000));
        String token2 = this.jwtTokenUtil.generateToken(this.user, new Date(now - 50000));
        assertTrue(this.jwtTokenUtil.isSameSession(token1, token2));
    }

    @Test
    void newLoginIsDifferentSession() {
        long now = System.currentTimeMillis();
        String token1 = this.jwtTokenUtil.generateToken(this.user, new Date(now - 50000));
        String token2 = this.jwtTokenUtil.generateToken(this.user, new Date(now - 10000));
        assertFalse(this.jwtTokenUtil.isSameSession(token1, token2));
    }
}
