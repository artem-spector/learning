package com.artem.learning.server.dao;

import com.artem.learning.server.couchdb.*;
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

    public Student getStudent(String studentId) {
        return db.getDocument(studentId, Student.class);
    }

    public UpdateDocumentResponse updateStudent(Student inStudent, String id) {
        assert id != null;
        Student student = db.getDocument(id, Student.class);
        student.updateFrom(inStudent);
        return db.updateDocument(student);
    }

    public UpdateDocumentResponse deleteStudent(String studentId) {
        Student student = db.getDocument(studentId, Student.class);
        return db.deleteDocument(student);
    }
}
