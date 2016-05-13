package com.artem.learning.server.dao;

import com.artem.learning.server.couchdb.Database;
import com.artem.learning.server.couchdb.DesignDocument;
import com.artem.learning.server.couchdb.View;
import com.artem.learning.server.couchdb.ViewDefinition;
import com.artem.learning.server.model.Lesson;

/**
 * TODO: Document!
 *
 * @author artem on 3/14/16.
 */
public class LessonDesignHelper {

    private static final String DESIGN_DOC_NAME = "lessons";
    private static final String LESSONS_BY_STUDENT_COURSE_VIEW = "lessons_by_student_course";

    public static DesignDocument getDesignDocument() {
        return new DesignDocument(DESIGN_DOC_NAME, "javascript",
                new ViewDefinition(LESSONS_BY_STUDENT_COURSE_VIEW, "design/lessonsByStudentAndCourse.js", null));
    }

    public static View<Lesson> getLessonsByStudentAndCourseView(Database db) {
        return db.getView(DESIGN_DOC_NAME, LESSONS_BY_STUDENT_COURSE_VIEW, Lesson.class);
    }
}
