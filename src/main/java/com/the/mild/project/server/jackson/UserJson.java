package com.the.mild.project.server.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserJson {
    @JsonProperty("googleId") private String googleId;
    @JsonProperty("email") private String email;
    @JsonProperty("firstName") private String firstName;
    @JsonProperty("lastName") private String lastName;
    @JsonProperty("expirationTime") private int expirationTime;

    @JsonCreator
    public UserJson(@JsonProperty("email") String email,
                    @JsonProperty("firstName") String firstName,
                    @JsonProperty("lastName") String lastName)
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @JsonCreator
    public UserJson(@JsonProperty("googleId") String googleId,
                    @JsonProperty("email") String email,
                    @JsonProperty("firstName") String firstName,
                    @JsonProperty("lastName") String lastName,
                    @JsonProperty("expirationTime") int expirationTime)
    {
        this.googleId = googleId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expirationTime = expirationTime;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGoogleId(String id) {
        this.googleId = id;
    }

    public String getEmail() {
        return email;
    }

    public String getGoogleId() {
        return googleId;
    }

    @Override
    public String toString() {
        return JacksonHandler.stringify(this);
    }
}
