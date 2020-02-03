package db.mongo.documents;

import static db.mongo.documents.util.TestDocumentEntry.AUTHOR_ID;
import static db.mongo.documents.util.TestDocumentEntry.CHANNEL_ID;
import static db.mongo.documents.util.TestDocumentEntry.REMINDER_MESSAGE;
import static db.mongo.documents.util.TestDocumentEntry.TIME;
import static db.mongo.documents.util.TestDocumentEntry.TIME_UNIT;

import java.util.Arrays;

import org.bson.Document;

import db.mongo.documents.util.BaseDocumentEntry;
import db.mongo.documents.util.TestDocumentEntry;

public class TestDocument extends BaseDocument {

    public TestDocument() {
        this.document = new Document();
    }

    /**
     * Put all required data into the document.
     * @param authorId
     * @param channelId
     * @param time
     * @param timeUnit
     * @param reminderMessage
     * @return TestDocument
     */
    public TestDocument putAll(long authorId, long channelId, long time, String timeUnit, String reminderMessage) {
        putData(TestDocumentEntry.class, authorId, channelId, time, timeUnit, reminderMessage);
        return this;
    }

    /**
     * Puts all necessary data into the document constrained by the DocumentEntry
     * @param documentEntries
     * @param data;
     * @param <E>
     */
    @Override
    protected <E extends BaseDocumentEntry> void putData(Class <E> documentEntries, Object... data) {
        final E[] values = documentEntries.getEnumConstants();
        for(int i = 0; i < values.length; i++) {
            document.put(values[i].key(), data[i]);
        }
    }

    /**
     * Returns the document.
     * @return document
     */
    @Override
    public Document getDocument() {
        return document;
    }
}
