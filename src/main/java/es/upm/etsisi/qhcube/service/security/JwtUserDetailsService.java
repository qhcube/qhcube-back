package es.upm.etsisi.qhcube.service.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService {

        @Override
        public UserDetails loadUserByUsername(String username) {
            return new User(username, "", new ArrayList<>());
        }
}
