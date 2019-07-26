package com.timon.core;


import com.timon.alert.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AlertServiceTests {

    @Autowired
    AlertService as;
    @Test
    public void testOverview(){

        List<?> o = as.overview();
        for ( Object o1 : o) {
            log.info( "o1:{}", o1);
        }
        log.info("");
    }
}

