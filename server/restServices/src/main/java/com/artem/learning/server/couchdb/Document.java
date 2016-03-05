package com.artem.learning.server.couchdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base class for "document" entities, defines id and revision.
 *
 * @author artem on 3/5/16.
 */
@JsonIgnoreProperties(value = {"_id", "_rev"}, allowSetters = true)
public abstract class Document {

    @JsonProperty(value = "_id")
    private String id;

    @JsonProperty(value = "_rev")
    private String revision;

    public String getId() {
        return id;
    }

    public String getRevision() {
        return revision;
    }

    protected Document() {
    }

    protected Document(String id) {
        this.id = id;
    }

    public void updatePersistentProperties(UpdateDocumentResponse res) {
        assert res.isSuccess();
        assert id == null || id.equals(res.getId());
        revision = res.getRevision();
    }
}
