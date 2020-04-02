package com.the.mild.project.server.resources;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;

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

    @GET
    @Path(PATH_CREATE)
    public Response createUser(@Context HttpHeaders header) {

        String googleId = header.getHeaderString(GOOGLE_ID);

        try {
            GoogleIdToken.Payload payload = UserAuth.checkAuth(googleId);
            Document userDoc = new Document();
            userDoc.put(MONGO_ID_FIELD, payload.getEmail());
            userDoc.put(FIRST_NAME, payload.get(GIVEN_NAME));
            userDoc.put(LAST_NAME, payload.get(FAMILY_NAME));

            mongoHandlerDevelopTest.tryInsert(USER_COLLECTION, userDoc);
        } catch (GeneralSecurityException | CollectionNotFoundException | DocumentSerializationException | IOException e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.NOT_FOUND)
//                    .header("Access-Control-Allow-Origin", "http://localhost:3000")
//                    .header("Access-Control-Allow-Credentials", "true")
//                    .header("Access-Control-Allow-Headers",
//                            "origin, content-type, accept, authorization")
//                    .header("Access-Control-Allow-Methods",
//                            "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .build();
        }

        return Response
                .status(Response.Status.OK)
//                .header("Access-Control-Allow-Origin", "http://localhost:3000")
//                .header("Access-Control-Allow-Credentials", "true")
//                .header("Access-Control-Allow-Headers",
//                        "origin, content-type, accept, authorization")
//                .header("Access-Control-Allow-Methods",
//                        "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }

    @GET
    @Path(PATH_LOGIN)
    public Response loginUser(@Context HttpHeaders header) {
        String googleId = header.getHeaderString(GOOGLE_ID);

        try {
            GoogleIdToken.Payload payload = UserAuth.checkAuth(googleId);
            Document sessionDoc = new Document();
            sessionDoc.put(MONGO_ID_FIELD, payload.getSubject());
            sessionDoc.put(EMAIL, payload.getEmail());
            sessionDoc.put(EXPIRATION_DATE, payload.getExpirationTimeSeconds());

            mongoHandlerDevelopTest.tryInsert(SESSION_COLLECTION, sessionDoc);
        } catch (GeneralSecurityException | CollectionNotFoundException | DocumentSerializationException | IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path(PATH_LOGOUT)
    public Response logoutUser(@Context HttpHeaders headers) {
        String googleId = headers.getHeaderString(GOOGLE_ID);
        try {
//            TODO: NEED TO FIX THIS FOR TESTING, IDS ARE RANDOM RIGHT NOW
            GoogleIdToken.Payload payload = UserAuth.checkAuth(googleId);

            // payload is null then the googleId could not be verified, return 401
            if (payload == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            Document document = new Document();
            document.put(MONGO_ID_FIELD, googleId);
            Document result = mongoHandlerDevelopTest.tryDelete(SESSION_COLLECTION, document);
        } catch (GeneralSecurityException | CollectionNotFoundException | DocumentSerializationException | IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).build();
    }
}
