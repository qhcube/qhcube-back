package es.upm.etsisi.qhcube.filter;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.mapper.PlayerMapper;
import es.upm.etsisi.qhcube.service.player.PlayerCRUDService;
import es.upm.etsisi.qhcube.service.security.JwtUserDetailsService;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import es.upm.etsisi.qhcube.utils.QHCubeUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final PlayerCRUDService playerCRUDService;

    @Value("${jwt.refreshLimit}")
    private long refreshLimit;

    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil, JwtUserDetailsService jwtUserDetailsService, PlayerCRUDService playerCRUDService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.playerCRUDService = playerCRUDService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
        throws ServletException, IOException {

        final String authorization = request.getHeader("Authorization");

        try {
            if (authorization != null && authorization.startsWith("Bearer ")) {

                String jwtToken = authorization.substring(7);
                String username = this.jwtTokenUtil.getUsernameFromToken(jwtToken);

                Date expiration = this.jwtTokenUtil.getExpirationDateFromToken(jwtToken);
                long diffTime = expiration.getTime() - new Date().getTime();

                if (diffTime < this.refreshLimit) {
                    this.refreshToken(response, username);
                }

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
                    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }

        } catch (ExpiredJwtException e) {
            log.debug("Recieved EXPIRED JWT token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void refreshToken(HttpServletResponse response, String username) {
        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
        String newToken = this.jwtTokenUtil.generateToken(userDetails);
        response.setHeader("authorization", "Bearer " + newToken);
        this.playerCRUDService.save(((QHCubeUserDetails) userDetails).getPlayer().setJwttoken(newToken));
    }
}
