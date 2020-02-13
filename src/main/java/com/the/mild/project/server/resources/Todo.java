package com.the.mild.project.server.resources;

import static com.the.mild.project.ResourceConfig.PATH_TODO_RESOURCE;
import static com.the.mild.project.server.Main.MONGO_DB_FACTORY;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.the.mild.project.db.mongo.MongoDatabaseFactory;
import com.the.mild.project.server.jackson.JacksonHandler;
import com.the.mild.project.server.jackson.JacksonTest;

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
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIt() {
        final JacksonTest test = new JacksonTest("test", "resource");

        return JacksonHandler.stringify(test);
    }
}
