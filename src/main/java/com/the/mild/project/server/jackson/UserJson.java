package com.the.mild.project.server.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserJson {
    @JsonProperty("id") private String id;
    @JsonProperty("email") private String email;

    public UserJson() {

    }

    @JsonCreator
    public UserJson(@JsonProperty("id") String id,
                    @JsonProperty("email") String email) {
        this.id = id;
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return JacksonHandler.stringify(this);
    }
}
