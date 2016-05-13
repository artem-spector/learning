package com.artem.learning.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * TODO: Document!
 *
 * @author artem on 3/5/16.
 */
public class StudentCourseAssignment {

    @JsonProperty("created")
    private Date createdAt;

    @JsonProperty("course_id")
    private String courseId;

    @JsonProperty("course_name")
    private String courseDisplayName;

    public StudentCourseAssignment() {
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseDisplayName() {
        return courseDisplayName;
    }

    @JsonIgnore
    public boolean isComplete() {
        return createdAt != null && courseId != null && courseDisplayName != null;
    }

    @JsonIgnore
    public void setCourse(Course course) {
        createdAt = new Date();
        this.courseId = course.getId();
        this.courseDisplayName = course.getDisplayName();
    }
}
