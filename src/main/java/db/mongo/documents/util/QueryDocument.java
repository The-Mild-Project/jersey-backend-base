package db.mongo.documents.util;

import org.bson.Document;

public abstract class QueryDocument<K extends InsertDocumentEntry> implements BaseDocument {
    protected Document document;

    protected QueryDocument() {
        this.document = new Document();
    }

    protected void putQuery(Query query) {
        document.put(query.key.key(), query.value);
    }

    protected void putQueries(Query... queries) {
        for(final Query query : queries) {
            document.put(query.key.key(), query.value);
        }
    }

    public final class Query<V> {
        private final K key;
        private final V value;

        public Query(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K key() {
            return key;
        }

        public V value() {
            return value;
        }
    }

    public abstract static class Builder<T extends QueryDocument> {
        private final T queryDocument;

        protected Builder(T queryDocument) {
            this.queryDocument = queryDocument;
        }

        public T build() {
            return queryDocument;
        }
    }
}
