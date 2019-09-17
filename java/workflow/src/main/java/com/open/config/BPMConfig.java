package com.open.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BPMConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        RestTemplate template = builder.build();
//        template.setMessageConverters(
//                Arrays.asList(
//                        new FormHttpMessageConverter(),
//                        new StringHttpMessageConverter()
//                )
//        );
//        template.getInterceptors().add(new BasicAuthorizationInterceptor("username", "password"));

        //return builder.basicAuthorization("rest-admin", "test").build();
        return builder.basicAuthorization("admin", "test").build();
    }
}
