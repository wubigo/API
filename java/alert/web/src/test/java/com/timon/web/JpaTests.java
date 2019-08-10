package com.timon.web;


import com.google.gson.Gson;
import com.timon.alert.model.Alert;
import com.timon.alert.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaTests {

    @Autowired
    AlertService as;


    @Test
    public  void testJustToday(){
        Page<Alert> p = as.findJustToday(0,5);
        int lastPage = p.getTotalPages();
        //p =  as.findJustToday(lastPage-1, 5);


        p = as.findTodayUnRead(0, 0,100);
        for ( Alert a : p ){
            log.info("a={}", a);
        }
    }

    @Test
    public void testAlertCreatedAt(){
        Alert a = new Alert();
        a = as.insert(a);
        log.info("created_at={}", a.getCreatedAt());
        Optional<Alert> opt= as.findById(16110146);
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

    @Test
    public void testTrend24H(){
        as.trend24H();
    }

    @Test
    public void testOverview(){
        List<Object[]> levelCount = (List<Object[]>)as.overview();


        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        for ( Object[] o : levelCount ){
            obj.put("level", o[0]);
            obj.put("count", o[1]);
            array.add(obj);
        }

        log.info("lc={}", array.toJSONString());

    }


    @Test
    public void testLevel(){
        List<Alert> ls = as.findAllByLevel("major");
        log.info("ls lenght={}", ls.size());

    }


    @Test
    public void testUnread(){
        Page<Alert> ls = as.findAllUnRead(0,2, 20);
        //List<Alert> ls = as.findAllUnRead();
        log.info("page ={}", ls.getTotalPages());
    }

    @Test
    public  void testPages(){
        Page<Alert> pa = as.findTodayInPage();

        int pages = pa.getTotalPages();
        log.info("pages={}", pages);

        Pageable pb = pa.getPageable();

        List<Alert> la = pa.getContent();

        log.info("records={}", la.size());
    }

}
