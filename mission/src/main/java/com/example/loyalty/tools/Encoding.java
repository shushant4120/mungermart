package com.example.loyalty.tools;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Encoding {

    public static void main(String[] args) {
        try {
            // Encode username and password
            String username = URLEncoder.encode("mungermart1_db_user", StandardCharsets.UTF_8.toString());
            String password = URLEncoder.encode("gpFKdX6JGKHj89nG", StandardCharsets.UTF_8.toString());

            // Full cluster hostname
            String clusterHost = "mungermart.iagr5pv.mongodb.net";

            // Auth details
            String authSource = "admin";
            String authMechanism = "SCRAM-SHA-1"; // Atlas default

            // Database and collection to read
            String dbName = "dev";
            String collName = "users";

            // Build the MongoDB URI
            String uri = String.format(
                    "mongodb+srv://%s:%s@%s/%s?authSource=%s&authMechanism=%s&retryWrites=true&w=majority&appName=mungermart",
                    username, password, clusterHost, dbName, authSource, authMechanism);

            System.out.println("Connecting to MongoDB URI: " + uri);

            // Connect to MongoDB
            MongoClient mongoClient = MongoClients.create(uri);

            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collName);

            // Print all documents in the collection
            collection.find().forEach(doc -> System.out.println(doc.toJson()));

            // Close the client
            mongoClient.close();

        } catch (Exception e) {
            System.err.println("Connection or query failed:");
            e.printStackTrace();
        }
    }
}
