package com.artem.learning.server.couchdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

/**
 * TODO: Document!
 *
 * @author artem on 2/28/16.
 */
public class UpdateDocumentResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("ok")
    private boolean success;

    @JsonProperty("rev")
    private String revision;

    @JsonIgnore
    private HttpStatus status;

    public UpdateDocumentResponse() {
    }

    public UpdateDocumentResponse(boolean success, HttpStatus status) {
        this.success = success;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getRevision() {
        return revision;
    }

    public boolean isConflict() {
        return HttpStatus.CONFLICT.equals(status);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
