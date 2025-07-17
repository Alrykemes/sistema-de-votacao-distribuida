package com.votingsystem.centralserver.repository;

import com.votingsystem.common.domain.User;

public interface UserRepositoryInterface {
    void save(User user);
    User findByUsername(String username);
}
