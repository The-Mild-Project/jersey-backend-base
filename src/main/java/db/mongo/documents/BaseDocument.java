package db.mongo.documents;

import org.bson.Document;

import db.mongo.documents.util.BaseDocumentEntry;

public abstract class BaseDocument {
    protected Document document;

    /**
     * Put data into a document.
     * @param enumType;
     * @param data;
     * @param <E>;
     */
    protected abstract <E extends BaseDocumentEntry> void putData(Class<E> enumType, Object... data);

    /**
     * Get back a document
     * @return document
     */
    public abstract Document getDocument();
}
