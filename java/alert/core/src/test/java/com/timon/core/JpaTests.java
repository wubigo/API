package com.timon.core;


import com.timon.alert.model.Alert;
import com.timon.alert.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaTests {

    @Autowired
    AlertService as;

    @Test
    public void testAlertCreatedAt(){
        Alert a = new Alert();
        a = as.insert(a);
        log.info("created_at={}", a.getCreatedAt());
        Optional<Alert> opt= as.findById(16110146L);
        if ( opt.isPresent()) {
            a = opt.get();
            log.info("created_at={}", a.getCreatedAt());

        }
    }

    @Test
    public void test24H(){
        log.info("begin");
        as.find24HUnReadByLevel("critical");
        log.info("end");
    }

}
