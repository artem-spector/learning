package com.artem.learning.server.db;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO: Document!
 *
 * @author artem on 2/28/16.
 */
public class CouchDBInfo {

    @JsonProperty("db_name")
    private String dbName;

    @JsonProperty("data_size")
    private int dataSize;

    @JsonProperty("doc_count")
    private int documentCount;

    public String getDbName() {
        return dbName;
    }

    public int getDataSize() {
        return dataSize;
    }

    public int getDocumentCount() {
        return documentCount;
    }
}
