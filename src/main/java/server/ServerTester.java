package server;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.mongodb.client.MongoDatabase;
import db.mongo.MongoDatabaseFactory;
import db.mongo.documents.TestDocument;
import db.mongo.util.MongoDocumentHandler;
import db.mongo.util.MongoDatabaseType;
import db.mongo.util.MongoEnv;
import db.mongo.util.exceptions.CollectionNotFoundException;
import db.mongo.util.exceptions.DocumentSerializationException;

public class ServerTester {
    public static void main(String[] args) {
        final MongoEnv env = new MongoEnv();
        final MongoDatabaseFactory factory = new MongoDatabaseFactory(env.getMongoUser(), env.getMongoPass());

        Arrays.stream(MongoDatabaseType.values())
              .forEach(type -> factory.addDatabase(type, env.getMongoCluster()));

        final MongoDatabase developTestDb = factory.getDatabase(MongoDatabaseType.DEVELOP_TEST);

        final MongoDocumentHandler writer = new MongoDocumentHandler(developTestDb);
        
        try {
            writer.tryWrite(new TestDocument(1L, 1L, 1L, "seconds", ""));
        } catch(DocumentSerializationException | CollectionNotFoundException e) {
            e.printStackTrace();
        }

        // developTestDb.getCollection();
    }
}
