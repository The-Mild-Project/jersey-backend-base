package com.the.mild.project.server.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.the.mild.project.util.YelpApiConnection;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.the.mild.project.ResourceConfig.*;

@Singleton
@Path(PATH_RESTAURANT_RESOURCE)
public class Restaurant {

    @GET
    @Path(PATH_ALL_RESTAURANTS)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurants() {
        try {
            JsonElement json = YelpApiConnection.businessSearch();
            int numElements = json.getAsJsonArray().size();
            return Response.ok(json.toString(), MediaType.APPLICATION_JSON).header("X-Total-Count", String.format("%d", numElements)).build();
        } catch (BadRequestException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    @GET
    @Path(PATH_TO_RESTAURANT)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurant(@PathParam("id") String id) {

        try {
            String rest = YelpApiConnection.businessSearchWithReviews(id, false);
            return Response.ok(rest).build();
        } catch (BadRequestException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    @GET
    @Path(PATH_TO_REVIEWS)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurantReviews(@PathParam("id") String id) {
        try {
            String rest = YelpApiConnection.businessSearchWithReviews(id, true);
            return Response.ok(rest).build();
        } catch (BadRequestException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }
}
