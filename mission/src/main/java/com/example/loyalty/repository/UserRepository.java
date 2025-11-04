package com.example.loyalty.repository;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoClient;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Repository class for managing user-related MongoDB operations.
 */
@Repository
public class UserRepository extends BaseMongoRepository {

    private static final String COLLECTION_NAME = "users";

    @Autowired
    public UserRepository(MongoClient mongoClient, String mongoDatabaseName) {
        super(mongoClient, mongoDatabaseName);
    }

    /**
     * Finds a user by storeFrontId and retailerId.
     *
     * @param storeFrontId the store ID
     * @param retailerId   the retailer's ObjectId string
     * @return the found user document or null
     */
    public Document findUserByStoreIdAndRetailerId(String storeFrontId, String retailerId) {
        return getCollection(COLLECTION_NAME)
                .find(and(
                        eq("storeFrontId", storeFrontId),
                        eq("_id", new ObjectId(retailerId))))
                .first();
    }
}
