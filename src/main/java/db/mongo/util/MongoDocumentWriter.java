package db.mongo.util;

import java.util.Objects;

import org.bson.Document;

import annotations.mongo.documents.DocumentSerializable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import db.mongo.documents.util.BaseDocument;
import db.mongo.util.exceptions.CollectionNotFoundException;
import db.mongo.util.exceptions.DocumentSerializationException;

public final class MongoDocumentWriter {

    private final MongoDatabase database;

    public MongoDocumentWriter(MongoDatabase database) {
        this.database = database;
    }

    private MongoDocumentWriter write(BaseDocument document) throws DocumentSerializationException, CollectionNotFoundException {
        final String collectionName = getCollectionNameIfSerializable(document);
        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if(collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        final Document doc = document.getDocument();
        collection.insertOne(doc);

        return this;
    }

    private void checkIfSerializable(BaseDocument document) throws DocumentSerializationException {
        if (Objects.isNull(document)) {
            throw new DocumentSerializationException("The object to serialize is null");
        }

        final Class<?> dClass = document.getClass();
        if (!dClass.isAnnotationPresent(DocumentSerializable.class)) {
            throw new DocumentSerializationException(String.format("The class %s is not annotated with DocumentSerializable.",
                                                                   dClass.getSimpleName()));
        }
    }

    private String getCollectionNameIfSerializable(BaseDocument document) throws DocumentSerializationException {
        checkIfSerializable(document);

        final Class<?> dClass = document.getClass();
        final DocumentSerializable annotation = dClass.getAnnotation(DocumentSerializable.class);

        return annotation.collectionName();
    }
}
