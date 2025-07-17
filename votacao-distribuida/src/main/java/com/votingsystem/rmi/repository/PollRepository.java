package com.votingsystem.rmi.repository;

import com.mongodb.client.MongoCollection;
import com.votingsystem.rmi.domain.poll.Poll;

public class PollRepository implements PollRepositoryInterface {
    private MongoConnection mongoConnection;
    private MongoCollection pollCollection;

    public PollRepository() {
        this.mongoConnection = new MongoConnection();
        this.pollCollection = mongoConnection.getPollCollection();
    }

    @Override
    public void save(Poll poll) {

    }

    @Override
    public Poll findAll() {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void vote(String id, String optionId) {

    }
}
