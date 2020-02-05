package db.mongo.documents;

import static db.mongo.util.MongoCollections.TEST_NAME;

import java.util.concurrent.TimeUnit;

import org.bson.Document;

import annotations.mongo.documents.DocumentEntry;
import annotations.mongo.documents.DocumentEntryKeys;
import annotations.mongo.documents.DocumentSerializable;
import db.mongo.documents.util.BaseDocument;
import db.mongo.documents.util.BaseDocumentEntry;

@DocumentSerializable(collectionName = TEST_NAME)
public class TestDocument extends BaseDocument {

    public TestDocument() {
        this.document = new Document();
    }

    /**
     * Put all required data into the document.
     * @param authorId;
     * @param channelId;
     * @param time;
     * @param timeUnit;
     * @param reminderMessage;
     * @return TestDocument
     */
    public TestDocument putAll(long authorId, long channelId, long time, String timeUnit, String reminderMessage) {
        putData(getEntryClass(), authorId, channelId, time, timeUnit, reminderMessage);
        return this;
    }

    /**
     * Returns the document.
     * @return document
     */
    @Override
    public Document getDocument() {
        return document;
    }

    @Override
    protected Class<Entry> getEntryClass() {
        return Entry.class;
    }

    @DocumentEntryKeys
    private enum Entry implements BaseDocumentEntry {
        @DocumentEntry(getClassType = long.class) AUTHOR_ID("authorId"),
        @DocumentEntry(getClassType = long.class) CHANNEL_ID("channelId"),
        @DocumentEntry(getClassType = long.class) TIME("time"),
        @DocumentEntry(getClassType = TimeUnit.class) TIME_UNIT("unit"),
        @DocumentEntry(getClassType = String.class) REMINDER_MESSAGE("reminderMessage");
        // @DocumentEntry(getClassType = long.class) AUTHOR_ID("authorId", long.class),
        // @DocumentEntry(getClassType = long.class) CHANNEL_ID("channelId", long.class),
        // @DocumentEntry(getClassType = long.class) TIME("time", long.class),
        // @DocumentEntry(getClassType = TimeUnit.class) TIME_UNIT("unit", TimeUnit.class),
        // @DocumentEntry(getClassType = String.class) REMINDER_MESSAGE("reminderMessage", String.class);

        final private String key;
        // final private Class classType;

        Entry(String key) {
            this.key = key;
        }

        // Entry(String key, Class classType) {
        //     this.key = key;
        //     this.classType = classType;
        // }

        @Override
        public String key() {
            return key;
        }

        // @Override
        // public Class classType() {
        //     return classType;
        // }
    }
}
