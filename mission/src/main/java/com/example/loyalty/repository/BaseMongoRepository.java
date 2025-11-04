package com.example.loyalty.repository;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * Base repository class providing common MongoDB operations.
 * 
 * Uses dependency injection for MongoClient instead of static access.
 */
@Repository
public abstract class BaseMongoRepository {

    private final MongoClient mongoClient;
    private final String databaseName;

    @Autowired
    public BaseMongoRepository(MongoClient mongoClient, String mongoDatabaseName) {
        this.mongoClient = mongoClient;
        this.databaseName = mongoDatabaseName;
    }

    protected MongoCollection<Document> getCollection(String collectionName) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        return database.getCollection(collectionName);
    }

    protected Document findById(String collectionName, String id) {
        MongoCollection<Document> collection = getCollection(collectionName);
        return collection.find(Filters.eq("_id", new ObjectId(id))).first();
    }

    protected Document findOne(String collectionName, String field, Object value) {
        MongoCollection<Document> collection = getCollection(collectionName);
        return collection.find(Filters.eq(field, value)).first();
    }
}
