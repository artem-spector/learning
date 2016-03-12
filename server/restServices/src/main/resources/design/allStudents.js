function(doc) {
    if (doc.docType == "student") {
        emit(doc.firstName, doc)
    }
}
