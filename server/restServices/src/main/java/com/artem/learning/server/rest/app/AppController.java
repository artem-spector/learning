package com.artem.learning.server.rest.app;

import com.artem.learning.server.dao.LessonDao;
import com.artem.learning.server.dao.StudentDao;
import com.artem.learning.server.model.Course;
import com.artem.learning.server.model.CourseRepository;
import com.artem.learning.server.model.Lesson;
import com.artem.learning.server.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @Autowired
    private LessonDao lessonDao;

    @Autowired
    private CourseRepository courseRepository;

    @RequestMapping(path = STUDENTS_PATH, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Student> getStudents() {
        return studentDao.getAllStudents();
    }

    @RequestMapping(path = STUDENTS_PATH + "/{studentId}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> beginLesson(@PathVariable String studentId, @RequestParam String courseId) {
        Course course = courseRepository.getCourse(courseId);
        Lesson lesson = course.prepareNewLesson(studentDao.getStudent(studentId));
        lessonDao.addLesson(lesson);
        return ResponseEntity.created(URI.create(lesson.getId())).build();
    }
}
