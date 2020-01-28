import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.DB;
import com.mongodb.client.MongoDatabase;

public class Tester {

    // private static final DB db;

    public static void main(String[] args) {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectWriter prettyMapper = new ObjectMapper().writerWithDefaultPrettyPrinter();

        final User user = new User();
        user.name = "Bob";
        user.email = "bob@bobby.bob";
        user.password = "password";

        try {
            final String result = mapper.writeValueAsString(user);
            System.out.println(result);
            System.out.println(prettyMapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static final class User {
        @JsonProperty("username") private String name;
        @JsonProperty("email") private String email;
        @JsonIgnore private String password;
    }
}
