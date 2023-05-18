/*
Gadis (c) 2022 Escuela Técnica Superior de Ingeniería de Sistemas Informáticos (UPM)

This file is part of Gadis.

Gadis is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
Gadis is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with Gadis.  If not, see <http://www.gnu.org/licenses/>.
*/

package es.upm.etsisi.qhcube.service.security;

import es.upm.etsisi.qhcube.dto.PlayerDTO;
import es.upm.etsisi.qhcube.service.player.PlayerService;
import es.upm.etsisi.qhcube.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;
    private final PlayerService playerService;

    public LoginService(AuthenticationManager authenticationManager,
                        JwtTokenUtil jwtTokenUtil,
                        JwtUserDetailsService userDetailsService,
                        PlayerService playerService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.playerService = playerService;
    }

    public PlayerDTO login(PlayerDTO player) {
        PlayerDTO result = new PlayerDTO().setEmail(player.getEmail());
        String authStatus = "ERROR";
        String authMessage = "Login correcto.";
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    player.getEmail(), player.getPassword()));
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(player.getEmail());
            final String token = this.jwtTokenUtil.generateToken(userDetails);
            this.playerService.findByEmail(player.getEmail())
                    .setJwttoken(token);
            authStatus = "OK";
        } catch (DisabledException e) {
            authMessage = "Usuario no activado.";
        } catch (BadCredentialsException e) {
            authMessage = "Credenciales incorrectas.";
        }

        return result.setRespStatus(authStatus).setRespMessage(authMessage);
    }
}
