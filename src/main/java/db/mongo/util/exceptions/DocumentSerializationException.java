package db.mongo.util.exceptions;

public class DocumentSerializationException extends Exception {
    public DocumentSerializationException(String errorMessage) {
        super(errorMessage);
    }
}