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

    @JsonIgnore
    public Map<String, StudentCourseAssignment> getCourseAssignments() {
        return courses;
    }

    public StudentCourseAssignment getCourseAssignment(String courseId) {
        return courses == null ? null : courses.get(courseId);
    }

    public void assignCourse(Course course) {
        String courseId = course.getId();
        StudentCourseAssignment assignment = getCourseAssignment(courseId);
        if (assignment == null) {
            if (courses == null) courses = new HashMap<>();
            assignment = new StudentCourseAssignment();
            assignment.setCourse(course);
            courses.put(courseId, assignment);
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
