package com.votingsystem.centralserver.repository;

import com.mongodb.client.MongoCollection;
import com.votingsystem.common.domain.User;
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
        Document doc = new Document()
                .append("username", user.getUsername())
                .append("password", user.getPassword());

        this.userCollection.insertOne(doc);
    }

    @Override
    public User findByUsername(String username) {
        Document doc = userCollection.find(new Document("username", username)).first();

        if (doc == null) return null;

        return new User(
                doc.getObjectId("_id").toHexString(),
                doc.getString("username"),
                doc.getString("password")
        );
    }
}
