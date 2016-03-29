function deleteStudent(name, id) {
    var doDelete = confirm("Are you sure to delete student " + name + "?");
    if (doDelete) {
        var form = document.getElementById('dummyForm');
        form.action = '/console/deleteStudent/' + id;
        form.submit();
    }
}

function assignCourse(studentId, courseId) {
    var form = document.getElementById('dummyForm');
    form.action = '/console/students/assign/' + studentId;
    document.getElementById('courseIdFormParam').value = courseId;
    form.submit();
}

function unassignCourse(studentId, courseId) {
    var form = document.getElementById('dummyForm');
    form.action = '/console/students/unassign/' + studentId;
    document.getElementById('courseIdFormParam').value = courseId;
    form.submit();
}