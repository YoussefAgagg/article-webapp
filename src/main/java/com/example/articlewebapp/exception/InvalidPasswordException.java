package com.example.articlewebapp.exception;

/**
 *  This exception is thrown in case of a user enter an invalid password.
 *
 * @author Youssef Agagg
 */
public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException() {
        super("Incorrect password");
    }
}
