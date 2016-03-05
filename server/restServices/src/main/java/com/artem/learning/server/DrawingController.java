package com.artem.learning.server;

import com.artem.learning.server.couchdb.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: Document!
 *
 * @author artem on 2/22/16.
 */

@RestController
public class DrawingController {

    private static final Logger logger = LoggerFactory.getLogger(DrawingController.class);

    @Autowired
    private Database db;

    @RequestMapping(path = "/drawing", method = RequestMethod.POST)
    public String sendDrawing(@RequestParam("data") String drawingStr) {
        return "OK";
    }
}
