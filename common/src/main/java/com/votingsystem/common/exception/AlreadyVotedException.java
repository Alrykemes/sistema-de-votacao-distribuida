package com.votingsystem.common.exception;

public class AlreadyVotedException extends RuntimeException {
    public AlreadyVotedException() {
        super("User has already voted in this poll");
    }
}
