package jersey;

import static com.the.mild.project.server.ResourceConfig.PATH_TEST_RESOURCE;
import static com.the.mild.project.server.ResourceConfig.PATH_TEST_RESOURCE_WITH_MULTIPLE_PARAMS_FORMAT;
import static com.the.mild.project.server.ResourceConfig.PATH_TEST_RESOURCE_WITH_PARAM_FORMAT;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.the.mild.project.server.Main;
import com.the.mild.project.server.jackson.JacksonTest;
import com.the.mild.project.server.jackson.MultipleParamsTest;
import com.the.mild.project.server.jackson.ParamTest;
import com.the.mild.project.server.jackson.util.JacksonHandler;

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
    public void testPathTestResource() {
        final JacksonTest test = new JacksonTest("test", "resource");

        String expectedResult = JacksonHandler.stringify(test);

        String responseMsg = target.path(PATH_TEST_RESOURCE)
                                   .request()
                                   .get(String.class);

        System.out.println(expectedResult);
        assertEquals(expectedResult, responseMsg);
    }

    /**
     * Test path with single param
     */
    @Test
    public void testPathTestResourceWithParam() {
        final String id = "1";
        final ParamTest test = new ParamTest(id);

        String expectedResult = JacksonHandler.stringify(test);

        String responseMsg = target.path(String.format(PATH_TEST_RESOURCE_WITH_PARAM_FORMAT, id))
                                   .request()
                                   .get(String.class);

        System.out.println(expectedResult);
        assertEquals(expectedResult, responseMsg);
    }

    /**
     * Test path with multiple params
     */
    @Test
    public void testPathTestResourceWithMultipleParams() {
        final String id = "1";
        final String exampleId = "1";
        final MultipleParamsTest test = new MultipleParamsTest(id, exampleId);

        String expectedResult = JacksonHandler.stringify(test);

        String responseMsg = target.path(String.format(PATH_TEST_RESOURCE_WITH_MULTIPLE_PARAMS_FORMAT, id, exampleId))
                                   .request()
                                   .get(String.class);

        System.out.println(expectedResult);
        assertEquals(expectedResult, responseMsg);
    }
}
