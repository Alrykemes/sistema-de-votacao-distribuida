package com.votingsystem.rmi.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.votingsystem.rmi.domain.user.User;
import com.votingsystem.rmi.domain.user.UserDto;
import org.bson.Document;

public class UserRepository implements UserRepositoryInterface {
    private MongoConnection mongoConnection;
    private MongoCollection<Document> userCollection;

    public UserRepository() {
        this.mongoConnection = new MongoConnection();
        this.userCollection = mongoConnection.getUserCollection();
    }

    @Override
    public void save(User user) {
        Document doc = new Document("username", user.getUsername())
                .append("password", user.getPassword());
        this.userCollection.insertOne(doc);
    }

    @Override
    public UserDto findByUsername(String username) {
        Document doc = userCollection.find(new Document("username", username)).first();

        if (doc == null) return null;

        return new UserDto(doc.getString("id"), doc.getString("username"), doc.getString("password"));
    }
}
