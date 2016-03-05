package com.artem.learning.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO: Document!
 *
 * @author artem on 3/5/16.
 */
public class CourseHistory {

    @JsonProperty("created")
    private Date createdAt;

    @JsonProperty("course_id")
    private String courseId;

    @JsonProperty("lessons")
    private List<Lesson> lessons;

    public CourseHistory() {
    }

    public CourseHistory(String courseId) {
        createdAt = new Date();
        this.courseId = courseId;
        lessons = new ArrayList<>();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCourseId() {
        return courseId;
    }

    public int getNumLessons() {
        return lessons.size();
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public Lesson getLesson(int i) {
        return lessons.get(i);
    }
}
