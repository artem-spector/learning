package com.artem.learning.server.couchdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base class for "document" entities, defines id and revision.
 *
 * @author artem on 3/5/16.
 */
public abstract class Document {

    @JsonProperty("_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @JsonProperty("_rev")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String revision;

    @JsonProperty("doc_type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String documentType;

    public String getId() {
        return id;
    }

    public String getRevision() {
        return revision;
    }

    @JsonCreator
    protected Document(@JsonProperty("doc_type") String documentType) {
        this.documentType = documentType;
    }

    @JsonCreator
    protected Document(@JsonProperty("doc_type") String documentType, @JsonProperty("_id") String id) {
        this.documentType = documentType;
        this.id = id;
    }

    public void updatePersistentProperties(UpdateDocumentResponse res) {
        assert res.isSuccess();
        assert id == null || id.equals(res.getId());
        if (id == null) id = res.getId();
        revision = res.getRevision();
    }
}
