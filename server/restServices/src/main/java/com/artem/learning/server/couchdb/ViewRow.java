package com.artem.learning.server.couchdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * TODO: Document!
 *
 * @author artem on 2/29/16.
 */
public class ViewRow<T> {

    public static final String ELEMENT_CLASS = "rowElementClass";

    @JsonProperty("id")
    private String documentId;

    @JsonProperty("key")
    private String key;

    @JsonProperty("value")
    @JsonDeserialize(using = ValueDeserializer.class)
    private T value;

    public String getDocumentId() {
        return documentId;
    }

    public String getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }
}


