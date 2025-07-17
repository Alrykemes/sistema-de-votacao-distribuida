package com.votingsystem.centralserver.repository;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.stream.StreamSupport;

public class MongoConnection {
    private static final String DATABASE_NAME = "voting-system";
    private static final String COLLECTION_USERS = "users";
    private static final String COLLECTION_POLLS = "polls";

    private static final MongoClient mongoClient;
    private static final MongoDatabase mongoDatabase;

    static {
        String uri = System.getenv("MONGODB_URI");
        mongoClient = MongoClients.create(uri);
        mongoDatabase = mongoClient.getDatabase(DATABASE_NAME);

        ensureCollectionExists(COLLECTION_USERS);
        ensureCollectionExists(COLLECTION_POLLS);
    }

    public static MongoCollection<Document> getUserCollection() {
        return mongoDatabase.getCollection(COLLECTION_USERS);
    }

    public static MongoCollection<Document> getPollCollection() {
        return mongoDatabase.getCollection(COLLECTION_POLLS);
    }

    private static void ensureCollectionExists(String collectionName) {
        boolean exists = StreamSupport
                .stream(mongoDatabase.listCollectionNames().spliterator(), false)
                .anyMatch(name -> name.equals(collectionName));

        if (!exists) {
            try {
                mongoDatabase.createCollection(collectionName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
