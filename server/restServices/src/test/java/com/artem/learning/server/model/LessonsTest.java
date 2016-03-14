package com.artem.learning.server.model;

import com.artem.courses.digits.DigitsWriting;
import com.artem.learning.server.ServerApp;
import com.artem.learning.server.couchdb.UpdateDocumentResponse;
import com.artem.learning.server.dao.LessonDao;
import com.artem.learning.server.dao.StudentDao;
import com.artem.server.api.drawing.DrawingRawData;
import com.artem.server.api.drawing.MotionData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 2/27/16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServerApp.class)
@WebAppConfiguration
public class LessonsTest {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private LessonDao lessonDao;

    private Student student1;
    private Student student2;
    private DigitsWriting course;

    @Before
    public void setUp() {
        UpdateDocumentResponse response =
                studentDao.createStudent("James", "Bond", DateTimeUtil.date(1900, Calendar.JANUARY, 4), Student.Gender.male);
        student1 = studentDao.getStudent(response.getId());
        response =
                studentDao.createStudent("Hilary", "Clinton", DateTimeUtil.date(1947, Calendar.OCTOBER, 26), Student.Gender.female);
        student2 = studentDao.getStudent(response.getId());
        course = new DigitsWriting();
    }

    @Test
    public void testStudentsAndLessons() {
        // the course courseAssignment does not exist at the beginning
        assertNull(student1.getCourseAssignment(course.getId()));
        assertNull(student2.getCourseAssignment(course.getId()));

        // assign the course; still no lessons
        student1.assignCourse(course.getId());
        student1 = saveAndRetrieveStudent(student1);
        assertEquals(0, getStudentLessons(student1, course).size());

        student2.assignCourse(course.getId());
        student2 = saveAndRetrieveStudent(student2);
        assertEquals(0, getStudentLessons(student2, course).size());

        // Do the first lesson
        lessonDao.addLesson(course.prepareNewLesson(student1));
        assertEquals(1, getStudentLessons(student1, course).size());
        assertEquals(0, getStudentLessons(student2, course).size());


/*
        courseAssignment.addLesson(lesson);
        saveAndRetrieveStudent();

        courseAssignment = student.getCourse(course.getId());
        lesson = courseAssignment.getLesson(courseAssignment.getNumLessons() - 1);
        while (lesson.hasNextTrial()) {
            Trial trial = lesson.getNextTrial();
            Object response = generateResponse(trial.getStimulus());
            trial.setResponse(response);
        }
        saveAndRetrieveStudent();

        // make sure the courseAssignment is stored
        courseAssignment = student.getCourse(course.getId());
        assertEquals(1, courseAssignment.getNumLessons());
        lesson = courseAssignment.getLesson(0);
        assertNotNull(lesson.getStartTime());
        assertNotNull(lesson.getEndTime());
        assertTrue(lesson.getNumTrials() > 0);
*/
    }

    private Student saveAndRetrieveStudent(Student student) {
        studentDao.updateStudent(student, student.getStudentId());
        return studentDao.getStudent(student.getId());
    }

    private Object generateResponse(Object stimulus) {
        DrawingRawData drawing = new DrawingRawData();
        drawing.add(new MotionData[] {
                new MotionData(System.currentTimeMillis(), 0, 0, MotionData.MotionType.Down),
                new MotionData(System.currentTimeMillis(), 0, 1, MotionData.MotionType.Move),
                new MotionData(System.currentTimeMillis(), 0, 1, MotionData.MotionType.Up)
        });
        return drawing;
    }

    private List<Lesson> getStudentLessons(Student student, Course course) {
        StudentCourseAssignment assignment = student.getCourseAssignment(course.getId());
        assertNotNull(assignment);
        assertNotNull(assignment);
        assertNotNull(assignment.getCreatedAt());
        assertEquals(course.getId(), assignment.getCourseId());
        return lessonDao.getLessons(student.getStudentId(), course.getId());
    }
}
