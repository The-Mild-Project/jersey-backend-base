package com.the.mild.project.db.mongo.documents;

import com.the.mild.project.db.mongo.InsertDocument;
import com.the.mild.project.db.mongo.InsertDocumentEntry;
import com.the.mild.project.db.mongo.annotations.DocumentEntryKeys;
import com.the.mild.project.db.mongo.annotations.DocumentSerializable;
import com.the.mild.project.server.jackson.UserJson;
import org.bson.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.the.mild.project.MongoCollections.USER_NAME;

@DocumentSerializable(collectionName = USER_NAME)
public class UserDocument extends InsertDocument {

    public UserDocument() {
        super();
    }

    public UserDocument(UserJson user) {
        super();
        putData(getEntryClass(), user.getId(), user.getUsername(), user.getEmail());
    }

    @Override
    public Class<Entry> getEntryClass() {
        return Entry.class;
    }

    @Override
    public Document getDocument() {
        return null;
    }

    @DocumentEntryKeys
    public enum Entry implements InsertDocumentEntry {
        ID("id"),
        EMAIL("email");

        private static final Map<String, Entry> ENTRY_BY_NAME = new HashMap<>();

        static {
            Arrays.asList(UserDocument.Entry.values())
                    .forEach(e -> ENTRY_BY_NAME.put(e.key(), e));
        }

        final private String key;

        Entry(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }

        public static Entry getByKeyName(String key) {
            return ENTRY_BY_NAME.getOrDefault(key, null);
        }
    }
}
