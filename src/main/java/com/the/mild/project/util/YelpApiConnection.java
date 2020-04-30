package com.the.mild.project.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class YelpApiConnection {

    private static final String clientId = System.getenv("YELP_CLIENT_ID");
    private static final String apiKey = System.getenv("YELP_API_KEY");
    private static final String defaultLocation = "94122";
    private static final String defaultCategory = "food";
    private static final String yelpUri = "https://api.yelp.com/v3";

    /**
     * Gets a list of restaurants from the Yelp API in a specified area code and restaurant type.
     * Default is to return 50 results.
     *
     * @param location
     * @param rests
     * @return
     * @throws BadRequestException
     */
    public static JsonElement businessSearch(String location, String rests) throws BadRequestException {
        String uri = String.format("%s/businesses/search?location=%s&categories=%s", yelpUri, location, rests);
        String results = YelpApiConnection.yelpApiRequest(uri);
        JsonObject json = new JsonParser().parse(results).getAsJsonObject();
        return json.get("businesses");
    }

    /**
     * Gets a list of restaurants from the Yelp API in a specified area code.
     * Default is to return 50 results.
     *
     * @param location
     * @return
     * @throws BadRequestException
     */
    public static JsonElement businessSearch(String location) throws BadRequestException {
        String uri = String.format("%s/businesses/search?location=%s&categories=%s", yelpUri, location, defaultCategory);
        String results = YelpApiConnection.yelpApiRequest(uri);
        JsonObject json = new JsonParser().parse(results).getAsJsonObject();
        return json.get("businesses");
    }

    /**
     * Gets a list of restaurants from the Yelp API in the default area code.
     * Default is to return 50 results.
     *
     * @return
     * @throws BadRequestException
     */
    public static JsonElement businessSearch() throws BadRequestException {
        String uri = String.format("%s/businesses/search?location=%s&categories=%s", yelpUri, defaultLocation, defaultCategory);
        String results = YelpApiConnection.yelpApiRequest(uri);
        JsonObject json = new JsonParser().parse(results).getAsJsonObject();
        return json.get("businesses");
    }

    /**
     * Gets results for a specific business from the Yelp API.
     * If the reviews flag is true then the reviews for that business will be displayed.
     *
     * @param businessId
     * @param reviews
     * @return
     * @throws BadRequestException
     */
    public static String businessSearchWithReviews(String businessId, Boolean reviews) throws BadRequestException {
        String uri = String.format("%s/businesses/%s", yelpUri, businessId);
        if (reviews) {
            uri = uri + "/reviews";
        }

        return YelpApiConnection.yelpApiRequest(uri);
    }

    /**
     * Establishes a connection to the Yelp API and uses the given URI to hit specific API endpoints.
     *
     * @param uri
     * @return
     * @throws BadRequestException
     */
    private static String yelpApiRequest(String uri) throws BadRequestException {
        Client client = ClientBuilder.newClient();
        try {
            WebTarget resource = client.target(String.format(uri));
            return resource.request(MediaType.APPLICATION_JSON).header("Authorization", String.format("Bearer %s", apiKey)).get(String.class);
        } catch (BadRequestException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        } finally {
            client.close();
        }
    }
}
