package com.artem.learning.server.model;

import com.artem.learning.server.ServerApp;
import com.artem.learning.server.couchdb.UpdateDocumentResponse;
import com.artem.learning.server.dao.StudentDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * TODO: Document!
 *
 * @author artem on 3/17/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServerApp.class)
@WebAppConfiguration
public class StudentTest {

    @Autowired
    private StudentDao dao;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCreateStudent() throws Exception {
        // create and retrieve
        UpdateDocumentResponse res = dao.createStudent("Ayn", "Rand", DateTimeUtil.date(1905, Calendar.FEBRUARY, 2), Student.Gender.female);
        assertTrue(res.isSuccess());
        Student student = dao.getStudent(res.getId());
        assertEquals(res.getId(), student.getId());

        // serialize/deserialize
        String str = mapper.writeValueAsString(student);
        Student readValue = mapper.readValue(str, Student.class);
        assertEquals(student, readValue);
    }
}
