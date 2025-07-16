package com.votingsystem.rmi.repository;

import com.mongodb.client.MongoCollection;
import com.votingsystem.rmi.domain.User;
import org.bson.Document;

public class UserRepository implements UserRepositoryInterface {
    private MongoConnection mongoConnection;
    private MongoCollection userCollection;

    public UserRepository() {
        this.mongoConnection = new MongoConnection();
        this.userCollection = mongoConnection.getUserCollection();
    }

    @Override
    public void save(User user) {
        this.userCollection.insertOne(user);
    }

    @Override
    public User findByUsername(String username) {
        return (User) userCollection.find(new Document("username", username)).first();
    }
}
