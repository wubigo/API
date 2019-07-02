package com.timon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
// public class AlertWebApplication {
// deploy standalone tomcat
public class AlertWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication.run(AlertWebApplication.class, args);

    }

}
