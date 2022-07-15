package com.example.articlewebapp.web.rest;

import com.example.articlewebapp.aop.logging.Loggable;
import com.example.articlewebapp.domain.User;
import com.example.articlewebapp.exception.EmailAlreadyUsedException;
import com.example.articlewebapp.exception.UsernameAlreadyUsedException;
import com.example.articlewebapp.service.MailService;
import com.example.articlewebapp.service.UserService;
import com.example.articlewebapp.dto.UserDTO;
import com.example.articlewebapp.web.rest.payload.KeyAndPasswordRequest;
import com.example.articlewebapp.web.rest.payload.PasswordChange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

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
    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final UserService userService;

    private final MailService mailService;


    /**
     * {@code POST  /register} : register the user.
     *
     * @param userDTO the managed user  Model.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws UsernameAlreadyUsedException {@code 400 (Bad Request)} if the username is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Loggable
    public void registerAccount(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.registerUser(userDTO);
        mailService.sendActivationEmail(user);

    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    @Loggable
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChange current and new password.
     */
    @PostMapping(path = "/account/change-password")
    @Loggable
    public void changePassword(@RequestBody @Valid PasswordChange passwordChange) {
        userService.changePassword(passwordChange.getCurrentPassword(), passwordChange.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @Loggable
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get());
        } else {
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    @Loggable
    public void finishPasswordReset(@Valid @RequestBody KeyAndPasswordRequest keyAndPassword) {
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

}
