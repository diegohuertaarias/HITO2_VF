package com.empresa.hito2;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DatabaseService {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public void connect(String connectionString, String dbName, String collectionName) {
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(dbName);
        collection = database.getCollection(collectionName);
    }

    public void insert(Document document) {
        collection.insertOne(document);
    }

    // Otros métodos CRUD (read, update, delete)
}
s