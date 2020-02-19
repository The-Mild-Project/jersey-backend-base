package com.the.mild.project;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum MongoCollections {
    TEST("test"),
    TODO("todo"),
    NULL("");

    public static final String TEST_NAME = "test";
    public static final String TODO_NAME = "todo";

    private static final Map<String, MongoCollections> COLLECTIONS_BY_NAME = new HashMap<>();

    static {
        Arrays.asList(MongoCollections.values())
              .forEach(collection -> COLLECTIONS_BY_NAME.put(collection.collectionName, collection));
    }

    private String collectionName;

    MongoCollections(String collectionName) {
        this.collectionName = collectionName;
    }

    public String collectionName() {
        return this.collectionName;
    }

    public static MongoCollections getByName(String name) {
        return COLLECTIONS_BY_NAME.getOrDefault(name, NULL);
    }
}
