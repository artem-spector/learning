package com.artem.learning.server.web;

import com.artem.learning.server.dao.StudentDao;
import com.artem.learning.server.model.DateTimeUtil;
import com.artem.learning.server.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * TODO: Document!
 *
 * @author artem on 3/19/16.
 */
@Controller
@RequestMapping(path = "/console")
public class WebAdminController {

    @Autowired
    private StudentDao studentDao;

    @RequestMapping(path = "/students", method = RequestMethod.GET)
    public String getStudents(Model model) {
        model.addAttribute("name", "Artem");
        model.addAttribute("students", studentDao.getAllStudents());
        return "studentsPage";
    }

    @RequestMapping(path = "/students", method = RequestMethod.POST)
    public String createStudent(@RequestParam String firstName, @RequestParam String lastName,
                                @RequestParam Student.Gender gender, @RequestParam String birthDate,
                                Model model) {
        studentDao.createStudent(firstName, lastName, parseDate(birthDate), gender);
        return getStudents(model);
    }

    @RequestMapping(path = "/students/{id}", method = RequestMethod.POST)
    public String updateStudent(@PathVariable String id,
                                @RequestParam String firstName, @RequestParam String lastName,
                                @RequestParam Student.Gender gender, @RequestParam String birthDate,
                                Model model) {
        Student student = studentDao.getStudent(id);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setBirthDate(parseDate(birthDate));
        student.setGender(gender);
        studentDao.updateStudent(student);
        return getStudents(model);
    }

    @RequestMapping(path = "/deleteStudent/{studentId}", method = RequestMethod.POST)
    public String deleteStudent(@PathVariable String studentId, Model model) {
        studentDao.deleteStudent(studentId);
        return getStudents(model);
    }

    private Date parseDate(String dateStr) {
        return DateTimeUtil.parseDateUTC(dateStr);
    }
}
