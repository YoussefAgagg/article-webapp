package com.example.articlewebapp.service;

import com.example.articlewebapp.aop.logging.Loggable;
import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.domain.UserFollowersFollowing;
import com.example.articlewebapp.exception.BadRequestException;
import com.example.articlewebapp.exception.EmailAlreadyUsedException;
import com.example.articlewebapp.exception.InvalidPasswordException;
import com.example.articlewebapp.exception.UsernameAlreadyUsedException;
import com.example.articlewebapp.repository.UserFollowersFollowingRepository;
import com.example.articlewebapp.repository.UserRepository;
import com.example.articlewebapp.security.AuthoritiesConstants;
import com.example.articlewebapp.security.SecurityUtils;
import com.example.articlewebapp.dto.AuthorityDTO;
import com.example.articlewebapp.dto.UserDTO;
import com.example.articlewebapp.dto.mapper.UserMapper;
import com.example.articlewebapp.web.rest.payload.UpdateUserData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * Service Implementation for managing {@link User}.
 *
 * @author Youssef Agagg
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserFollowersFollowingRepository  userFollowersFollowingRepository;
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
    @Loggable
    public Optional<UserDTO> updateUser(UpdateUserData userData) {
        return Optional
                .of(userRepository.findById(userData.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    user.setFirstName(userData.getFirstName());
                    user.setLastName(userData.getLastName());
                    user.setEmail(userData.getEmail().toLowerCase());
                    user.setGender(userData.getGender());
                    user.setDateOfBirth(userData.getDateOfBirth());
                    user.setMobile(userData.getMobile());
                    log.debug("Changed Information for User: {}", user);
                    return user;
                })
                .map(userMapper::toDto);
    }
    @Transactional(readOnly = true)
    @Loggable
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }
    @Loggable
    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findOneByUsername(username.toLowerCase()).map(userMapper::toDto);
    }
    @Loggable
    public void deleteUser(String username) {
        userRepository
                .findOneByUsername(username.toLowerCase())
                .ifPresent(user -> {
                    userRepository.delete(user);
                    log.debug("Deleted User: {}", user);
                });
    }
    @Loggable
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Loggable
    public void followUser(Long followingId) {
        User userLogin = getLoginUser();
        if (Objects.equals(userLogin.getId(),followingId)){
            throw new BadRequestException("user try to follow himself");
        }

        UserFollowersFollowing userFollowersFollowing= getFollowersFollowing(followingId, userLogin);
        userFollowersFollowingRepository.save(userFollowersFollowing);
    }

    private UserFollowersFollowing getFollowersFollowing(Long followingId, User userLogin) {
        User toFollow=userRepository.findById(followingId)
                .orElseThrow(()->{throw new BadRequestException("the user doesn't exist");});

        UserFollowersFollowing.UserFollowersFollowingID id=new UserFollowersFollowing.UserFollowersFollowingID();
        id.setUser(toFollow);
        id.setFollowerId(userLogin);
        UserFollowersFollowing userFollowersFollowing=new UserFollowersFollowing();
        userFollowersFollowing.setId(id);
        return userFollowersFollowing;
    }

    @Loggable
    public Page<UserDTO> getUserFollowers(String username, Pageable pageable) {
        return userRepository.findAllByFollowingUsername(username, pageable)
                .map(userMapper::toDto);

    }
    @Loggable
    public Page<UserDTO> getUserFollowing(String username, Pageable pageable) {
        return userRepository.findAllByFollowersUsername(username, pageable)
                .map(userMapper::toDto);
    }
    @Loggable
    public void unfollowUser(Long followingId) {
        User userLogin = getLoginUser();
        if (Objects.equals(userLogin.getId(),followingId)){
            throw new BadRequestException("user try ro unfollow himself");
        }
        userFollowersFollowingRepository.delete(getFollowersFollowing(followingId, userLogin));
    }

    private User getLoginUser() {
        return SecurityUtils
                .getCurrentUserUsername()
                .flatMap(userRepository::findOneByUsername)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Current user login not found"));
    }
}
