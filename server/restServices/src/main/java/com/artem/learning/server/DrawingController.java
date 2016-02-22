package com.artem.learning.server;

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

    @RequestMapping(path = "/drawing", method = RequestMethod.POST)
    public String sendDrawing(@RequestParam("data") String drawingStr) {
        return "OK";
    }
}
