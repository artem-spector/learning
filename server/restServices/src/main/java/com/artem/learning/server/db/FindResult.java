package com.artem.learning.server.db;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 2/27/16
 */
public class FindResult<T> {

    @JsonProperty("total_rows")
    private int totalRows;

    @JsonProperty("offset")
    private int offset;

    @JsonProperty("rows")
    private T[] rows;

    public int getTotalRows() {
        return totalRows;
    }

    public int getOffset() {
        return offset;
    }

    public T[] getRows() {
        return rows;
    }
}
