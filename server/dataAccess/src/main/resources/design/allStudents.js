function(doc) {
    if (doc.doc_type == "Student") {
        emit(doc._id, doc)
    }
}
