package com.artem.analysis.ui;

import com.artem.courses.digits.DigitWritingTrial;
import com.artem.learning.server.model.Lesson;
import com.artem.learning.server.model.Student;
import com.artem.learning.server.model.StudentCourseAssignment;
import com.artem.learning.server.model.Trial;
import com.artem.server.api.drawing.DrawingRawData;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            return (String) ((Map) trial.getTask()).get("stimulus");
        else
            throw new IllegalStateException();
    }

    public void buildDetailsPanel(JPanel panel) {
        panel.setLayout(new BorderLayout());
        JPanel dataPanel = new JPanel();
        panel.add(dataPanel, BorderLayout.NORTH);
        if (student != null) {
            dataPanel.add(new Label("Student"));
            dataPanel.add(new Label(student.getLastName() + " " + student.getFirstName()));
            dataPanel.add(new Label(String.valueOf(student.getAge())));
            dataPanel.add(new Label(student.getGender().name()));
        } else if (assignment != null) {
            dataPanel.add(new Label("Course " + assignment.getCourseDisplayName()));
            dataPanel.add(new Label("assigned on " + dateFormat.format(assignment.getCreatedAt())));
        } else if (lesson != null) {
            int durationMin = (int) ((lesson.getEndTime().getTime() - lesson.getStartTime().getTime()) / 60000);
            dataPanel.add(new Label("Lesson"));
            dataPanel.add(new Label("Started on " + dateTimeFormat.format(lesson.getStartTime())));
            dataPanel.add(new Label("Duration " + durationMin + " min."));
        } else if (trial != null) {
            dataPanel.add(new Label("Trial"));
            int durationSec = (int) ((trial.getSubmittedAt().getTime() - trial.getPresentedAt().getTime()) / 1000);
            dataPanel.add(new Label("Presented at " + timeFormat.format(trial.getPresentedAt())));
            dataPanel.add(new Label("Duration " + durationSec + " sec."));
            if (trial instanceof DigitWritingTrial) {
                Button playButton = new Button("Play");
                dataPanel.add(playButton);
                DrawingRawData drawingData = ((DigitWritingTrial) trial).getDrawingData();
                final DrawingPlaybackPanel playbackPanel = new DrawingPlaybackPanel(drawingData);
                panel.add(playbackPanel, BorderLayout.CENTER);
                playButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        playbackPanel.clear();
                        playbackPanel.play();
                    }
                });
            }
        }
    }
}
