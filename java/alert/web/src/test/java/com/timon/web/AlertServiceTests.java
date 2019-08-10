package com.timon.web;


import com.timon.alert.model.Alert;
import com.timon.alert.service.AlertService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlertServiceTests {
    @Autowired
    AlertService as;
    @Test
    public void testInsert(){
        Alert  a = new Alert();
        int i = 3;
        a.setSno("sno"+i);
        a.setName("name"+i);
        a.setLocation_id("7d81a1a70bdd4d669b644106b1c5d14f");
        a.setLevel("");
        a.setTime(new Date().getTime());
        a.setDetail("detail"+i);
        a.setIsRead(0);
        as.insert(a);

    }
}
