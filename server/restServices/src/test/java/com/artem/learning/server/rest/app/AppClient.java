package com.artem.learning.server.rest.app;

import com.artem.learning.server.model.Student;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

/**
 * TODO: Document!
 *
 * @author artem on 3/12/16.
 */
public class AppClient {

    private MockMvc mvc;

    public AppClient(MockMvc mvc) {
        this.mvc = mvc;
    }

    public List<Student> getStudents() {
        return null;
    }

    public void beginLesson() {

    }

    public void chooseStudent(String firstName, String lastName) {

    }

    public void chooseCourse(String courseDisplayName) {

    }
}
