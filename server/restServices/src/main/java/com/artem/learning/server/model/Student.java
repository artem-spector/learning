package com.artem.learning.server.model;

import com.artem.learning.server.couchdb.Document;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 2/27/16
 */
public class Student extends Document {

    public enum Gender {male, female}

    @JsonProperty
    private String docType;

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    @JsonProperty
    private Date birthDate;

    @JsonProperty
    private Gender gender;

    @JsonProperty
    private Map<String, CourseHistory> courses;

    public Student() {
    }

    public Student(String firstName, String lastName, Date birthDate, Gender gender) {
        docType = "student";
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public CourseHistory getCourseHistory(String courseId) {
        return courses == null ? null : courses.get(courseId);
    }

    public void activateCourse(String courseId) {
        CourseHistory history = getCourseHistory(courseId);
        if (history == null) {
            if (courses == null) courses = new HashMap<>();
            history = new CourseHistory(courseId);
            courses.put(courseId, history);
        }
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.toString();
        }
    }
}
