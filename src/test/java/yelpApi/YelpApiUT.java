package yelpApi;

import com.the.mild.project.util.YelpApiConnection;
import org.junit.Test;

public class YelpApiUT {

    @Test
    public void businessSearchTest() {

        System.out.println(YelpApiConnection.businessSearch());
        assert true;
    }
}
