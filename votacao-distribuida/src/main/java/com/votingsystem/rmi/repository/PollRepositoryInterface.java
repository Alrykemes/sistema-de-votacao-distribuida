package com.votingsystem.rmi.repository;

import com.votingsystem.rmi.domain.poll.Poll;
import com.votingsystem.rmi.domain.user.User;

public interface PollRepositoryInterface {
    public void save(Poll poll);
    public Poll findAll();
    public void delete(String id);
    public void vote(String id, String optionId);
}
