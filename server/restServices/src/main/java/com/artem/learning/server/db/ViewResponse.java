package com.artem.learning.server.db;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 2/27/16
 */
public class ViewResponse<T> {


    @JsonProperty("total_rows")
    private int totalRows;

    @JsonProperty("offset")
    private int offset;

    @JsonProperty("rows")
    private ViewRow<T>[] rows;

    public int getTotalRows() {
        return totalRows;
    }

    public int getOffset() {
        return offset;
    }

    public ViewRow<T>[] getRows() {
        return rows;
    }
}
