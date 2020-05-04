package com.the.mild.project.db.mongo;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.UpdateOptions;
import com.the.mild.project.db.mongo.annotations.DocumentEntryKeys;
import com.the.mild.project.db.mongo.annotations.DocumentSerializable;
import com.the.mild.project.db.mongo.exceptions.CollectionNotFoundException;
import com.the.mild.project.db.mongo.exceptions.DocumentSerializationException;

public final class MongoDocumentHandler {

    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final UpdateOptions updateOptions = new UpdateOptions();
    private static final FindOneAndUpdateOptions findOneAndUpdateOptions = new FindOneAndUpdateOptions();

    static {
        updateOptions.upsert(true);
        findOneAndUpdateOptions.upsert(true);
    }

    private final MongoDatabase database;

    public MongoDocumentHandler(MongoDatabase database) {
        this.database = database;
    }

    public FindIterable<Document> tryFindAll(QueryDocument document) throws DocumentSerializationException, CollectionNotFoundException {
        log.info(document.toString());
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
     * Finds a record in a given collection by its ID field.
     *
     * @param collectionName
     * @param id
     * @return
     * @throws CollectionNotFoundException
     */
    public Document tryFindById(String collectionName, String id) throws CollectionNotFoundException {
        log.info(String.format("%s : %s", collectionName, id));

        final Document query = new Document();
        query.put("_id", id);

        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if(collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        final FindIterable<Document> documents = collection.find(query);
        final Document next = documents.iterator()
                                       .next();
        return next;
    }

    public Document tryFindOne(String collectionName, DocumentEntry<?>... entries) throws CollectionNotFoundException {
        log.info(collectionName);
        final Document query = new Document();
        for(DocumentEntry<?> entry : entries) {
            query.put(entry.getKey(), entry.getValue());
        }

        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if(collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        final FindIterable<Document> documents = collection.find(query);
        final Document next = documents.iterator()
                                       .next();
        return next;
    }

    public void tryUpdateOne(String collectionName, Document original, Document update)
            throws CollectionNotFoundException {
        log.info(String.format("%s : %s, %s", collectionName, original.toString(), update.toString()));
        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if(collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        collection.updateOne(original, new Document(MongoModifiers.SET.getModifier(), update));
    }

    public void tryUpdateOne(String collectionName, Document original, DocumentEntry<?>... entries) throws CollectionNotFoundException {
        log.info(String.format("%s : %s", collectionName, original.toString()));
        final Document update = new Document();
        final Document set = new Document();
        for(DocumentEntry<?> entry : entries) {
            set.put(entry.getKey(), entry.getValue());
        }
        update.put(MongoModifiers.SET.getModifier(), set);

        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if(collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        collection.updateOne(original, update, updateOptions);
    }

    public void tryUpdateOneById(String collectionName, String id, DocumentEntry<?>... entries) throws CollectionNotFoundException {
        log.info(String.format("%s : %s", collectionName, id));
        final ObjectId objectId = new ObjectId(id);
        final Document update = new Document();
        final Document set = new Document();
        for(DocumentEntry<?> entry : entries) {
            set.put(entry.getKey(), entry.getValue());
        }
        update.put(MongoModifiers.SET.getModifier(), set);

        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if(collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        final FindIterable<Document> documents = collection.find(new Document("_id", objectId));
        final MongoCursor<Document> iterator = documents.iterator();

        if(iterator.hasNext()) {
            collection.updateOne(iterator.next(), update, updateOptions);
        }
    }

    public void tryUpdateOneById(String collectionName, String id, InsertDocument update) throws CollectionNotFoundException {
        log.info(String.format("%s : %s",collectionName, id));
        final MongoCollection<Document> collection = database.getCollection(collectionName);
        final ObjectId objectId = new ObjectId(id);

        if(collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        final Document set = new Document(MongoModifiers.SET.getModifier(), update.getDocument());
        final Document d = collection.findOneAndUpdate(new Document("_id", objectId), set, findOneAndUpdateOptions);
        log.info(String.format("d=%s\n", d));
    }

    public void tryUpdateOneById(String collectionName, String id, Document update) throws CollectionNotFoundException {
        log.info(String.format("%s : %s, %s", collectionName, id, update.toString()));
        final MongoCollection<Document> collection = database.getCollection(collectionName);
        final ObjectId objectId = new ObjectId(id);

        if(collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        final Document set = new Document(MongoModifiers.SET.getModifier(), update);
        final Document d = collection.findOneAndUpdate(new Document("_id", objectId), set, findOneAndUpdateOptions);
        log.info(String.format("d=%s\n", d));
    }

    /**
     * Tries to write a document to the database.
     *
     * @param document document
     * @return this
     * @throws DocumentSerializationException If the document is not annotated correctly.
     * @throws CollectionNotFoundException If the collection name was not found in the database.
     */
    public MongoDocumentHandler tryInsert(InsertDocument document) throws DocumentSerializationException, CollectionNotFoundException {
        log.info(document.toString());
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

    /**
     * Inserts a new document into a given collection.
     *
     * @param collectionName
     * @param document
     * @return
     * @throws DocumentSerializationException
     * @throws CollectionNotFoundException
     */
    public MongoDocumentHandler tryInsert(String collectionName, InsertDocument document) throws DocumentSerializationException, CollectionNotFoundException {
        log.info(String.format("%s : %s", collectionName, document.toString()));
        checkIfCanInsertDocument(document);

        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if(collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        final Document doc = document.getDocument();
        collection.insertOne(doc);

        return this;
    }

    /**
     * Insert a new document into a given collection.
     *
     * @param collectionName
     * @param document
     * @return
     * @throws DocumentSerializationException
     * @throws CollectionNotFoundException
     */
    public MongoDocumentHandler tryInsert(String collectionName, Document document) throws DocumentSerializationException, CollectionNotFoundException {
        log.info(String.format("%s : %s", collectionName, document.toString()));

        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if(collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        FindIterable docs = collection.find(document);
        if (docs.first() == null) {
            collection.insertOne(document);
        }

        return this;
    }

    /**
     * Insert a document, or replace one if it already exists.
     *
     * @param collectionName
     * @param idDoc
     * @param newDoc
     * @return
     * @throws CollectionNotFoundException
     */
    public boolean insertOrUpdate(String collectionName, Document idDoc, Document newDoc)
            throws CollectionNotFoundException {
        log.info(String.format("%s : %s, %s", collectionName, idDoc, newDoc));

        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if(collection == null) {
            throw new CollectionNotFoundException(
                    String.format("Collection %s was not found in the database.", collectionName)
            );
        }

        FindIterable docs = collection.find(idDoc);
        if (docs.first() == null) {
            collection.insertOne(newDoc);
            return true;
        } else {
            UpdateResult result = collection.replaceOne(idDoc, newDoc);
            log.info(String.valueOf(result.wasAcknowledged()));
            return result.wasAcknowledged();
        }
    }

    /**
     * Get all docs in a collection, used for admin panel.
     *
     * @param collectionName
     * @return
     * @throws CollectionNotFoundException
     */
    public JsonArray getAllDocs(String collectionName) throws CollectionNotFoundException {
        log.info(collectionName);
        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if (collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        JsonParser parser = new JsonParser();
        JsonArray allUsers = new JsonArray();

        for (Document doc: collection.find()) {
            String docId = (String) doc.get("_id");
            doc.put(("id"), docId);
            doc.remove("_id");
            String docString = doc.toJson();
            JsonObject user = parser.parse(docString).getAsJsonObject();
            allUsers.add(user);
        }
        return allUsers;
    }

    /**
     * Returns a document associated with a specific ID.
     *
     * @param collectionName
     * @param id
     * @return
     * @throws CollectionNotFoundException
     */
    public JsonElement getDocById(String collectionName, String id)
            throws CollectionNotFoundException, DocumentSerializationException {
        log.info(String.format("%s : %s", collectionName, id));
        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if (collection == null) {
            throw new CollectionNotFoundException(
                    String.format("Collection %s was not found in the database.", collectionName)
            );
        }

        JsonParser parser = new JsonParser();

        Document findDoc = new Document();
        findDoc.put("_id", id);

        FindIterable docs = collection.find(findDoc);
        Document foundDoc = (Document) docs.first();
        if (foundDoc == null) {
            throw new DocumentSerializationException(
                    String.format("ID does not exist in collection %s", collection)
            );
        }

        foundDoc.put("id", id);
        foundDoc.remove("_id");

        String docString = foundDoc.toJson();
        JsonObject results = parser.parse(docString).getAsJsonObject();

        return results;
    }

    /**
     * Deletes a given document from a specified collection if it exists.
     *
     * @param collectionName
     * @param document
     * @return deleted document or null if no document was found
     * @throws CollectionNotFoundException
     */
    public Document tryDelete(String collectionName, Document document) throws CollectionNotFoundException, DocumentSerializationException {
        log.info(String.format("%s : %s", collectionName, document.toString()));
        final MongoCollection<Document> collection = database.getCollection(collectionName);

        if (collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        Document session = collection.find(document).first();
        if (session == null) {
            throw new DocumentSerializationException("Document does not exist");
        }
        return collection.findOneAndDelete(session);
    }

    /**
     * Deletes a given record based on their ID in a given collection.
     *
     * @param collectionName
     * @param id
     * @return
     * @throws CollectionNotFoundException
     * @throws DocumentSerializationException
     */
    public Document tryDelete(String collectionName, String id) throws CollectionNotFoundException, DocumentSerializationException {
        log.info(String.format("%s : %s", collectionName, id));
        final MongoCollection<Document> collection = database.getCollection(collectionName);

        Document document = new Document();
        document.put("_id", id);

        if (collection == null) {
            throw new CollectionNotFoundException(String.format("Collection %s was not found in the database.", collectionName));
        }

        Document session = collection.find(document).first();
        if (session == null) {
            throw new DocumentSerializationException("Document does not exist");
        }
        return collection.findOneAndDelete(session);
    }

    private void checkIfCanQueryDocument(QueryDocument document) throws DocumentSerializationException {
        log.info(document.toString());
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
        log.info(document.toString());
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
        log.info(document.toString());
        return Objects.isNull(document);
    }

    private Validity checkIfSerializable(BaseDocument document) throws DocumentSerializationException {
        log.info(document.toString());
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
        log.info(document.toString());
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
        log.info(document.toString());
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
