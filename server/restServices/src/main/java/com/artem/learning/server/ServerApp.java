package com.artem.learning.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO: Document!
 *
 * @author artem on 2/21/16.
 */

@SpringBootApplication(scanBasePackages = {"com.artem"})
public class ServerApp {

    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
    }
}
