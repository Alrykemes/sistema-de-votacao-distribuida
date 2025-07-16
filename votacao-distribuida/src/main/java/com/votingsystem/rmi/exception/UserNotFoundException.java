package com.votingsystem.rmi.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String id) {
        super("User not found with ID: " + id);
    }
}

