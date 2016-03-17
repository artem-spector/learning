package com.artem.learning.server.rest.app;

import com.artem.learning.server.model.Course;
import com.artem.learning.server.model.Student;
import com.artem.learning.server.model.StudentCourseAssignment;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    private String courseId;
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
        for (StudentCourseAssignment assignment : student.getCourseAssignments().values()) {
            if (assignment.getCourseDisplayName().equals(courseDisplayName)) {
                courseId = assignment.getCourseId();
                return;
            }
        }
        fail("Course not found");
    }

    public void beginLesson() throws Exception {
        String studentCoursePath = AppController.APP_STUDENTS_PATH + "/" + student.getId();
        MvcResult res = mvc.perform(post(studentCoursePath)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .param("courseId", courseId))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        lessonId = res.getResponse().getHeader("Location");
    }

    public void submitLessonRequest(Object trialResponse) throws Exception {
        String path = AppController.APP_LESSON_PATH + "/" + lessonId;
        MockHttpServletRequestBuilder post = post(path);
        if (trialResponse != null)
            post.param("trialResponse", mapper.writeValueAsString(trialResponse));

        MvcResult res = mvc.perform(post)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Map value = mapper.readValue(res.getResponse().getContentAsString(), Map.class);
    }

    private List<Student> getStudents() throws Exception {
        MvcResult res = mvc.perform(get(AppController.APP_STUDENTS_PATH).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        return unmarshalList(res, Student.class);
    }

    private List<Course> getCourses() throws Exception {
        String studentCourses = AppController.APP_STUDENTS_PATH + "/" + student.getId() + "/courses";
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
