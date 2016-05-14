package com.artem.analysis.ui;

import com.artem.learning.server.model.Lesson;
import com.artem.learning.server.model.Student;
import com.artem.learning.server.model.StudentCourseAssignment;
import com.artem.learning.server.model.Trial;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.text.DateFormat;
import java.util.Map;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 5/14/16
 */
public class TrialTreeNode extends DefaultMutableTreeNode {

    private static DateFormat dateTimeFormat = DateFormat.getDateTimeInstance();
    private static DateFormat dateFormat = DateFormat.getDateInstance();
    private static DateFormat timeFormat = DateFormat.getTimeInstance();

    private Student student;
    private StudentCourseAssignment assignment;
    private Lesson lesson;
    private Trial trial;

    private TrialTreeNode(Student student, StudentCourseAssignment assignment, Lesson lesson, Trial trial) {
        this.student = student;
        this.assignment = assignment;
        this.lesson = lesson;
        this.trial = trial;
        setUserObject(getDisplayName());
    }

    public TrialTreeNode(Student student) {
        this(student, null, null, null);
    }

    public TrialTreeNode(StudentCourseAssignment assignment) {
        this(null, assignment, null, null);
    }

    public TrialTreeNode(Lesson lesson) {
        this(null, null, lesson, null);
    }

    public TrialTreeNode(Trial trial) {
        this(null, null, null, trial);
    }

    private String getDisplayName() {
        if (student != null)
            return student.getFirstName() + " " + student.getLastName();
        else if (assignment != null)
            return assignment.getCourseDisplayName();
        else if (lesson != null)
            return dateTimeFormat.format(lesson.getStartTime());
        else if (trial != null)
            return (String) ((Map)trial.getTask()).get("stimulus");
        else
            throw new IllegalStateException();
    }

    public void buildDetailsPanel(JPanel panel) {
        panel.setLayout(new FlowLayout());
        if (student != null) {
            panel.add(new Label("Student"));
            panel.add(new Label(student.getLastName() + " " + student.getFirstName()));
            panel.add(new Label(String.valueOf(student.getAge())));
            panel.add(new Label(student.getGender().name()));
        } else if (assignment != null) {
            panel.add(new Label("Course " + assignment.getCourseDisplayName()));
            panel.add(new Label("assigned on " + dateFormat.format(assignment.getCreatedAt())));
        } else if (lesson != null) {
            int durationMin = (int) ((lesson.getEndTime().getTime() - lesson.getStartTime().getTime()) / 60000);
            panel.add(new Label("Lesson"));
            panel.add(new Label("Started on " + dateTimeFormat.format(lesson.getStartTime())));
            panel.add(new Label("Duration " + durationMin + " min."));
        } else if (trial != null) {
            panel.add(new Label("Trial"));
            int durationSec = (int) ((trial.getSubmittedAt().getTime() - trial.getPresentedAt().getTime()) / 1000);
            panel.add(new Label("Presented at " + timeFormat.format(trial.getPresentedAt())));
            panel.add(new Label("Duration " + durationSec + " sec."));
        }
    }
}
