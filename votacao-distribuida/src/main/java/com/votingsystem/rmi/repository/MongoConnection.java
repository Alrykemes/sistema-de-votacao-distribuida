package com.votingsystem.rmi.repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoConnection {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private Logger log;

    public MongoConnection() {
        log = Logger.getLogger("global");

        ConnectionString connectionString = new ConnectionString("mongodb://bancodeDatabase:askjKLSDJFBN342523ASKJDFNasasfkjnadsfkjadfb34634@mongodb:27017");

        // Configura codec registry para suportar POJOs
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)  // adiciona suporte a POJOs
                .build();

        mongoClient = MongoClients.create(settings);
        mongoDatabase = mongoClient.getDatabase("voting-system");
        log.info("Conectado ao MongoDB com suporte a POJOs!");

        ensureCollectionExists("users");
        ensureCollectionExists("polls");
    }

    // Agora você pode especificar o tipo da coleção para facilitar o uso
    public MongoCollection getUserCollection() {
        return this.mongoDatabase.getCollection("users");
    }

    public MongoCollection<com.votingsystem.rmi.domain.poll.Poll> getPollCollection() {
        return this.mongoDatabase.getCollection("polls", com.votingsystem.rmi.domain.poll.Poll.class);
    }

    private void ensureCollectionExists(String collectionName) {
        Set<String> existingCollections = new HashSet<>();

        MongoIterable<String> collectionNames = this.mongoDatabase.listCollectionNames();

        for (String name : collectionNames) {
            existingCollections.add(name);
        }

        if (!existingCollections.contains(collectionName)) {
            log.info("Coleção '" + collectionName + "' não encontrada. Criando...");
            try {
                this.mongoDatabase.createCollection(collectionName);
                log.info("Coleção '" + collectionName + "' criada com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro ao criar a coleção '" + collectionName + "': " + e.getMessage());
            }
        }
    }
}
