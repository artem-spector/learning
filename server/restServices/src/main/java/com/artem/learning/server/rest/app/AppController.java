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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

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
    private static final String LESSON_PATH = "/lesson";

    public static final String APP_STUDENTS_PATH = APP_BASE_PATH + STUDENTS_PATH;
    public static final String APP_LESSON_PATH = APP_BASE_PATH + LESSON_PATH;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private LessonDao lessonDao;

    @Autowired
    private CourseRepository courseRepository;

    @RequestMapping(path = STUDENTS_PATH, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Callable<List<Student>> getStudents() {
        return () -> studentDao.getAllStudents();
    }

    @RequestMapping(path = STUDENTS_PATH + "/{studentId}", method = RequestMethod.POST)
    @ResponseBody
    public Callable<ResponseEntity<Void>> beginLesson(@PathVariable String studentId, @RequestParam String courseId) {
        return () -> {
            Course course = courseRepository.getCourse(courseId);
            Lesson lesson = course.prepareNewLesson(studentDao.getStudent(studentId));
            lessonDao.addLesson(lesson);
            return ResponseEntity.created(URI.create(lesson.getId())).build();
        };
    }

    @RequestMapping(path = LESSON_PATH + "/{lessonId}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Callable<ResponseEntity<Map<String, Object>>> processLessonRequest(@PathVariable String lessonId, @RequestParam(name = "trialResponse", required = false) String trialResponse) {
        return () -> {
            assert lessonId != null;
            Lesson lesson = lessonDao.getLesson(lessonId);
            assert lesson != null;

            Map<String, Object> res = new HashMap<>();

            if (trialResponse != null) {
                res.put("trialFeedback", lesson.submitTrialResponse(trialResponse));
            }

            if (lesson.hasNextTrial()) {
                res.put("nextTrial", lesson.getNextTask());
            } else {
                res.put("lessonFeedback", lesson.getLessonFeedback());
            }

            lessonDao.updateLesson(lesson);
            return ResponseEntity.ok(res);
        };
    }
}
