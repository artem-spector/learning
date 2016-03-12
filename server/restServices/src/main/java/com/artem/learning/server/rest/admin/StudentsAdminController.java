package com.artem.learning.server.rest.admin;

import com.artem.learning.server.couchdb.UpdateDocumentResponse;
import com.artem.learning.server.dao.StudentsDao;
import com.artem.learning.server.model.DateTimeUtil;
import com.artem.learning.server.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Document!
 *
 * @author artem on 3/9/16.
 */

@RestController
@RequestMapping(path = StudentsAdminController.ADMIN_STUDENTS_PATH)
public class StudentsAdminController {

    public static final String ADMIN_STUDENTS_PATH = "/admin/students";

    @Autowired
    private StudentsDao dao;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> createStudent(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String birthDate,
            @RequestParam Student.Gender gender) {
        UpdateDocumentResponse response = dao.createStudent(firstName, lastName, DateTimeUtil.parseDateOnly(birthDate), gender);
        if (response.isSuccess())
            return ResponseEntity.ok("ok");
        else
            return ResponseEntity.status(response.getStatus()).body("Student creation failed");
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Student> getStudents() {
        List<Student> allStudents = dao.getAllStudents();
        return allStudents;
    }
}
