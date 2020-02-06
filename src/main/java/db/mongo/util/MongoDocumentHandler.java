package db.mongo.util;

import java.util.Objects;

import org.bson.Document;

import annotations.mongo.documents.DocumentEntryKeys;
import annotations.mongo.documents.DocumentSerializable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import db.mongo.documents.util.BaseDocument;
import db.mongo.documents.util.InsertDocument;
import db.mongo.documents.util.InsertDocumentEntry;
import db.mongo.documents.util.QueryDocument;
import db.mongo.util.exceptions.CollectionNotFoundException;
import db.mongo.util.exceptions.DocumentSerializationException;

public final class MongoDocumentHandler {

    private final MongoDatabase database;

    public MongoDocumentHandler(MongoDatabase database) {
        this.database = database;
    }

    public FindIterable<Document> tryFind(QueryDocument document) throws DocumentSerializationException, CollectionNotFoundException {
        checkIfCanQueryDocument(document);

        final String collectionName = getCollectionName(document);
        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if(collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        final Document doc = document.getDocument();

        return collection.find(doc);
    }

    /**
     * Tries to write a document to the database.
     * @param document document
     * @return this
     * @throws DocumentSerializationException If the document is not annotated correctly.
     * @throws CollectionNotFoundException If the collection name was not found in the database.
     */
    public MongoDocumentHandler tryInsert(InsertDocument document) throws DocumentSerializationException, CollectionNotFoundException {
        checkIfCanInsertDocument(document);

        final String collectionName = getCollectionName(document);
        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if(collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        final Document doc = document.getDocument();
        collection.insertOne(doc);

        return this;
    }

    private void checkIfCanQueryDocument(QueryDocument document) throws DocumentSerializationException {
        final boolean isNull = checkIfNull(document);
        if(isNull) {
            throw new DocumentSerializationException("The object to serialize is null");
        }

        final Validity validDoc = checkIfSerializable(document);
        if(!validDoc.valid) {
            throw new DocumentSerializationException(validDoc.message);
        }
    }

    private void checkIfCanInsertDocument(InsertDocument document) throws DocumentSerializationException {
        final boolean isNull = checkIfNull(document);
        if(isNull) {
            throw new DocumentSerializationException("The object to serialize is null");
        }

        final Validity validDoc = checkIfSerializable(document);
        if(!validDoc.valid) {
            throw new DocumentSerializationException(validDoc.message);
        }

        final Validity validKeys = checkIfHasKeys(document);
        if(!validKeys.valid) {
            throw new DocumentSerializationException(validKeys.message);
        }
    }

    private boolean checkIfNull(BaseDocument document) {
        return Objects.isNull(document);
    }

    private Validity checkIfSerializable(BaseDocument document) throws DocumentSerializationException {
        final Validity validity = new Validity();
        final Class<?> dClass = document.getClass();

        if (!dClass.isAnnotationPresent(DocumentSerializable.class)) {
            validity.valid = false;
            validity.message = String.format("The class %s is not annotated with DocumentSerializable.",
                                               dClass.getSimpleName());
        }

        return validity;
    }

    private Validity checkIfHasKeys(InsertDocument document) throws DocumentSerializationException {
        final Validity validity = new Validity();
        final Class<? extends InsertDocumentEntry> entryClass = document.getEntryClass();

        if (!entryClass.isAnnotationPresent(DocumentEntryKeys.class)) {
            validity.valid = false;
            validity.message = String.format("The class %s is not annotated with DocumentEntryKeys.",
                                             entryClass.getSimpleName());
        }

        return validity;
    }

    /**
     * Gets the name of the MongoDb Collection that the document belongs to
     * @param document document
     * @return collectionName
     */
    private String getCollectionName(BaseDocument document) {
        final Class<?> dClass = document.getClass();
        final DocumentSerializable annotation = dClass.getAnnotation(DocumentSerializable.class);

        return annotation.collectionName();
    }

    private final class Validity {
        private boolean valid;
        private String message;

        private Validity() {
            this.valid = true;
            this.message = "";
        }
    }
}
