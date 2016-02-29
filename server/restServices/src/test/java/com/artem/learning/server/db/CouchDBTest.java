package com.artem.learning.server.db;

import com.artem.learning.server.ServerApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

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
        CouchDBConnectionInfo dbInfo = dbClient.getConnectionInfo();
        assertNotNull(dbInfo);
        assertNotNull(dbInfo.getId());
        assertNotNull(dbInfo.getVersion());
        assertNotNull(dbInfo.getWelcomePhrase());

        String dbName = "test_db";
        CouchDBInfo database;
        try {
            database = dbClient.getDB(dbName);
            assertNull(database);

            dbClient.createDB(dbName);
            database = dbClient.getDB(dbName);
            assertNotNull(database);
        } finally {
            dbClient.deleteDB(dbName);
            database = dbClient.getDB(dbName);
            assertNull(database);
        }
    }

    @Test
    public void testCRUDEntity() {
        String dbName = "test_users";
        try {
            CouchDBInfo db = dbClient.createDB(dbName);
            assertNotNull(db);
            assertEquals(0, db.getDocumentCount());

            // 1. get user by a wrong id and get null
            User retrieved = dbClient.getObject(dbName, "wrong-id", User.class);
            assertNull(retrieved);

            // 2. create user and store it
            User original = new User("user1@somemail.com", "Mr. Smith");
            UpdateResponse res = dbClient.addObject(dbName, original);
            assertTrue(res.isSuccess());
            assertEquals(1, dbClient.getDB(dbName).getDocumentCount());

            // 2. get user by correct id and compare with original one
            retrieved = dbClient.getObject(dbName, res.getId(), User.class);
            assertEquals(original, retrieved);

            // 3. update user
            original = new User("someOther@anothermail.com", "James Bond");
            res = dbClient.updateObject(dbName, res.getId(), res.getRevision(), original);
            assertTrue(res.isSuccess());
            retrieved = dbClient.getObject(dbName, res.getId(), User.class);
            assertEquals(original, retrieved);

            // 4. delete user and make sure the document doesn't exist
            dbClient.deleteObject(dbName, res.getId(), res.getRevision());
        } finally {
            dbClient.deleteDB(dbName);
        }
    }

}
