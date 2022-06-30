package com.example.articlewebapp.web.rest;


import com.example.articlewebapp.aop.logging.Loggable;
import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.exception.ResourceDoesntExistException;
import com.example.articlewebapp.exception.ChangeUsernameException;
import com.example.articlewebapp.exception.EmailAlreadyUsedException;
import com.example.articlewebapp.repository.UserRepository;
import com.example.articlewebapp.security.AuthoritiesConstants;
import com.example.articlewebapp.security.SecurityUtils;
import com.example.articlewebapp.service.UserService;
import com.example.articlewebapp.service.dto.UserDTO;
import com.example.articlewebapp.web.rest.payload.UpdateUserData;
import com.example.articlewebapp.web.rest.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link User}
 *
 * @author Youssef Agagg.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;

    private final UserRepository userRepository;


    @PutMapping("/users/{username}")
    @Loggable
    @PreAuthorize("#username.toLowerCase() == authentication.principal.username")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UpdateUserData userData,@PathVariable String username) {
        log.debug("REST request to update User : {}", userData);
        String userLogin = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Current user login not found"));
        if(!userLogin.equalsIgnoreCase(userData.getUsername())){
            throw new ChangeUsernameException( "username can not be changed");
        }
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userData.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getUsername().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }

        Optional<UserDTO> updatedUser = userService.updateUser(userData);

        return updatedUser.map((response) -> ResponseEntity.ok().body(response))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    @Loggable
    public ResponseEntity<List<UserDTO>> getAllUsers( @PageableDefault(sort = { "id" })Pageable pageable) {
        log.debug("REST request to get all User for an admin");

        final Page<UserDTO> page = userService.getAllUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/users/{username}")
    @Loggable
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        log.debug("REST request to get User : {}", username);
        return userService.getUserByUsername(username).map((response) -> ResponseEntity.ok().body(response))
                .orElseThrow(() -> new ResourceDoesntExistException("this  account doesn't exist"));
    }


    @DeleteMapping("/users/{username}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or #username.toLowerCase() == authentication.principal.username")
    @Loggable
    public ResponseEntity<Void> deleteUser(@PathVariable  String username) {
        log.debug("REST request to delete User: {}", username);
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
