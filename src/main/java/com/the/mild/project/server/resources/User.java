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

import static com.the.mild.project.ResourceConfig.*;
import static com.the.mild.project.server.Main.MONGO_DB_FACTORY;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
@Path(PATH_USER_RESOURCE)
public class User {

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
    @Path(PATH_LOGIN)
    public Response createAndLogin(@Context HttpHeaders headers) {

        String googleId = headers.getHeaderString(GOOGLE_ID);
        log.info(googleId.substring(0,10));

        for (Map.Entry<String, List<String>> entry: headers.getRequestHeaders().entrySet()) {
            System.out.println(String.format("%s : %s", entry.getKey(), entry.getValue()));
        }


        try {
            GoogleIdToken.Payload payload = UserAuth.checkAuth(googleId);
            String userEmail = payload.getEmail();
            String firstName = payload.get(GIVEN_NAME).toString();
            String lastName = payload.get(FAMILY_NAME).toString();

            Document exists = mongoHandlerDevelopTest.tryFindById(USER_COLLECTION, userEmail);
            if (exists == null) {
                // Collect user doc
                Document userDoc = new Document();
                userDoc.put(MONGO_ID_FIELD, userEmail);
                userDoc.put(FIRST_NAME, firstName);
                userDoc.put(LAST_NAME, lastName);
                userDoc.put(ADMIN, false);

                mongoHandlerDevelopTest.tryInsert(USER_COLLECTION, userDoc);
            }

            // Collect session doc
            Document sessionDoc = new Document();
            sessionDoc.put(MONGO_ID_FIELD, googleId);
            sessionDoc.put(EMAIL, payload.getEmail());
            sessionDoc.put(EXPIRATION_DATE, payload.getExpirationTimeSeconds());

            mongoHandlerDevelopTest.tryInsert(SESSION_COLLECTION, sessionDoc);
        } catch (Exception e) {
            log.warning(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path(PATH_LOGOUT)
    public Response logoutUser(@Context HttpHeaders headers) {
        String googleId = headers.getHeaderString(GOOGLE_ID);
        log.info(googleId.substring(0,10));

        try {
//            TODO: NEED TO FIX THIS FOR TESTING, IDS ARE RANDOM RIGHT NOW
            GoogleIdToken.Payload payload = UserAuth.checkAuth(googleId);
            System.out.println(payload);
            // payload is null then the googleId could not be verified, return 401
            if (payload == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            Document document = new Document();
            document.put(MONGO_ID_FIELD, googleId);
            Document result = mongoHandlerDevelopTest.tryDelete(SESSION_COLLECTION, document);
        } catch (GeneralSecurityException | CollectionNotFoundException | DocumentSerializationException | IOException e) {
            log.warning(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path(PATH_GET_ALL)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers(@Context HttpHeaders headers) {
        String googleId = headers.getHeaderString(GOOGLE_ID);
        log.info(googleId.substring(0,10));
        try {
            GoogleIdToken.Payload payload = UserAuth.checkAuth(googleId);
            if (payload == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            String email = payload.getEmail();
            if (!mongoHandlerDevelopTest.checkForAdmin(email)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            JsonElement results = mongoHandlerDevelopTest.getAllDocs(USER_COLLECTION);
            return Response
                    .ok(results.toString(), MediaType.APPLICATION_JSON)
                    .header("X-Total-Count", String.format("%d", results.getAsJsonArray().size()))
                    .build();
        } catch (GeneralSecurityException | CollectionNotFoundException | IOException e) {
            log.warning(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path(PATH_DELETE_USER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@Context HttpHeaders headers, @PathParam("username") String userName) {
        String googleId = headers.getHeaderString(GOOGLE_ID);
        log.info(googleId.substring(0,10));

        Document results;
        Document res = new Document();

        try {
            GoogleIdToken.Payload payload = UserAuth.checkAuth(googleId);
            if (payload == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            String email = payload.getEmail();
            if (!mongoHandlerDevelopTest.checkForAdmin(email)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            results = mongoHandlerDevelopTest.tryDelete(USER_COLLECTION, userName);
            res.put("id", results.getString("_id"));

            return Response.ok(res).build();
        } catch (GeneralSecurityException | CollectionNotFoundException |
                DocumentSerializationException | IOException e) {
            log.warning(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
