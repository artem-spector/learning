function(doc) {
    if (doc.studentId !== undefined) {
        emit(doc.studentId, doc)
    }
}
