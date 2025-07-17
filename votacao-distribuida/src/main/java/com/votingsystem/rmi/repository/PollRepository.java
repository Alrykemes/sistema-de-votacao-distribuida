package com.votingsystem.rmi.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.votingsystem.rmi.domain.poll.Poll;
import com.votingsystem.rmi.domain.poll.PollOption;

import java.util.ArrayList;
import java.util.List;

public class PollRepository implements PollRepositoryInterface {
    private MongoConnection mongoConnection;
    private MongoCollection<Poll> pollCollection;


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

    @Override
    public Poll findByTitle(String title) {
        return pollCollection.find(Filters.eq("title", title)).first();
    }

    @Override
    public void vote(String pollId, String optionId) {
        if (pollId == null || optionId == null) {
            throw new IllegalArgumentException("pollId and optionId cannot be null");
        }

        Poll poll = pollCollection.find(Filters.eq("id", pollId)).first();

        if (poll == null) {
            throw new IllegalArgumentException("Poll with ID " + pollId + " not found");
        }

        boolean optionFound = false;

        for (PollOption option : poll.getPollOptionsList()) {
            if (option.getId().equals(optionId)) {
                option.incrementVotes();
                optionFound = true;
                break;
            }
        }

        if (!optionFound) {
            throw new IllegalArgumentException("Option with ID " + optionId + " not found in poll " + pollId);
        }

        pollCollection.replaceOne(Filters.eq("id", pollId), poll, new ReplaceOptions().upsert(true));
    }
}