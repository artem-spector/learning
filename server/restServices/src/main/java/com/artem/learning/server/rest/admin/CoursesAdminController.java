package com.artem.learning.server.rest.admin;

import com.artem.learning.server.model.Course;
import com.artem.learning.server.model.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO: Document!
 *
 * @author artem on 3/12/16.
 */
@RestController
@RequestMapping(path = CoursesAdminController.ADMIN_COURSES_PATH)
public class CoursesAdminController {

    public static final String ADMIN_COURSES_PATH = "/admin/courses";

    @Autowired
    private CourseRepository repo;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Course> getStudents() {
        return repo.getAvailableCourses();
    }

}
