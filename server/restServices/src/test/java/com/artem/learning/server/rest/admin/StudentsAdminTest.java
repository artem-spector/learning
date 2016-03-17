package com.artem.learning.server.rest.admin;

import com.artem.learning.server.ServerApp;
import com.artem.learning.server.couchdb.Database;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

/**
 * TODO: Document!
 *
 * @author artem on 3/9/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServerApp.class)
@WebAppConfiguration
public class StudentsAdminTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Database db;

    private AdminClient adminClient;

    @Before
    public void setUp() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        adminClient = new AdminClient(mvc);
    }

    @Test
    public void studentCRUDTest() throws Exception {
        // 1. no students
        List<Map<String, Object>> allStudents = adminClient.getAllStudents();
        int numStudents = allStudents.size();

        // 2. create a student, no courses
        adminClient.createStudent("James", "Bond", "1900-01-04", "male");
        allStudents = adminClient.getAllStudents();
        assertEquals(1, allStudents.size() - numStudents);
        Map<String, Object> student = allStudents.get(allStudents.size() - 1);
        String studentId = (String) student.get("_id");
        Map<String, Object> courses = (Map<String, Object>) student.get("courses");
        assertEquals(0, courses.size());

        // 3. add a course to the student
        List<Map<String, Object>> allCourses = adminClient.getAllCourses();
        assertTrue(!allCourses.isEmpty());
        Map<String, Object> course = allCourses.get(0);

        adminClient.assignCourseToStudent(course, student);
        adminClient.updateStudent(student);
        student = adminClient.getStudent(studentId);
        courses = (Map<String, Object>) student.get("courses");
        assertEquals(1, courses.size());

        // 4. remove course from student
        String courseId = (String) course.get("id");
        courses.remove(courseId);
        adminClient.updateStudent(student);
        student = adminClient.getStudent(studentId);
        courses = (Map<String, Object>) student.get("courses");
        assertEquals(0, courses.size());

        // 5.delete student
        adminClient.deleteStudent(student);
        assertEquals(numStudents, adminClient.getAllStudents().size());
    }

}
