package com.artem.learning.server.model;

import com.artem.learning.server.couchdb.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 2/27/16
 */
public class Student extends Document {

    public enum Gender {male, female}

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
    private Map<String, StudentCourseAssignment> courses;

    public Student() {
        super("Student");
    }

    public Student(String firstName, String lastName, Date birthDate, Gender gender) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.courses = new HashMap<>();
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
            this.courses.put(courseId, new StudentCourseAssignment(courseId));
        }
    }

    @JsonIgnore
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

    public StudentCourseAssignment getCourseAssignment(String courseId) {
        return courses == null ? null : courses.get(courseId);
    }

    public void assignCourse(String courseId) {
        StudentCourseAssignment history = getCourseAssignment(courseId);
        if (history == null) {
            if (courses == null) courses = new HashMap<>();
            history = new StudentCourseAssignment(courseId);
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || !(obj instanceof Student)) return false;
        Student that = (Student) obj;

        Object[] thisState = {getId(),firstName, lastName, birthDate, gender, courses};
        Object[] thatState = {that.getId(), that.firstName, that.lastName, that.birthDate, that.gender, that.courses};
        return Arrays.equals(thisState, thatState);
    }
}
