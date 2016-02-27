package com.artem.learning.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 2/27/16
 */
public class UniqueEntity {

    @JsonProperty("_id")
    private String id;

    public String getId() {
        return id;
    }
}
