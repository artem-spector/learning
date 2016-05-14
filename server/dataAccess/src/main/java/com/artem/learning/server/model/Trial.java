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
    private Date createdAt;

    @JsonProperty
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=DateTimeUtil.DATE_TIME_FORMAT)
    private Date presentedAt;

    @JsonProperty
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=DateTimeUtil.DATE_TIME_FORMAT)
    private Date submittedAt;

    public Trial() {
        createdAt = new Date();
    }

    public Object submitResponse(long presentedAt, String responseStr) {
        submittedAt = new Date();
        this.presentedAt = new Date(presentedAt);
        return setResponseAndGetFeedback(responseStr);
    }

    public Date getPresentedAt() {
        return presentedAt;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public abstract Object getTask();

    protected abstract Object setResponseAndGetFeedback(String responseStr);
}
