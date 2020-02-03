package db.mongo.documents.util;

import java.util.concurrent.TimeUnit;

public enum TestDocumentEntry implements BaseDocumentEntry {
    AUTHOR_ID("authorId", long.class),
    CHANNEL_ID("channelId", long.class),
    TIME("time", long.class),
    TIME_UNIT("unit", TimeUnit.class),
    REMINDER_MESSAGE("reminderMessage", String.class);

    final private String key;
    final private Class classType;

    TestDocumentEntry(String key, Class classType) {
        this.key = key;
        this.classType = classType;
    }

    public String key() {
        return key;
    }

    public Class classType() {
        return classType;
    }
}
