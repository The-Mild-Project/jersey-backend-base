package db.mongo.util;

import java.util.Objects;

import org.bson.Document;

import annotations.mongo.documents.DocumentSerializable;
import db.mongo.documents.util.BaseDocument;

public class DocumentWriter {

    private Document write(BaseDocument document) {
        try {
            checkIfSerializable(document);
        } catch(DocumentSerializationException e) {
            System.out.println(e.getMessage());
        }
        return document.getDocument();
    }

    private void checkIfSerializable(BaseDocument document) throws DocumentSerializationException {
        if (Objects.isNull(document)) {
            throw new DocumentSerializationException("The object to serialize is null");
        }

        Class<?> classO = document.getClass();
        if (!classO.isAnnotationPresent(DocumentSerializable.class)) {
            throw new DocumentSerializationException(String.format("The class %s is not annotated with DocumentSerializable.",
                                                                   classO.getSimpleName()));
        }
    }
}
