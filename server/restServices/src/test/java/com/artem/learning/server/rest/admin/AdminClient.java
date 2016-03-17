package com.artem.learning.server.rest.admin;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO: Document!
 *
 * @author artem on 3/12/16.
 */
public class AdminClient {

    private MockMvc mvc;

    public AdminClient(MockMvc mvc) {
        this.mvc = mvc;
    }

    public void createStudent(String firstName, String lastName, String birthDate, String gender) throws Exception {
        mvc.perform(post(StudentsAdminController.ADMIN_STUDENTS_PATH)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("birthDate", birthDate)
                .param("gender", gender))
                .andDo(print())
                .andExpect(status().isOk());
    }

    public Map<String, Object> getStudent(String studentId) throws Exception {
        MvcResult result = mvc.perform(get(StudentsAdminController.ADMIN_STUDENTS_PATH + "/" + studentId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        String str = result.getResponse().getContentAsString();
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class);
        return new ObjectMapper().readValue(str, mapType);
    }

    public void updateStudent(Map<String, Object> student) throws Exception {
        mvc.perform(put(StudentsAdminController.ADMIN_STUDENTS_PATH + "/" + student.get("_id"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(student)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    public void deleteStudent(Map<String, Object> student) throws Exception {
        mvc.perform(delete(StudentsAdminController.ADMIN_STUDENTS_PATH + "/" + student.get("_id")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    public List<Map<String, Object>> getAllStudents() throws Exception {
        MvcResult result = mvc.perform(get(StudentsAdminController.ADMIN_STUDENTS_PATH).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        return unmarshalListOfMaps(result);
    }

    public List<Map<String, Object>> getAllCourses() throws Exception {
        MvcResult result = mvc.perform(get(CoursesAdminController.ADMIN_COURSES_PATH).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        return unmarshalListOfMaps(result);
    }

    public void assignCourseToStudent(Map<String, Object> course, Map<String, Object> student) {
        Map<Object, Object> courses = (Map<Object, Object>) student.get("courses");
        assert courses != null;

        Map<String, Object> assignment = new HashMap<>();
        String courseId = (String) course.get("id");
        courses.put(courseId, assignment);
        assignment.put("id", courseId);
    }

    private List<Map<String, Object>> unmarshalListOfMaps(MvcResult result) throws java.io.IOException {
        String str = result.getResponse().getContentAsString();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType mapType = typeFactory.constructMapType(Map.class, String.class, Object.class);
        JavaType listType = typeFactory.constructCollectionType(List.class, mapType);
        return new ObjectMapper().readValue(str, listType);
    }


}
