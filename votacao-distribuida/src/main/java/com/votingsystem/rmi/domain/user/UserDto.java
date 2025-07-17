package com.votingsystem.rmi.domain.user;

import java.io.Serializable;

public record UserDto(String id, String username, String password) implements Serializable {
    private static final long serialVersionUID = 1L;
}
