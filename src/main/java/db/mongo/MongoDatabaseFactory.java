package db.mongo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import db.mongo.util.MongoCollectionType;

public final class MongoDatabaseFactory {
    // "mongodb+srv://<username>:<password>@<cluster name>.mongodb.net/<database name>";
    private static final String CONNECTION_STRING_FORMAT = "mongodb+srv://%s:%s@%s.mongodb.net/%s";
    private static final Map<MongoCollectionType, DatabaseProxy> DATABASE_BY_PROXY = new HashMap<>();

    private final String username;
    private final String password;

    public MongoDatabaseFactory(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addDatabase(MongoCollectionType type, String clusterName, String databaseName) {
        DATABASE_BY_PROXY.put(type, new DatabaseProxy(clusterName, databaseName));
    }

    public MongoDatabase getDatabase(MongoCollectionType type) {
        assert DATABASE_BY_PROXY.containsKey(type);

        return DATABASE_BY_PROXY.get(type).database;
    }

    private final class DatabaseProxy {
        private final String clusterName;
        private final String databaseName;

        private final ConnectionString connectionString;
        private final MongoDatabase database;

        private DatabaseProxy(String clusterName, String databaseName) {
            this.clusterName = clusterName;
            this.databaseName = databaseName;
            this.connectionString = new ConnectionString(
                String.format(CONNECTION_STRING_FORMAT, username, password, clusterName, databaseName));
            this.database = initDatabase();
        }

        private MongoDatabase initDatabase() {
            final MongoClientSettings settings = MongoClientSettings.builder()
                                                                    .applyConnectionString(connectionString)
                                                                    .retryWrites(true)
                                                                    .writeConcern(WriteConcern.MAJORITY)
                                                                    .build();

            final MongoClient mongoClient = MongoClients.create(settings);
            final MongoDatabase db = mongoClient.getDatabase(databaseName);

            initCollections(db);

            return db;
        }

        private void initCollections(MongoDatabase db) {
            Arrays.stream(MongoCollectionType.values())
                  .filter(type -> type != MongoCollectionType.NULL)
                  .forEach(type -> {
                      final String name = type.collectionName();
                      MongoCollection collection = db.getCollection(name);
                      if(collection == null) {
                          db.createCollection(name, null);
                      }
                  });
        }
    }
}
