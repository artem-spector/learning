function(doc) {
    if (doc.doc_type == "Lesson") {
        emit([doc.student_id, doc.course_id, doc.start_time], doc)
    }
}