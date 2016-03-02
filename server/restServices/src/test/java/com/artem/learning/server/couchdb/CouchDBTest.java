package com.artem.learning.server.couchdb;

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
    private Database database;

    @Test
    public void testCreateDeleteDB() {
        ConnectionInfo info = database.getConnectionInfo();
        assertNotNull(info);
        assertNotNull(info.getId());
        assertNotNull(info.getVersion());
        assertNotNull(info.getWelcomePhrase());

        try {
            DatabaseInfo databaseInfo = database.getDatabaseInfo();
            assertNull(databaseInfo);

            database.createDB();
            databaseInfo = database.getDatabaseInfo();
            assertNotNull(databaseInfo);
        } finally {
            database.deleteDB();
            assertNull(database.getDatabaseInfo());
        }
    }

    @Test
    public void testCRUDEntity() {
        try {
            DatabaseInfo db = database.createDB();
            assertNotNull(db);
            assertEquals(0, db.getDocumentCount());

            // 1. get user by a wrong id and get null
            User retrieved = database.getDocument("wrong-id", User.class);
            assertNull(retrieved);

            // 2. create user and store it
            User original = new User("user1@somemail.com", "Mr. Smith");
            UpdateResponse success = database.addDocument(original);
            assertTrue(success.isSuccess());
            String firstRevision = success.getRevision();
            assertEquals(1, database.getDatabaseInfo().getDocumentCount());

            // 2. get user by correct id and compare with original one
            retrieved = database.getDocument(success.getId(), User.class);
            assertEquals(original, retrieved);

            // 3. update user with revision
            original = new User("someOther@anothermail.com", "James Bond");
            success = database.updateDocument(success.getId(), success.getRevision(), original);
            assertTrue(success.isSuccess());
            retrieved = database.getDocument(success.getId(), User.class);
            assertEquals(original, retrieved);

            // 3.5 update user with wrong revision and fail
            original = new User("someOtherModified@anothermail.com", "Bond, James Bond");
            UpdateResponse fail = database.updateDocument(success.getId(), firstRevision, original);
            assertFalse(fail.isSuccess());
            assertTrue(fail.isConflict());

            // 4. delete user and make sure the document doesn't exist
            UpdateResponse deleted = database.deleteDocument(success.getId(), success.getRevision());
            assertTrue(deleted.isSuccess());
            assertNull(database.getDocument(success.getId(), User.class));
            // delete again and fail
            deleted = database.deleteDocument(success.getId(), success.getRevision());
            assertFalse(deleted.isSuccess());
        } finally {
            database.deleteDB();
        }
    }

    @Test
    public void testLookup() {
        try {
            // 1. create DB and view
            database.createDB();
            String designDocName = "users";
            String viewName = "by_login_id";
            database.createOrUpdateDesignDocument(designDocName, new ViewDefinition(viewName, "db/design/usersByLoginId.js", null));
            // do it again to make sure update works as well
            database.createOrUpdateDesignDocument(designDocName, new ViewDefinition(viewName, "db/design/usersByLoginId.js", null));

            // 2. fill DB with users
            int numUsers = 100;
            for (int i = 0; i < numUsers; i++) {
                database.addDocument(new User("usr" + i + "@mail.com", "User " + i));
            }

            // 2. lookup all users and check that the 3rd element is user 11 (alphabetical order)
            ViewResponse<User> res = database.findByKey(designDocName, viewName, User.class, null, null);
            assertEquals(numUsers, res.getRows().length);
            User user = res.getRows()[3].getValue();
            assertEquals("User 11", user.getDisplayName());

            // 3. lookup users from 50 and get 54: 50 - 99 + 6,7,8,9
            res = database.findByKey(designDocName, viewName, User.class, "usr50", null);
            assertEquals(54, res.getRows().length);

        } finally {
            database.deleteDB();
        }
    }

}
