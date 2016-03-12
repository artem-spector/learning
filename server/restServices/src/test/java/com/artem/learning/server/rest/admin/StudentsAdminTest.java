package com.artem.learning.server.rest.admin;

import com.artem.learning.server.ServerApp;
import com.artem.learning.server.couchdb.Database;
import com.artem.learning.server.model.DateTimeUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void studentCRUDTest() throws Exception {
        // 1. no students
        List<Map<String, Object>> allStudents = getAllStudents();
        int numStudents = allStudents.size();

        // 2. create a student, no courses
        createStudent("James", "Bond", "1900-01-04", "male");
        allStudents = getAllStudents();
        assertEquals(1, allStudents.size() - numStudents);
        Map<String, Object> student = allStudents.get(allStudents.size() - 1);
        String studentId = (String) student.get("studentId");
        Map<String, Object> courses = (Map<String, Object>) student.get("courses");
        assertEquals(0, courses.size());

        // 3. add a course to the student
        List<Map<String, Object>> allCourses = getAllCourses();
        assertTrue(!allCourses.isEmpty());
        Map<String, Object> course = allCourses.get(0);

        Map<String, Object> assignment = new HashMap<>();
        String courseId = (String) course.get("id");
        courses.put(courseId, assignment);
        assignment.put("id", courseId);
        assignment.put("name", course.get("name"));
        assignment.put("assignedAt", DateTimeUtil.formatDateOnly(new Date()));

        updateStudent(student);
        student = getStudent(studentId);
        courses = (Map<String, Object>) student.get("courses");
        assertEquals(1, courses.size());

        // 4. remove course from student
        courses.remove(courseId);
        updateStudent(student);
        student = getStudent(studentId);
        courses = (Map<String, Object>) student.get("courses");
        assertEquals(0, courses.size());

        // 5.delete student
        deleteStudent(student);
        assertEquals(numStudents, getAllStudents().size());
    }

    private void createStudent(String firstName, String lastName, String birthDate, String gender) throws Exception {
        mvc.perform(post(StudentsAdminController.ADMIN_STUDENTS_PATH)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("birthDate", birthDate)
                .param("gender", gender))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private Map<String, Object> getStudent(String studentId) throws Exception {
        MvcResult result = mvc.perform(get(StudentsAdminController.ADMIN_STUDENTS_PATH + "/" + studentId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        String str = result.getResponse().getContentAsString();
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class);
        return new ObjectMapper().readValue(str, mapType);
    }

    private void updateStudent(Map<String, Object> student) throws Exception {
        mvc.perform(put(StudentsAdminController.ADMIN_STUDENTS_PATH + "/" + student.get("studentId"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(student)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private void deleteStudent(Map<String, Object> student) throws Exception {
        mvc.perform(delete(StudentsAdminController.ADMIN_STUDENTS_PATH + "/" + student.get("studentId")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private List<Map<String, Object>> getAllStudents() throws Exception {
        MvcResult result = mvc.perform(get(StudentsAdminController.ADMIN_STUDENTS_PATH).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        return unmarshalListOfMaps(result);
    }

    private List<Map<String, Object>> getAllCourses() throws Exception {
        MvcResult result = mvc.perform(get(CoursesAdminController.ADMIN_COURSES_PATH).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        return unmarshalListOfMaps(result);
    }

    private List<Map<String, Object>> unmarshalListOfMaps(MvcResult result) throws java.io.IOException {
        String str = result.getResponse().getContentAsString();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType mapType = typeFactory.constructMapType(Map.class, String.class, Object.class);
        JavaType listType = typeFactory.constructCollectionType(List.class, mapType);
        return new ObjectMapper().readValue(str, listType);
    }

}
