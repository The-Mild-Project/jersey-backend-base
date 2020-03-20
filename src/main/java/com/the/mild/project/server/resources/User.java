package com.the.mild.project.server.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.the.mild.project.MongoDatabaseType;
import com.the.mild.project.db.mongo.DocumentEntry;
import com.the.mild.project.db.mongo.MongoDatabaseFactory;
import com.the.mild.project.db.mongo.MongoDocumentHandler;
import com.the.mild.project.db.mongo.documents.UserDocument;
import com.the.mild.project.db.mongo.exceptions.CollectionNotFoundException;
import com.the.mild.project.db.mongo.exceptions.DocumentSerializationException;
import com.the.mild.project.server.jackson.JacksonHandler;
import com.the.mild.project.server.jackson.UserJson;
import org.bson.Document;

import static com.the.mild.project.ResourceConfig.PATH_USER_RESOURCE;
import static com.the.mild.project.ResourceConfig.PATH_CREATE;
import static com.the.mild.project.ResourceConfig.PATH_LOGIN;
import static com.the.mild.project.server.Main.MONGO_DB_FACTORY;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path(PATH_USER_RESOURCE)
public class User {

    private static final MongoDatabaseFactory mongoFactory;
    private static final MongoDocumentHandler mongoHandlerDevelopTest;

    static {
        mongoFactory = MONGO_DB_FACTORY.orElse(null);

        assert mongoFactory != null;

        final MongoDatabase developTestDb = mongoFactory.getDatabase(MongoDatabaseType.DEVELOP_TEST);
        mongoHandlerDevelopTest = new MongoDocumentHandler(developTestDb);
    }

    @POST
    @Path(PATH_CREATE)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String userInfo) {
        try {
            UserJson user = JacksonHandler.unmarshal(userInfo, UserJson.class);
            final UserDocument document = new UserDocument(user);

            mongoHandlerDevelopTest.tryInsert(document);
        } catch(JsonProcessingException | DocumentSerializationException | CollectionNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }
        return Response.status(200).build();
    }

    @POST
    @Path(PATH_LOGIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUser(String userInfo) {
        try {
            UserJson user = JacksonHandler.unmarshal(userInfo, UserJson.class);
            final UserDocument document = new UserDocument(user);

            mongoHandlerDevelopTest.tryInsert("session", document);
        } catch (JsonProcessingException | CollectionNotFoundException | DocumentSerializationException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }
        return Response.status(200).build();
    }

    @POST
    @Path(PATH_LOGIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logoutUser(String userInfo) {
        try {
            UserJson user = JacksonHandler.unmarshal(userInfo, UserJson.class);
            DocumentEntry<String> documentEntry = new DocumentEntry<>("googleId", user.getGoogleId());
            Document document = mongoHandlerDevelopTest.tryFindOne("session", documentEntry);
            DeleteResult result = mongoHandlerDevelopTest.TryDelete("session", document);
        } catch (JsonProcessingException | CollectionNotFoundException e) {
            e.printStackTrace();
            return Response.status(404).build();
        }
        return Response.status(200).build();
    }
}
