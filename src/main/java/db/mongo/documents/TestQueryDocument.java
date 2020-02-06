package db.mongo.documents;

import static db.mongo.util.MongoCollections.TEST_NAME;

import java.util.function.Consumer;

import org.bson.Document;

import annotations.mongo.documents.DocumentSerializable;
import db.mongo.documents.util.QueryDocument;

@DocumentSerializable(collectionName = TEST_NAME)
public class TestQueryDocument extends QueryDocument<TestDocument.Entry> {

    public TestQueryDocument() {
        super();
    }

    public TestQueryDocument findWhere(Query query) {
        putQuery(query);
        return this;
    }

    public TestQueryDocument findWhere(Query query, Query... optionalQueries) {
        putQuery(query);
        putQueries(optionalQueries);
        return this;
    }

    public TestQueryDocument findWhere(Consumer<Query> query) {
        // Query document
        // query.accept();
        return this;
    }

    @Override
    public Document getDocument() {
        return document;
    }

    public static final class Builder extends QueryDocument.Builder<TestQueryDocument> {
        protected Builder() {
            super(new TestQueryDocument());
        }

        
    }
}
