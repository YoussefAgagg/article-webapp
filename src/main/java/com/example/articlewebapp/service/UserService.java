package com.example.articlewebapp.service;

import com.example.articlewebapp.aop.logging.Loggable;
import com.example.articlewebapp.domain.Authority;
import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.exception.EmailAlreadyUsedException;
import com.example.articlewebapp.exception.InvalidPasswordException;
import com.example.articlewebapp.exception.UsernameAlreadyUsedException;
import com.example.articlewebapp.repository.AuthorityRepository;
import com.example.articlewebapp.repository.UserRepository;
import com.example.articlewebapp.security.AuthoritiesConstants;
import com.example.articlewebapp.security.SecurityUtils;
import com.example.articlewebapp.service.dto.AuthorityDTO;
import com.example.articlewebapp.service.dto.UserDTO;
import com.example.articlewebapp.service.dto.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final UserMapper userMapper;
    @Loggable
    public User registerUser(UserDTO userDTO) {
        userRepository
                .findOneByUsername(userDTO.getUsername().toLowerCase())
                .ifPresent(existingUser -> {
                        throw new UsernameAlreadyUsedException();
                 });
        userRepository
                .findOneByEmailIgnoreCase(userDTO.getEmail())
                .ifPresent(existingUser -> {
                        throw new EmailAlreadyUsedException();
                });
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setActivated(false);
        userDTO.setUsername(userDTO.getUsername().toLowerCase());
        userDTO.setActivationKey(UUID.randomUUID().toString());
        AuthorityDTO authority=new AuthorityDTO();
        authority.setName(AuthoritiesConstants.USER);
        userDTO.setAuthorities(Set.of(authority));
        User newUser =userMapper.toEntity(userDTO);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
    @Loggable
    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
                .findOneByActivationKey(key)
                .map(user -> {
                    user.setActivated(true);
                    user.setActivationKey(null);
                    log.debug("Activated user: {}", user);
                    return user;
                });
    }

    @Transactional
    @Loggable
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
                .getCurrentUserUsername()
                .flatMap(userRepository::findOneByUsername)
                .ifPresent(user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    log.debug("Changed password for User: {}", user);
                });
    }

    @Loggable
    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
                .findOneByEmailIgnoreCase(mail)
                .filter(User::isActivated)
                .map(user -> {
                    user.setResetKey(UUID.randomUUID().toString());
                    return user;
                });
    }

    @Loggable
    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
                .findOneByResetKey(key)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    return user;
                });
    }
}
