package com.example.articlewebapp.exception;

public class ResourceDoesntExistException extends RuntimeException {
    public ResourceDoesntExistException(String s) {
        super(s);
    }
}
