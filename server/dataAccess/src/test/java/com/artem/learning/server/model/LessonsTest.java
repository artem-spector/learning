package com.artem.learning.server.model;

import com.artem.courses.digits.DigitsWriting;
import com.artem.TestApplication;
import com.artem.learning.server.couchdb.UpdateDocumentResponse;
import com.artem.learning.server.dao.LessonDao;
import com.artem.learning.server.dao.StudentDao;
import com.artem.server.api.drawing.DrawingRawData;
import com.artem.server.api.drawing.MotionData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 2/27/16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
@WebAppConfiguration
public class LessonsTest {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private LessonDao lessonDao;

    private ObjectMapper mapper = new ObjectMapper();

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
    public void testStudentsAndLessons() throws JsonProcessingException {
        // the course courseAssignment does not exist at the beginning
        assertNull(student1.getCourseAssignment(course.getId()));
        assertNull(student2.getCourseAssignment(course.getId()));

        // assign the course; still no lessons
        student1.assignCourse(course);
        student1 = saveAndRetrieveStudent(student1);
        assertNotNull(student1.getCourseAssignment(course.getId()));
        assertEquals(0, getStudentLessons(student1, course).size());

        student2.assignCourse(course);
        student2 = saveAndRetrieveStudent(student2);
        assertNotNull(student1.getCourseAssignment(course.getId()));
        assertEquals(0, getStudentLessons(student2, course).size());

        // Do the first lesson
        lessonDao.addLesson(course.prepareNewLesson(student1));
        List<Lesson> student1Lessons = getStudentLessons(student1, course);
        assertEquals(1, student1Lessons.size());
        assertEquals(0, getStudentLessons(student2, course).size());

        Lesson lesson = student1Lessons.get(0);
        assertEquals(0, lesson.getNumTrials());

        DrawingRawData drawing = new DrawingRawData();
        while (lesson.hasNextTrial()) {
            long presentedAt = System.currentTimeMillis();
            Object task = lesson.getNextTask();
            String response = generateResponse(drawing);
            lesson.submitTrialResponse(presentedAt, response);
        }
        UpdateDocumentResponse updateRes = lessonDao.updateLesson(lesson);
        assertTrue(updateRes.isSuccess());
        lesson = getStudentLessons(student1, course).get(0);
        assertTrue(lesson.getNumTrials() > 0);
        Date endTime = lesson.getEndTime();
        assertNotNull(endTime);
        assertTrue(endTime.getTime() > lesson.getStartTime().getTime());
    }

    private String generateResponse(DrawingRawData drawing) throws JsonProcessingException {
        long time = System.currentTimeMillis();
        drawing.clear(time);
        drawing.add(new MotionData[] {
                new MotionData(time + 10, 0, 0, MotionData.MotionType.Down),
                new MotionData(time + 20, 0, 1, MotionData.MotionType.Move),
                new MotionData(time + 30, 0, 1, MotionData.MotionType.Up)
        });
        return mapper.writeValueAsString(drawing);
    }

    private Student saveAndRetrieveStudent(Student student) {
        studentDao.updateStudent(student);
        return studentDao.getStudent(student.getId());
    }

    private List<Lesson> getStudentLessons(Student student, Course course) {
        StudentCourseAssignment assignment = student.getCourseAssignment(course.getId());
        assertNotNull(assignment);
        assertNotNull(assignment);
        assertNotNull(assignment.getCreatedAt());
        assertEquals(course.getId(), assignment.getCourseId());
        return lessonDao.getLessons(student.getId(), course.getId());
    }
}
