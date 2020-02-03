package db.mongo.documents;

import org.bson.Document;

import db.mongo.documents.util.BaseDocumentEntry;

public interface BaseDocument {
    /**
     * Put data into a document.
     * @param enumType;
     * @param data;
     * @param <E>;
     */
    <E extends BaseDocumentEntry> void putData(Class<E> enumType, Object... data);

    /**
     * Get back a document
     * @return document
     */
    Document getDocument();
}
