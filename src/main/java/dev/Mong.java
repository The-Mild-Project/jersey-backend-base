package dev;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientSettings;

public class Mong {
    private ConnectionString connString = new ConnectionString(
        "mongodb+srv://themildproject:CSKOFoLmrQ10XR2s@cluster0-4uxsi.mongodb.net/test?retryWrites=true&w=majority"
    );
    private MongoClientSettings settings = MongoClientSettings.builder()
                                       .applyConnectionString(connString)
                                       .retryWrites(true)
                                       .build();
    // MongoClients = MongoClient factory
    private MongoClient mongoClient = MongoClients.create(settings);
    private MongoDatabase database = mongoClient.getDatabase("test");
}
