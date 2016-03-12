package com.artem.learning.server.model;

import com.artem.learning.server.couchdb.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Calendar;
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
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern=DateTimeUtil.DATE_ONLY_FORMAT)
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
        this.courses = new HashMap<>();
    }

    @JsonProperty
    public String getStudentId() {
        return getId();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void updateFrom(Student in) {
        this.firstName = in.firstName;
        this.lastName = in.lastName;
        this.birthDate = in.birthDate;
        this.gender = in.gender;

        // retain only the courses which were not removed
        this.courses.keySet().retainAll(in.courses.keySet());

        // go over new assignments and add them manually
        in.courses.keySet().removeAll(courses.keySet());
        for (String courseId : in.courses.keySet()) {
            this.courses.put(courseId, new CourseHistory(courseId));
        }
    }

    public int getAge() {
        Calendar dob = Calendar.getInstance();
        dob.setTime(birthDate);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
            age--;
        } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }
        return age;
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
