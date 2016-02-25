package com.artem.learning.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @RequestMapping(path = "/drawing", method = RequestMethod.POST)
    public String sendDrawing(@RequestParam("data") String drawingStr) {
        logger.debug("got drawing:\n" + drawingStr);
        return "OK";
    }
}
