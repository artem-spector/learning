package com.artem.learning.server.rest.app;

import com.artem.learning.server.model.Course;
import com.artem.learning.server.model.Student;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO: Document!
 *
 * @author artem on 3/12/16.
 */
public class AppClient {

    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();
    private TypeFactory typeFactory = TypeFactory.defaultInstance();

    private Student student;
    private Course course;
    private String lessonId;

    public AppClient(MockMvc mvc) {
        this.mvc = mvc;
    }

    public void chooseStudent(String firstName, String lastName) throws Exception {
        this.student = null;
        for (Student student : getStudents()) {
            if (student.getFirstName().equals(firstName) && student.getLastName().equals(lastName)) {
                this.student = student;
                return;
            }
        }
        fail("Student not found");
    }

    public void chooseCourse(String courseDisplayName) throws Exception {
        for (Course course : getCourses()) {
            if (course.getDisplayName().equals(courseDisplayName)) {
                this.course = course;
            }
        }
        fail("Course not found");
    }

    public void beginLesson() throws Exception {
        String studentCoursePath = AppController.APP_STUDENTS_PATH + "/" + student.getStudentId() + "/courses/" + course.getId();
        MvcResult res = mvc.perform(post(studentCoursePath).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andReturn();
        lessonId = res.getResponse().getHeader("Location");
    }

    private List<Student> getStudents() throws Exception {
        MvcResult res = mvc.perform(get(AppController.APP_STUDENTS_PATH).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        return unmarshalList(res, Student.class);
    }

    private List<Course> getCourses() throws Exception {
        String studentCourses = AppController.APP_STUDENTS_PATH + "/" + student.getStudentId() + "/courses";
        MvcResult res = mvc.perform(get(studentCourses).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        return unmarshalList(res, Course.class);
    }

    private <T> List<T> unmarshalList(MvcResult res, Class<T> elementType) throws IOException {
        JavaType listType = typeFactory.constructCollectionType(List.class, elementType);
        return mapper.readValue(res.getResponse().getContentAsString(), listType);
    }
}