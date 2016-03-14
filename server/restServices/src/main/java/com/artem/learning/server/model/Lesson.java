package com.artem.learning.server.model;

import com.artem.learning.server.couchdb.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public abstract class Lesson extends Document {

    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("course_id")
    private String courseId;

    @JsonProperty("start_time")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=DateTimeUtil.DATE_TIME_FORMAT)
    private Date startTime;

    @JsonProperty("end_time")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=DateTimeUtil.DATE_TIME_FORMAT)
    private Date endTime;

    @JsonProperty("trials")
    private List<Trial> trials;

    public Lesson() {
    }

    public Lesson(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        startTime = new Date();
        trials = new ArrayList<>();
    }

    @JsonProperty
    public String getLessonId() {
        return getId();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
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
