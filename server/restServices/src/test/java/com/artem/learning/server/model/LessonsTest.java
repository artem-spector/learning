package com.artem.learning.server.model;

import com.artem.courses.digits.DigitsWriting;
import com.artem.learning.server.ServerApp;
import com.artem.learning.server.couchdb.Database;
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
    private Database db;
    private Student student;
    private DigitsWriting course;

    @Before
    public void setUp() {
        if (db.getDatabaseInfo() != null) db.deleteDB();
        db.createDB();
        Calendar calendar = Calendar.getInstance();
        calendar.set(1900, Calendar.JANUARY, 4);
        student = new Student("James", "Bond", calendar.getTime(), Student.Gender.male);
        db.addDocument(student);
        course = new DigitsWriting();
    }

    @Test
    public void testFirstLesson() {
        // the course history does not exist at the beginning
        CourseHistory history = student.getCourseHistory(course.getId());
        assertNull(history);

        // after activation the course history exists but is empty
        student.activateCourse(course.getId());
        saveAndRetrieveStudent();
        history = student.getCourseHistory(course.getId());
        assertNotNull(history);
        assertNotNull(history.getCreatedAt());
        assertEquals(course.getId(), history.getCourseId());
        assertEquals(0, history.getNumLessons());

        // Do the first lesson
        Lesson lesson = course.prepareNewLesson(history);
        history.addLesson(lesson);
        saveAndRetrieveStudent();

        history = student.getCourseHistory(course.getId());
        lesson = history.getLesson(history.getNumLessons() - 1);
        while (lesson.hasNextTrial()) {
            Trial trial = lesson.getNextTrial();
            Object response = generateResponse(trial.getStimulus());
            trial.setResponse(response);
        }
        saveAndRetrieveStudent();

        // make sure the history is stored
        history = student.getCourseHistory(course.getId());
        assertEquals(1, history.getNumLessons());
        lesson = history.getLesson(0);
        assertNotNull(lesson.getStartTime());
        assertNotNull(lesson.getEndTime());
        assertTrue(lesson.getNumTrials() > 0);
    }

    private void saveAndRetrieveStudent() {
        db.updateDocument(student);
        student = db.getDocument(student.getId(), Student.class);
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

}
