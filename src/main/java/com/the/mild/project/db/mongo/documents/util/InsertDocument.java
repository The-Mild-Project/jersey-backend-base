package com.the.mild.project.db.mongo.documents.util;

import org.bson.Document;

public abstract class InsertDocument implements BaseDocument {
    protected Document document;

    protected InsertDocument() {
        this.document = new Document();
    }

    /**
     * Put data into a document.
     * @param documentEntries;
     * @param data;
     * @param <E>;
     */
    protected <E extends InsertDocumentEntry> void putData(Class<E> documentEntries, Object... data) {
        final E[] values = documentEntries.getEnumConstants();
        for(int i = 0; i < values.length; i++) {
            document.put(values[i].key(), data[i]);
        }
    }

    public abstract Class<? extends InsertDocumentEntry> getEntryClass();
}
