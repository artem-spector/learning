function deleteStudent(name, id) {
    var doDelete = confirm("Are you sure to delete student " + name + "?");
    if (doDelete) {
        var deleteForm = document.getElementById('deleteStudentForm');
        deleteForm.action = '/console/deleteStudent/' + id;
        deleteForm.submit();
    }
}