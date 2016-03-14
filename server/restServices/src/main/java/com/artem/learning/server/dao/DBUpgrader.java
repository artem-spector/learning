package com.artem.learning.server.dao;

import com.artem.learning.server.couchdb.Database;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TODO: Document!
 *
 * @author artem on 3/12/16.
 */
@Component
public class DBUpgrader implements InitializingBean{

    public static final String DROP_CREATE = "drop-create";
    public static final String UPDATE_DESIGN = "update-design";

    @Autowired
    private Database db;

    @Value("${db.upgrade.mode}")
    private String mode;

    @Override
    public void afterPropertiesSet() throws Exception {
        switch (mode) {
            case DROP_CREATE:
                if (db.getDatabaseInfo() != null)
                    db.deleteDB();
                // fall through
            case UPDATE_DESIGN:
                if (db.getDatabaseInfo() == null) {
                    db.createDB();
                }
                db.updateDocument(StudentDesignHelper.getDesignDocument());
                db.updateDocument(LessonDesignHelper.getDesignDocument());
                break;
            default:
                throw new Exception("Unsupported DB upgrade mode: '" + mode + "'");

        }
    }

}
