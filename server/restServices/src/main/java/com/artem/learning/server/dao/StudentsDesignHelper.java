package com.artem.learning.server.dao;

import com.artem.learning.server.couchdb.Database;
import com.artem.learning.server.couchdb.DesignDocument;
import com.artem.learning.server.couchdb.View;
import com.artem.learning.server.couchdb.ViewDefinition;
import com.artem.learning.server.model.Student;
import org.springframework.stereotype.Component;

/**
 * TODO: Document!
 *
 * @author artem on 3/9/16.
 */
public class StudentsDesignHelper {

    private static final String DESIGN_DOC_NAME = "students";
    private static final String ALL_STUDENTS_VIEW = "all_students";

    public static DesignDocument getDesignDocument() {
        return new DesignDocument(DESIGN_DOC_NAME, "javascript",
                new ViewDefinition(ALL_STUDENTS_VIEW, "design/allStudents.js", null));
    }

    public static View<Student> getAllStudentsView(Database db) {
        return db.getView(DESIGN_DOC_NAME, ALL_STUDENTS_VIEW, Student.class);
    }
}
