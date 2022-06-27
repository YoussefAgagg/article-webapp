package com.example.articlewebapp.exception;

/**
 *  This exception is thrown in case of a user enter a Username already exist.
 *
 * @author Youssef Agagg
 */

public class UsernameAlreadyUsedException extends RuntimeException{
    public UsernameAlreadyUsedException() {
        super("Username is already in use!");
    }
}
