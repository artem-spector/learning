package com.artem.learning.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * TODO: Document!
 *
 * @author artem on 2/21/16.
 */

@SpringBootApplication
public class ServerApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ServerApp.class, args);
    }
}
