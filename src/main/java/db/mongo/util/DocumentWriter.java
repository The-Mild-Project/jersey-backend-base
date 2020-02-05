package db.mongo.util;

import java.util.Objects;

import org.bson.Document;

import annotations.mongo.documents.DocumentSerializable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import db.mongo.documents.util.BaseDocument;
import db.mongo.util.exceptions.DocumentSerializationException;

public final class DocumentWriter {

    private final MongoDatabase database;

    public DocumentWriter(MongoDatabase database) {
        this.database = database;
    }

    private DocumentWriter write(BaseDocument document) {
        try {
            final String collectionName = getCollectionNameIfSerializable(document);
            final MongoCollection<Document> collection = database.getCollection(collectionName);
            final Document doc = document.getDocument();

            collection.insertOne(doc);
        } catch(DocumentSerializationException e) {
            System.out.println(e.getMessage());
        }
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

        final String collectionName;
        final Class<?> dClass = document.getClass();
        if(dClass.isAnnotationPresent(DocumentSerializable.class)) {
            final DocumentSerializable annotation = dClass.getAnnotation(DocumentSerializable.class);
            collectionName = annotation.collectionName();
        } else {
            collectionName = null;
        }
        return collectionName;
    }
}
