function(doc) {
    if (doc.login_id) {
        emit(doc.login_id, doc)
    }
}
