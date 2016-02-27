package com.artem.learning.server;

import com.artem.learning.server.db.FindResult;
import com.artem.learning.server.db.LearningDB;
import com.artem.learning.server.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * TODO: Document!
 *
 * @author artem
 *         Date: 2/27/16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServerApp.class)
@WebAppConfiguration
public class DBTest {

    @Autowired
    private LearningDB db;


    @Test
    public void testCreateUser() {
        String loginId = "aaa@bbb.com";
        Student created = new Student(loginId, "Aaaa", "Bbbbb", getCalendarDate(2000, 21, 2).getTime(), Student.Gender.male);
        db.createEntity(created);
        Student[] result = db.find("/_design/students/_view/studentsByLoginId", "someone-else", Student.class);
        assertEquals(0, result.length);

        result = db.find("/_design/students/_view/studentsByLoginId", loginId, Student.class);
        assertTrue(result.length > 0);
    }

    private Calendar getCalendarDate(int year, int month, int date) {
        Calendar res = Calendar.getInstance();
        res.set(year, month, date);
        return res;
    }
}
