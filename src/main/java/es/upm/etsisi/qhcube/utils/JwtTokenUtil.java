package es.upm.etsisi.qhcube.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtTokenUtil implements Serializable {

    @Serial
    private static final long serialVersionUID = 8459809523059085563L;

    private final SecretKey key;
    private final long expiration;

    public JwtTokenUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean isSameSession(String token, String otherToken) {
        return this.getIssuedAtDateFromToken(token).equals(this.getIssuedAtDateFromToken(otherToken));
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(this.key).build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        // login: la sesión empieza ahora
        return generateToken(userDetails, new Date());
    }

    public String generateToken(UserDetails userDetails, Date issuedAt) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername(), issuedAt);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, Date issuedAt) {
        return Jwts
            .builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(issuedAt)
            .expiration(new Date(System.currentTimeMillis() + this.expiration))
            .signWith(this.key)
            .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = null;
        if (token != null)
            username = getUsernameFromToken(token);
        return (token != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
