package com.timon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;

@Slf4j
//@SpringBootApplication
public class AlertCoreApplication {
// deploy standalone tomcat
//public class AlertCoreApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {

        //SpringApplication.run(AlertCoreApplication.class, args);
        //configureApplication(new SpringApplicationBuilder()).run(args);
    }


    //@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder in) {
        log.info("override configure...");
        return configureApplication(in);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        log.info("configureApplication...");

        //AlertProcessor.loadMetric("metric");
        return builder.sources(AlertCoreApplication.class).bannerMode(Banner.Mode.OFF);
    }

}
