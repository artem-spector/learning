package com.artem.learning.server.couchdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO: Document!
 *
 * @author artem on 2/28/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectionInfo {

    @JsonProperty("couchdb")
    private String welcomePhrase;

    @JsonProperty("uuid")
    private String id;

    @JsonProperty("version")
    private String version;

    public String getWelcomePhrase() {
        return welcomePhrase;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }
}
