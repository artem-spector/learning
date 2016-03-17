package com.artem.learning.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 2/27/16
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public abstract class Trial {

    @JsonProperty
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=DateTimeUtil.DATE_TIME_FORMAT)
    private Date creationTime;

    @JsonProperty
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=DateTimeUtil.DATE_TIME_FORMAT)
    private Date responseTime;

    public Trial() {
        creationTime = new Date();
    }

    public Object submitResponse(Object response) {
        responseTime = new Date();
        return setResponseAndGetFeedback(response);
    }

    public abstract Object getTask();

    protected abstract Object setResponseAndGetFeedback(Object response);
}
