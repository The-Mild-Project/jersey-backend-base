package db.mongo.documents.util;

import org.bson.Document;

import annotations.mongo.documents.DocumentSerializable;

@DocumentSerializable
public abstract class BaseDocument {
    protected Document document;

    /**
     * Put data into a document.
     * @param documentEntries;
     * @param data;
     * @param <E>;
     */
    protected <E extends BaseDocumentEntry> void putData(Class<E> documentEntries, Object... data) {
        final E[] values = documentEntries.getEnumConstants();
        for(int i = 0; i < values.length; i++) {
            document.put(values[i].key(), data[i]);
        }
    }

    /**
     * Get back a document
     * @return document
     */
    public abstract Document getDocument();

    protected abstract Class<? extends BaseDocumentEntry> getEntryClass();
}
