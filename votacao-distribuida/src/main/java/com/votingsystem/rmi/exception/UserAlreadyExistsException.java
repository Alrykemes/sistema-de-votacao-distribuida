package com.votingsystem.rmi.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String username) {
        super("User Already exists with username: " + username);
    }
}

