package com.artem.analysis.ui;

import com.artem.learning.server.dao.LessonDao;
import com.artem.learning.server.dao.StudentDao;
import com.artem.learning.server.model.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.text.DateFormat;
import java.util.Map;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 5/13/16
 */
@Component
public class TrialBrowsingPanel extends JScrollPane implements InitializingBean {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonDao lessonDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMutableTreeNode students = new DefaultMutableTreeNode("students");
        for (Student student : studentDao.getAllStudents()) {
            students.add(createStudentNode(student));
        }

        setViewportView(new JTree(students));
    }

    private DefaultMutableTreeNode createStudentNode(Student student) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(student.getFirstName() + " " + student.getLastName());
        for (StudentCourseAssignment assignment : student.getCourseAssignments().values()) {
            node.add(createCourseNode(student.getId(), assignment.getCourseId()));
        }
        return node;
    }

    private DefaultMutableTreeNode createCourseNode(String studentId, String courseId) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(courseRepository.getCourse(courseId).getDisplayName());
        for (Lesson lesson : lessonDao.getLessons(studentId, courseId)) {
            node.add(createLessonNode(lesson));
        }

        return node;
    }

    private DefaultMutableTreeNode createLessonNode(Lesson lesson) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(DateFormat.getDateTimeInstance().format(lesson.getStartTime()));
        for (Trial trial : lesson.getTrials()) {
            node.add(createTrialNode(trial));
        }
        return node;
    }

    private DefaultMutableTreeNode createTrialNode(Trial trial) {
        String stimulus = (String) ((Map)trial.getTask()).get("stimulus");
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(stimulus);
        return node;
    }
}
