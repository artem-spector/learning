package com.artem.learning.server.model;

import com.artem.courses.digits.DigitsWriting;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Document!
 *
 * @author artem on 3/12/16.
 */
@Component
public class CourseRepository {

    public List<Course> getAvailableCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(new DigitsWriting());
        return courses;
    }

    public Course getCourse(String id) {
        DigitsWriting res = new DigitsWriting();
        return id.equals(res.getId()) ? res : null;
    }
}
