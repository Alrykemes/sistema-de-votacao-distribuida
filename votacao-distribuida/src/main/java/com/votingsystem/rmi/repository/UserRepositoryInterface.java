package com.votingsystem.rmi.repository;

import com.votingsystem.rmi.domain.User;

public interface UserRepositoryInterface {
    public void save(User user);
    public User findByUsername(String username);
}
