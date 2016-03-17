package com.artem.learning.server.rest.admin;

import com.artem.learning.server.couchdb.UpdateDocumentResponse;
import com.artem.learning.server.dao.StudentDao;
import com.artem.learning.server.model.CourseRepository;
import com.artem.learning.server.model.DateTimeUtil;
import com.artem.learning.server.model.Student;
import com.artem.learning.server.model.StudentCourseAssignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    private StudentDao dao;

    @Autowired
    private CourseRepository courseRepository;

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

    @RequestMapping(path = "/{studentId}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> updateStudent(@RequestBody Student inStudent, @PathVariable String studentId) {
        assert inStudent.getId().equals(studentId);

        // if the courses were assigned on the client side the assignment data may be incomplete
        for (Map.Entry<String, StudentCourseAssignment> entry : inStudent.getCourseAssignments().entrySet()) {
            String courseId = entry.getKey();
            StudentCourseAssignment assignment = entry.getValue();
            if (!assignment.isComplete()) {
                assignment.setCourse(courseRepository.getCourse(courseId));
            }
        }

        UpdateDocumentResponse response = dao.updateStudent(inStudent);
        if (response.isSuccess())
            return ResponseEntity.ok("ok");
        else
            return ResponseEntity.status(response.getStatus()).body("Student update failed");
    }

    @RequestMapping(path = "/{studentId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deleteStudent(@PathVariable String studentId) {
        UpdateDocumentResponse response = dao.deleteStudent(studentId);
        if (response.isSuccess())
            return ResponseEntity.ok("ok");
        else
            return ResponseEntity.status(response.getStatus()).body("Student delete failed");
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Student> getStudents() {
        return dao.getAllStudents();
    }

    @RequestMapping(path = "/{studentId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Student getStudent(@PathVariable String studentId) {
        return dao.getStudent(studentId);
    }

}
