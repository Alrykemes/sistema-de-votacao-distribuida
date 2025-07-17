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
        if (poll == null) throw new IllegalArgumentException("Poll não pode ser nulo");

        pollCollection.replaceOne(
                Filters.eq("id", poll.getId()),
                poll,
                new ReplaceOptions().upsert(true)
        );
    }

    @Override
    public List<Poll> findAll() {
        List<Poll> polls = new ArrayList<>();
        pollCollection.find().into(polls);
        return polls;
    }

    public Poll findByTitle(String title) {
        return pollCollection.find(Filters.eq("title", title)).first();
    }

    @Override
    public void vote(String id, String optionId) {}
}
