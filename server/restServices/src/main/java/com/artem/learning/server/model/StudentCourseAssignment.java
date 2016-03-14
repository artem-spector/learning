package com.artem.learning.server.model;

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

    public StudentCourseAssignment() {
    }

    public StudentCourseAssignment(String courseId) {
        createdAt = new Date();
        this.courseId = courseId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCourseId() {
        return courseId;
    }

}
