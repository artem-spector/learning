package com.artem.learning.server.db;

import com.artem.learning.server.ServerApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * TODO: Document!
 *
 * @author artem on 2/28/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServerApp.class)
@WebAppConfiguration
public class CouchDBTest {

    @Autowired
    private CouchDBConnection dbClient;

    @Test
    public void testCreateDeleteDB() {
        CouchDBInfo dbInfo = dbClient.getDBInfo();
        assertNotNull(dbInfo);

        String dbName = "testtest";
        CouchDB database = dbClient.getDB(dbName);
        assertNull(database);

        dbClient.createDB(dbName);
        database = dbClient.getDB(dbName);
        assertNotNull(database);

        dbClient.deleteDB(dbName);
        database = dbClient.getDB(dbName);
        assertNull(database);
    }


}
