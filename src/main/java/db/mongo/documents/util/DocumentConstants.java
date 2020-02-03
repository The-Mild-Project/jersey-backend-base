package db.mongo.documents.util;

import java.util.concurrent.TimeUnit;

public class DocumentConstants {
    public enum Type {
        REMINDER();
    }
    
    public enum TestDocumentEntry {
        AUTHOR_ID("authorId", long.class),
        CHANNEL_ID("channelId", long.class),
        TIME("time", long.class),
        TIME_UNIT("unit", TimeUnit.class),
        REMINDER_MESSAGE("reminderMessage", String.class);
        
        TestDocumentEntry(String key, Class type) {
            // Utility
        }
    }
    
    private DocumentConstants() {
        // Utility
    }
}
