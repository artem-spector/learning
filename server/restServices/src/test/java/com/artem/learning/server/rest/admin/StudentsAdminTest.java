package com.artem.learning.server.rest.admin;

import com.artem.learning.server.ServerApp;
import com.artem.learning.server.couchdb.Database;
import com.artem.learning.server.model.Student;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO: Document!
 *
 * @author artem on 3/9/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServerApp.class)
@WebAppConfiguration
public class StudentsAdminTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Database db;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void studentsCRUDTest() throws Exception {
        mvc.perform(post(StudentsAdminController.ADMIN_STUDENTS_PATH)
                .param("firstName", "James")
                .param("lastName", "Bond")
                .param("birthDate", "1900-01-04 UTC")
                .param("gender", Student.Gender.male.name()))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get(StudentsAdminController.ADMIN_STUDENTS_PATH).accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }
}
