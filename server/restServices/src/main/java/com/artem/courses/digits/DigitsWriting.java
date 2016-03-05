package com.artem.courses.digits;

import com.artem.learning.server.model.Course;
import com.artem.learning.server.model.CourseHistory;
import com.artem.learning.server.model.Lesson;

/**
 * TODO: Document!
 *
 * @author artem on 3/5/16.
 */
public class DigitsWriting implements Course {

    @Override
    public String getId() {
        return "com.artem.digitsWriting";
    }

    @Override
    public String getDisplayName() {
        return "Writing Digits";
    }

    @Override
    public Lesson prepareNewLesson(CourseHistory history) {
        return new DigitWritingLesson(10);
    }
}
