package com.the.mild.project.db.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Entries {
    private final List<DocumentEntry> entryList = new ArrayList<>();

    public Entries() {
    }

    public Entries addEntry(String key, Object value) {
        entryList.add(new DocumentEntry<>(key, value));
        return this;
    }

    public void forEach(Consumer<? super DocumentEntry> mutator) {
        entryList.forEach(mutator);
    }
}
