package db.mongo.documents;

import static db.mongo.util.MongoCollections.TEST_NAME;

import annotations.mongo.documents.DocumentSerializable;
import db.mongo.documents.util.QueryDocument;

@DocumentSerializable(collectionName = TEST_NAME)
public class TestQueryDocument extends QueryDocument<TestDocument.Entry> {

    public TestQueryDocument() {
        super();
    }

    public TestQueryDocument where(Query query) {
        putQuery(query);
        return this;
    }

    public TestQueryDocument where(Query query, Query... optionalQueries) {
        putQuery(query);
        putQueries(optionalQueries);
        return this;
    }
}
