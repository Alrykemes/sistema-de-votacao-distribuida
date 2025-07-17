package com.votingsystem.rmi.repository;

import com.votingsystem.rmi.domain.user.User;
import com.votingsystem.rmi.domain.user.UserDto;

public interface UserRepositoryInterface {
    public void save(User user);
    public UserDto findByUsername(String username);
}
