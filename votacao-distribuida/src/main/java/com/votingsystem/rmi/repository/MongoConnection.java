package com.votingsystem.rmi.repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class MongoConnection {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private Logger log;

    public MongoConnection() {
        log = Logger.getLogger("global");
        ConnectionString connectionString = new ConnectionString("mongodb://bancodeDatabase:askjKLSDJFBN342523ASKJDFNasasfkjnadsfkjadfb34634@mongodb:27017");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        mongoClient = MongoClients.create(settings);

        mongoDatabase = mongoClient.getDatabase("voting-system");
        log.info("Conectado ao MongoDB!");

        ensureCollectionExists("users");
        ensureCollectionExists("polls");
    }

    public MongoCollection getUserCollection() {
        return this.mongoDatabase.getCollection("users");
    }

    public MongoCollection getPollCollection() {
        return this.mongoDatabase.getCollection("polls");
    }

    // Metodo que verifica a existência da collection
    private void ensureCollectionExists(String collectionName) {
        Set<String> existingCollections = new HashSet<>();

        MongoIterable<String> collectionNames = this.mongoDatabase.listCollectionNames();

        for (String name : collectionNames) {
            existingCollections.add(name);
        }

        if (!existingCollections.contains(collectionName)) {
            log.info("Coleção '" + collectionName + "' não encontrada. Criando...");
            try {
                // Tenta criar a coleção
                this.mongoDatabase.createCollection(collectionName);
                log.info("Coleção '" + collectionName + "' criada com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro ao criar a coleção '" + collectionName + "': " + e.getMessage());
            }
        }
    }
}
