package com.example.articlewebapp.exception;

import org.springframework.security.core.AuthenticationException;

/**
 *  This exception is thrown in case of a not activated user trying to authenticate.
 *
 * @author Youssef Agagg
 */
public class UserNotActivatedException extends AuthenticationException {
    public UserNotActivatedException(String message) {
        super(message);
    }
}
