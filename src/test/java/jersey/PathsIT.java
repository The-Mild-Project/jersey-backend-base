package jersey;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.the.mild.project.server.Main;
import com.the.mild.project.server.jackson.JacksonTest;
import com.the.mild.project.server.resources.TestResource;

public class PathsIT {
    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        server = Main.startServer();

        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        final ObjectMapper mapper = new ObjectMapper();

        final JacksonTest test = new JacksonTest("test", "resource");

        String result = "{}";
        try {
            result = mapper.writeValueAsString(test);
            System.out.println(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String responseMsg = target.path("testresource").request().get(String.class);
        assertEquals("Got resource!", responseMsg);
    }
}
