package db.mongo.util.exceptions;

public class CollectionNotFoundException extends Exception {
    public CollectionNotFoundException(String message) {
        super(message);
    }
}
