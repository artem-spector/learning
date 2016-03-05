package com.artem.learning.server.couchdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * TODO: Document!
 *
 * @author artem on 2/29/16.
 */
public class ViewRow<T> {

    @JsonProperty("id")
    private String documentId;

    @JsonProperty("key")
    private Object key;

    @JsonProperty("value")
    @JsonDeserialize(using = ViewRowValueDeserializer.class)
    private T value;

    public String getDocumentId() {
        return documentId;
    }

    public Object getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }

}


