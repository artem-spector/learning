package com.artem.learning.server.rest.app;

import com.artem.learning.server.dao.StudentDao;
import com.artem.learning.server.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO: Document!
 *
 * @author artem on 3/14/16.
 */
@RestController
@RequestMapping(path = AppController.APP_BASE_PATH)
public class AppController {

    public static final String APP_BASE_PATH = "/app";
    private static final String STUDENTS_PATH = "/students";
    public static final String APP_STUDENTS_PATH = APP_BASE_PATH + STUDENTS_PATH;

    @Autowired
    private StudentDao studentDao;

    @RequestMapping(path = STUDENTS_PATH, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Student> getStudents() {
        return studentDao.getAllStudents();
    }
}
