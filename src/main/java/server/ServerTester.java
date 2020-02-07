package server;

import static db.mongo.documents.TestDocument.Entry.AUTHOR_ID;
import static db.mongo.documents.TestDocument.Entry.CHANNEL_ID;
import static db.mongo.documents.TestDocument.Entry.REMINDER_MESSAGE;
import static db.mongo.documents.TestDocument.Entry.TIME;

import java.util.Arrays;
import java.util.function.Consumer;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import db.mongo.MongoDatabaseFactory;
import db.mongo.documents.TestDocument;
import db.mongo.documents.TestQueryDocument;
import db.mongo.documents.util.Query;
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

        final MongoDocumentHandler handler = new MongoDocumentHandler(developTestDb);

        try {
            handler.tryInsert(new TestDocument(1L, 1L, 1L, "seconds", ""));

            TestQueryDocument.Builder builder = new TestQueryDocument.Builder();

            // final TestQueryDocument query = builder.addQueries(
            //     b -> b.query(AUTHOR_ID, 1L)
            // ).build();

            Query[] queries = new Query[] {
                new Query<>(AUTHOR_ID, 1L),
                new Query<>(TIME, 1L)
            };

            final TestQueryDocument query = builder.addQueries(queries)
                                                   .build();

            final FindIterable<Document> documents = handler.tryFind(query);
            for(Document document : documents) {

            }


        } catch(DocumentSerializationException | CollectionNotFoundException e) {
            e.printStackTrace();
        }
    }
}
