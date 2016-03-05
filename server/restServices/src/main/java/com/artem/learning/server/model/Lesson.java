package com.artem.learning.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 2/27/16
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public abstract class Lesson {

    @JsonProperty("start_time")
    private Date startTime;

    @JsonProperty("end_time")
    private Date endTime;

    @JsonProperty("trials")
    private List<Trial> trials;

    public Lesson() {
        startTime = new Date();
        trials = new ArrayList<>();
    }

    public void end() {
        endTime = new Date();
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getNumTrials() {
        return trials.size();
    }

    @JsonIgnore
    public Trial getNextTrial() {
        Trial res = generateTrial();
        trials.add(res);
        return res;
    }

    @JsonIgnore
    public abstract boolean hasNextTrial();

    public abstract Trial generateTrial();
}
