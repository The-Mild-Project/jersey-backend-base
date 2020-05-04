package com.the.mild.project.server.resources;

import com.google.gson.JsonElement;
import com.the.mild.project.util.YelpApiConnection;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.logging.Logger;

import static com.the.mild.project.ResourceConfig.*;

@Singleton
@Path(PATH_RESTAURANT_RESOURCE)
public class Restaurant {

    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @GET
    @Path(PATH_ALL_RESTAURANTS)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurants(@QueryParam("zip") String zip, @QueryParam("type") String type) {
        log.info(String.format("%s : %s", zip, type));
        try {
            JsonElement json;
            if (type != null) {
                json = YelpApiConnection.businessSearch(zip, type);
            } else if (zip != null) {
                json = YelpApiConnection.businessSearch(zip);
            } else {
                json = YelpApiConnection.businessSearch();
            }

            int numElements = json.getAsJsonArray().size();
            return Response
                    .ok(json.toString(), MediaType.APPLICATION_JSON)
                    .header("X-Total-Count", String.format("%d", numElements))
                    .build();
        } catch (BadRequestException e) {
            log.warning(e.getMessage());
            return Response.status(400).build();
        }
    }

    @GET
    @Path(PATH_TO_RESTAURANT)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurant(@PathParam("id") String id) {
        log.info(id);
        try {
            String rest = YelpApiConnection.businessSearchWithReviews(id, false);
            return Response.ok(rest).build();
        } catch (BadRequestException e) {
            log.warning(e.getMessage());
            return Response.status(400).build();
        }
    }

    @GET
    @Path(PATH_TO_REVIEWS)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurantReviews(@PathParam("id") String id) {
        log.info(id);
        try {
            String rest = YelpApiConnection.businessSearchWithReviews(id, true);
            return Response.ok(rest).build();
        } catch (BadRequestException e) {
            log.info(e.getMessage());
            return Response.status(400).build();
        }
    }
}
