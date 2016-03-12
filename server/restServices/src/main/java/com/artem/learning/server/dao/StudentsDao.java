package com.artem.learning.server.dao;

import com.artem.learning.server.couchdb.Database;
import com.artem.learning.server.couchdb.LookupViewResponse;
import com.artem.learning.server.couchdb.UpdateDocumentResponse;
import com.artem.learning.server.couchdb.ViewRow;
import com.artem.learning.server.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO: Document!
 *
 * @author artem on 3/9/16.
 */
@Component
public class StudentsDao {

    @Autowired
    private Database db;

    public UpdateDocumentResponse createStudent(String firstName, String lastName, Date birthDate, Student.Gender gender) {
        Student student = new Student(firstName, lastName, birthDate, gender);
        return db.addDocument(student);
    }

    public List<Student> getAllStudents() {
        LookupViewResponse<Student> response = StudentsDesignHelper.getAllStudentsView(db).lookup(null, null);
        ViewRow<Student>[] rows = response.getRows();

        List<Student> res = new ArrayList<>(rows.length);
        for (ViewRow<Student> row : rows) {
            res.add(row.getValue());
        }
        return res;
    }

}
