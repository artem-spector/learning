package com.artem.learning.server.couchdb;

import com.artem.TestApplication;
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
@SpringApplicationConfiguration(classes = TestApplication.class)
@WebAppConfiguration
public class CouchDBTest {

    @Autowired
    private Database database;

    @Test
    public void testCRUDEntity() {
        DatabaseInfo db = database.getDatabaseInfo();
        assertNotNull(db);

        // 1. get user by a wrong id and get null
        User retrieved = database.getDocument("wrong-id", User.class);
        assertNull(retrieved);

        // 2. create user and store it
        int documentCount = database.getDatabaseInfo().getDocumentCount();
        User user = new User("user1@somemail.com", "Mr. Smith");
        UpdateDocumentResponse success = database.addDocument(user);
        assertTrue(success.isSuccess());
        assertEquals(1, database.getDatabaseInfo().getDocumentCount() - documentCount);

        // 2. get user by correct id and compare with original one
        retrieved = database.getDocument(success.getId(), User.class);
        assertEquals(user, retrieved);

        // 3. update user with revision
        user.setLoginId("someOther@anothermail.com");
        user.setDisplayName("James Bond");
        success = database.updateDocument(user);
        assertTrue(success.isSuccess());
        retrieved = database.getDocument(success.getId(), User.class);
        assertEquals(user, retrieved);

        // 4. delete user and make sure the document doesn't exist
        UpdateDocumentResponse deleted = database.deleteDocument(user);
        assertTrue(deleted.isSuccess());
        assertNull(database.getDocument(success.getId(), User.class));
        // delete again and fail
        deleted = database.deleteDocument(user);
        assertFalse(deleted.isSuccess());
    }

    @Test
    public void testLookup() {
        // 1. create view
        String designDocName = "users";
        String viewName = "by_login_id";
        DesignDocument doc = new DesignDocument(designDocName, "javascript",
                new ViewDefinition(viewName, "db/design/usersByLoginId.js", null));
        UpdateDocumentResponse response = database.updateDocument(doc);
        assertTrue(response.isSuccess());
        // do it again to make sure update works as well
        response = database.updateDocument(doc);
        assertTrue(response.isSuccess());

        // 2. fill DB with users
        int numUsers = 100;
        for (int i = 0; i < numUsers; i++) {
            database.addDocument(new User("usr" + i + "@mail.com", "User " + i));
        }

        // 2. lookup all users and check that the 3rd element is user 11 (alphabetical order)
        View<User> usersByLoginId = database.getView(designDocName, viewName, User.class);
        LookupViewResponse<User> res = usersByLoginId.lookup(null, null);
        assertEquals(numUsers, res.getRows().length);
        User user = res.getRows()[3].getValue();
        assertEquals("User 11", user.getDisplayName());

        // 3. lookup users from 50 and get 54: 50 - 99 + 6,7,8,9
        res = usersByLoginId.lookup("usr50", null);
        assertEquals(54, res.getRows().length);

        // 4. lookup user 50, update it, and lookup again
        ViewRow<User> row = findUser(usersByLoginId, "usr50@mail.com");
        user = row.getValue();
        assertEquals("User 50", user.getDisplayName());

        user.setDisplayName("Bond");
        response = database.updateDocument(user);
        assertTrue(response.isSuccess());

        row = findUser(usersByLoginId, "usr50@mail.com");
        user = row.getValue();
        assertEquals("Bond", user.getDisplayName());
    }

    private ViewRow<User> findUser(View<User> view, String key) {
        LookupViewResponse<User> res = view.lookup(key, key);
        assertEquals(1, res.getRows().length);
        return res.getRows()[0];
    }
}
