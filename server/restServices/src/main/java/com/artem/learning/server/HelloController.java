package com.artem.learning.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

/**
 * TODO: Document!
 *
 * @author artem on 2/21/16.
 */

@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public Callable<String> hello() {
        logger.info("entering hello");
        return  new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000);
                logger.info("returning value");
                return "hello";
            }
        };
    }
}
