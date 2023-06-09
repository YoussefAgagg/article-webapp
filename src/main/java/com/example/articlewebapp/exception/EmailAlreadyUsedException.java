package com.example.articlewebapp.exception;
/**
 *  This exception is thrown in case of a user enter an email already exist.
 *
 * @author Youssef Agagg
 */

public class EmailAlreadyUsedException extends RuntimeException{
    public EmailAlreadyUsedException() {
        super("Email is already in use!");
    }
}
