package com.the.mild.project.server.resources;

import static com.the.mild.project.ResourceConfig.PATH_TODO_RESOURCE;
import static com.the.mild.project.server.Main.MONGO_DB_FACTORY;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.the.mild.project.MongoCollections;
import com.the.mild.project.MongoDatabaseType;
import com.the.mild.project.db.mongo.MongoDatabaseFactory;
import com.the.mild.project.db.mongo.documents.TodoDocument;
import com.the.mild.project.server.jackson.JacksonHandler;
import com.the.mild.project.server.jackson.JacksonTest;
import com.the.mild.project.server.jackson.TodoJson;

/**
 * Root resource
 */
@Singleton
@Path(PATH_TODO_RESOURCE)
public class Todo {
    private static final MongoDatabaseFactory mongoFactory;

    static {
        mongoFactory = MONGO_DB_FACTORY.orElse(null);

        assert mongoFactory != null;
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void putTodo(String todoBody) {
        System.out.printf("todo = %s\n", todoBody);

        try {
            TodoJson todo = JacksonHandler.unmarshal(todoBody, TodoJson.class);
            final Document document = new TodoDocument(todo).getDocument();

            final MongoDatabase database = mongoFactory.getDatabase(MongoDatabaseType.DEVELOP_TEST);
            final MongoCollection<Document> collection = database.getCollection(MongoCollections.TODO.name());

            collection.insertOne(document);

        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
