package es.upm.etsisi.qhcube.service.security;

import es.upm.etsisi.qhcube.entity.Player;
import es.upm.etsisi.qhcube.repository.PlayerRepository;
import es.upm.etsisi.qhcube.utils.QHCubeUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final PlayerRepository playerRepository;

    public JwtUserDetailsService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Player> user = this.playerRepository.findByEmail(email);

        if (user.isPresent()) {
            return new QHCubeUserDetails(user.get(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }

        // return new QHCubeUserDetails(username, "", new ArrayList<>());
    }
}
