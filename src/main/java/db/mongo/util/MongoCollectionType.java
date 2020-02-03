package db.mongo.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public enum MongoCollectionType
{
    NULL("");

    private static final Map<String, MongoCollectionType> COLLECTIONS_BY_NAME = new HashMap<>();
    
    static {
        Arrays.asList(MongoCollectionType.values())
              .forEach(collection -> COLLECTIONS_BY_NAME.put(collection.collectionName, collection));
    }
    
    private String collectionName;
    
    MongoCollectionType(String collectionName) {
        this.collectionName = collectionName;
    }
    
    public String collectionName() {
        return this.collectionName;
    }
    
    public static MongoCollectionType getByName(String name) {
        return COLLECTIONS_BY_NAME.getOrDefault(name, NULL);
    }
}
