package com.artem.learning.server.dao;

import com.artem.learning.server.couchdb.Database;
import com.artem.learning.server.couchdb.LookupViewResponse;
import com.artem.learning.server.couchdb.UpdateDocumentResponse;
import com.artem.learning.server.couchdb.ViewRow;
import com.artem.learning.server.model.DateTimeUtil;
import com.artem.learning.server.model.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO: Document!
 *
 * @author artem on 3/14/16.
 */
@Component
public class LessonDao {

    @Autowired
    private Database db;

    public List<Lesson> getLessons(String studentId, String courseId) {
        List<Lesson> res = new ArrayList<>();
        String[] startKey = {studentId, courseId, DateTimeUtil.formatDateTime(new Date(0))};
        String[] endKey = {studentId, courseId, DateTimeUtil.formatDateTime(new Date(System.currentTimeMillis() + 3600 * 1000))};
        LookupViewResponse<Lesson> response = LessonDesignHelper.getLessonsByStudentAndCourseView(db).lookup(startKey, endKey);
        for (ViewRow<Lesson> row : response.getRows()) {
            res.add(row.getValue());
        }
        return res;
    }

    public void addLesson(Lesson lesson) {
        db.addDocument(lesson);
    }

    public UpdateDocumentResponse updateLesson(Lesson lesson) {
        return db.updateDocument(lesson);
    }

    public Lesson getLesson(String lessonId) {
        return db.getDocument(lessonId, Lesson.class);
    }
}
