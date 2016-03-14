function(doc) {
    if (doc.lessonId !== undefined) {
        emit([doc.student_id, doc.course_id, doc.start_time], doc)
    }
}