package com.timon;

import com.timon.common.HttpUtil;
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

    public static void main(String[] args) {


        SpringApplication app = new SpringApplication(AlertDaemon.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {

        redisUtil.set("hi","there");
        log.info("hi={}", redisUtil.get("hi"));

        // to do
        // 1: start redis
        // 2: read device metric config

//        try {
//            String s = HttpUtil.readUrl("http://172.16.16.1/TiMon/core/raw/dev/avm/src/main/resources/rawData.json");
//            mp.transform(s);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        log.info("kafka consumer listener is starting...");
        //poll.fetch();
    }






}
