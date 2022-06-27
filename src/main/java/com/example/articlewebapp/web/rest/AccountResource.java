package com.example.articlewebapp.web.rest;

import com.example.articlewebapp.exception.EmailAlreadyUsedException;
import com.example.articlewebapp.exception.InvalidPasswordException;
import com.example.articlewebapp.exception.UsernameAlreadyUsedException;
import com.example.articlewebapp.service.MailService;
import com.example.articlewebapp.service.UserService;
import com.example.articlewebapp.service.dto.UserDTO;
import com.example.articlewebapp.web.rest.payload.KeyAndPasswordRequest;
import com.example.articlewebapp.web.rest.payload.PasswordChange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * REST controller for managing the current user's account.
 *
 * @author Youssef Agagg
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class AccountResource {


    private final UserService userService;

    private final MailService mailService;


    /**
     * {@code POST  /register} : register the user.
     *
     * @param userDTO the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws UsernameAlreadyUsedException {@code 400 (Bad Request)} if the username is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody UserDTO userDTO) {
       // TODO implement registration of new user
        log.debug("register new user {}",userDTO);
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        // TODO: implement activation  of new user
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChange current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChange passwordChange) {
        // TODO: implement changing user password
        log.debug("changed password {}",passwordChange);
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        // TODO: implement password reset
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordRequest keyAndPassword) {
        // TODO: implement password reset
    }

}
