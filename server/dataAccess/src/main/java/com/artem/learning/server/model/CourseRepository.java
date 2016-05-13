package com.artem.learning.server.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TODO: Document!
 *
 * @author artem on 3/12/16.
 */
@Component
public class CourseRepository {

    @Autowired
    private List<Course> availableCourses;

    public List<Course> getAvailableCourses() {
        return availableCourses;
    }

    public Course getCourse(String id) {
        for (Course course : availableCourses) {
            if (course.getId().equals(id))
                return course;
        }
        return null;
    }
}
