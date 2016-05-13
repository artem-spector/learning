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

    @JsonProperty("curr_trial")
    private Trial currentTrial;

    public Lesson() {
        super("Lesson");
    }

    public Lesson(String studentId, String courseId) {
        this();
        this.studentId = studentId;
        this.courseId = courseId;
        startTime = new Date();
        trials = new ArrayList<>();
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
    public Object getNextTask() {
        if (currentTrial == null) {
            currentTrial = generateTrial();
        }
        return currentTrial.getTask();
    }

    public Object submitTrialResponse(long presentedAt, String trialResponseStr) {
        assert currentTrial != null && trialResponseStr != null;
        Object feedback = currentTrial.submitResponse(presentedAt, trialResponseStr);
        trials.add(currentTrial);
        currentTrial = null;
        return feedback;
    }

    @JsonIgnore
    public abstract boolean hasNextTrial();

    public abstract Trial generateTrial();

    @JsonIgnore
    public abstract Object getLessonFeedback();
}
