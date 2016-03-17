package com.artem.learning.server.rest.app;

import com.artem.courses.digits.DigitsWriting;
import com.artem.learning.server.ServerApp;
import com.artem.learning.server.couchdb.Database;
import com.artem.learning.server.rest.admin.AdminClient;
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

/**
 * TODO: Document!
 *
 * @author artem on 3/12/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServerApp.class)
@WebAppConfiguration
public class AppLessonTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Database db;

    private AdminClient adminClient;
    private AppClient appClient;

    @Before
    public void setUp() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        adminClient = new AdminClient(mvc);
        appClient = new AppClient(mvc);
        adminCreateStudentAndAssignCourse();
    }

    @Test
    public void testBeginLesson() throws Exception {
        appClient.chooseStudent("Bill", "Gates");
        appClient.chooseCourse(new DigitsWriting().getDisplayName());
        appClient.beginLesson();
    }

    private void adminCreateStudentAndAssignCourse() throws Exception {
        adminClient.createStudent("Bill", "Gates", "1955-10-28", "male");
        Map<String, Object> student = chooseStudent(adminClient.getAllStudents(), "Bill", "Gates");

        Map<String, Object> course = adminClient.getAllCourses().get(0);
        adminClient.assignCourseToStudent(course, student);
        adminClient.updateStudent(student);
    }

    private Map<String, Object> chooseStudent(List<Map<String, Object>> students, String firstName, String lastName) {
        for (Map<String, Object> student : students) {
            if (student.get("firstName").equals(firstName) && student.get("lastName").equals(lastName))
                return student;
        }
        return null;
    }
}
