package com.timon;

import com.timon.alert.AlertProcessor;
import com.timon.common.MsgPreProcessor;
import com.timon.common.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class AlertDaemon implements CommandLineRunner {

    @Autowired
    MsgPreProcessor mp;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    AlertProcessor ap;


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AlertDaemon.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {

       ap.loadMetric("FD154430C49FD7-2019-06-19_104152+0800");
       //ap.shotMetric("FD154430C49FD7");

        // to do
        // 1: start redis
        // 2: read device metric config

        log.info("kafka consumer listener is starting...");

    }






}
