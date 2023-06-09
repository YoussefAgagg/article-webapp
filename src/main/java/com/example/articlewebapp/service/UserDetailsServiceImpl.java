package com.example.articlewebapp.service;
import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.exception.UserNotActivatedException;
import com.example.articlewebapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The implementation of service used to query user details during login.
 *
 * @author Youssef Agagg
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Authenticating {}", username);

        if (!(Objects.isNull(username)||username.isBlank())) {
            var storedUser =userRepository.findOneByUsername(username);
           return storedUser.map(user -> createSpringSecurityUser( user))
                    .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " was not found in the database"));
        }
        return null;
    }
    private org.springframework.security.core.userdetails.User createSpringSecurityUser( User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + user.getUsername() + " was not activated");
        }
        var grantedAuthorities = user
                .getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
