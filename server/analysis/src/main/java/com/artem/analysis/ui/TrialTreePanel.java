package com.artem.analysis.ui;

import com.artem.learning.server.dao.LessonDao;
import com.artem.learning.server.dao.StudentDao;
import com.artem.learning.server.model.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.List;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 5/13/16
 */
@Component
public class TrialTreePanel extends JScrollPane implements InitializingBean, TreeSelectionListener {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonDao lessonDao;

    @Autowired
    private List<TrialTreeSelectionListener> listeners;

    private JTree tree;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMutableTreeNode students = new DefaultMutableTreeNode("students");
        for (Student student : studentDao.getAllStudents()) {
            students.add(createStudentNode(student));
        }

        tree = new JTree(students);
        setViewportView(tree);
        tree.addTreeSelectionListener(this);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreeNode selected = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
        for (TrialTreeSelectionListener listener : listeners) {
            listener.nodeSelected(selected);
        }
    }

    private TrialTreeNode createStudentNode(Student student) {
        TrialTreeNode node = new TrialTreeNode(student);
        for (StudentCourseAssignment assignment : student.getCourseAssignments().values()) {
            node.add(createCourseNode(student.getId(), assignment));
        }
        return node;
    }

    private TrialTreeNode createCourseNode(String studentId, StudentCourseAssignment assignment) {
        TrialTreeNode node = new TrialTreeNode(assignment);
        for (Lesson lesson : lessonDao.getLessons(studentId, assignment.getCourseId())) {
            node.add(createLessonNode(lesson));
        }
        return node;
    }

    private TrialTreeNode createLessonNode(Lesson lesson) {
        TrialTreeNode node = new TrialTreeNode(lesson);
        for (Trial trial : lesson.getTrials()) {
            node.add(createTrialNode(trial));
        }
        return node;
    }

    private TrialTreeNode createTrialNode(Trial trial) {
        TrialTreeNode node = new TrialTreeNode(trial);
        return node;
    }
}
