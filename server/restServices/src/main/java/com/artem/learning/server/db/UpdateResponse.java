package com.artem.learning.server.db;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO: Document!
 *
 * @author artem on 2/28/16.
 */
public class UpdateResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("ok")
    private boolean success;

    @JsonProperty("rev")
    private String revision;

    public String getId() {
        return id;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getRevision() {
        return revision;
    }
}
