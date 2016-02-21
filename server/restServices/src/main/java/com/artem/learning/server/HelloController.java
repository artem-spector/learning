package com.artem.learning.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: Document!
 *
 * @author artem on 2/21/16.
 */

@RestController
public class HelloController {

    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public String hello() {
        return  "hello";
    }
}
