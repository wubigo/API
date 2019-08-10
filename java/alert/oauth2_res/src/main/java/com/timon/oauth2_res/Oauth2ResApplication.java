package com.timon.oauth2_res;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
public class Oauth2ResApplication {

//    @Bean
//    public Filter userContextFilter() {
//        UserContextFilter userContextFilter = new UserContextFilter();
//        return userContextFilter;
//    }

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ResApplication.class, args);
    }

}
