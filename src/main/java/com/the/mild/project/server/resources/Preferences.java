package com.the.mild.project.server.resources;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.gson.JsonElement;
import com.mongodb.client.MongoDatabase;
import com.the.mild.project.MongoDatabaseType;
import com.the.mild.project.db.mongo.MongoDatabaseFactory;
import com.the.mild.project.db.mongo.MongoDocumentHandler;
import com.the.mild.project.db.mongo.exceptions.CollectionNotFoundException;
import com.the.mild.project.db.mongo.exceptions.DocumentSerializationException;
import org.bson.Document;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.logging.Logger;

import static com.the.mild.project.ResourceConfig.*;
import static com.the.mild.project.server.Main.MONGO_DB_FACTORY;

@Singleton
@Path(PATH_PREFERENCES)
public class Preferences {

    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final MongoDatabaseFactory mongoFactory;
    private static final MongoDocumentHandler mongoHandlerDevelopTest;

    static {
        mongoFactory = MONGO_DB_FACTORY.orElse(null);

        assert mongoFactory != null;

        final MongoDatabase developTestDb = mongoFactory.getDatabase(MongoDatabaseType.DEVELOP_TEST);
        mongoHandlerDevelopTest = new MongoDocumentHandler(developTestDb);
    }

    @GET
    @Path(PATH_PREFERENCES_GET_ALL)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPreferences(@Context HttpHeaders headers) {
        String googleId = headers.getHeaderString(GOOGLE_ID);
        log.info(googleId.substring(0,10));

        try {
            GoogleIdToken.Payload payload = UserAuth.checkAuth(googleId);
            if (payload == null) {
                log.warning("Unable to login");
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            String email = payload.getEmail();
            if (!mongoHandlerDevelopTest.checkForAdmin(email)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            
            JsonElement results = mongoHandlerDevelopTest.getAllDocs(PREFERENCES_COLLECTION);
            return Response
                    .ok(results.toString(), MediaType.APPLICATION_JSON)
                    .header("X-Total-Count", String.format("%d", results.getAsJsonArray().size()))
                    .build();
        } catch (GeneralSecurityException | CollectionNotFoundException | IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path(PATH_PREFERENCES_GET_USER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPreferences(@Context HttpHeaders headers) {
        String googleId = headers.getHeaderString(GOOGLE_ID);
        log.info(googleId.substring(0,10));

        try {
            GoogleIdToken.Payload payload = UserAuth.checkAuth(googleId);
            System.out.println(payload);
            if (payload == null) {
                log.warning("Unable to login");
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            String email = payload.getEmail();
            JsonElement results = mongoHandlerDevelopTest.getDocById(PREFERENCES_COLLECTION, email);
            return Response
                    .ok(results.toString(), MediaType.APPLICATION_JSON)
                    .build();
        } catch (GeneralSecurityException | CollectionNotFoundException |
                DocumentSerializationException | IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path(PATH_PREFERENCES_SET)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setPreferences(@Context HttpHeaders headers, List preferencesList) {
        String googleId = headers.getHeaderString(GOOGLE_ID);
        log.info(googleId.substring(0,10));

        try {
            GoogleIdToken.Payload payload = UserAuth.checkAuth(googleId);
            System.out.println(payload);
            if (payload == null) {
                log.warning("Unable to login");
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            String userEmail = payload.getEmail();

            Document idDoc = new Document();
            idDoc.put("_id", userEmail);

            Document prefDoc = new Document();
            prefDoc.put("_id", userEmail);
            prefDoc.put("food", preferencesList);

            Boolean didUpdate = mongoHandlerDevelopTest.insertOrUpdate(PREFERENCES_COLLECTION, idDoc, prefDoc);
            System.out.println(didUpdate);
            log.info(String.format("Did update: %s", didUpdate));
            if (didUpdate) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (GeneralSecurityException | IOException | CollectionNotFoundException e) {
            log.warning(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
