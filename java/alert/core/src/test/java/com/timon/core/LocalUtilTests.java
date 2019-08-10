package com.timon.core;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.timon.alert.AlertProcessor;
import com.timon.alert.AlertRecord;
import com.timon.alert.AlertRuleEngine;
import com.timon.alert.MetricRecord;
import com.timon.alert.model.Alert;
import com.timon.common.HttpUtil;
import com.timon.common.JsonUtil;
import com.timon.common.LocalUtil;
import com.timon.common.RedisUtil;
import com.timon.domain.DevMsg;
import com.timon.domain.Group550;
import com.timon.domain.Location;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalUtilTests {

    @Autowired
    AlertRuleEngine engine;

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    LocalUtil localUtil;


    @Test
    public void testJsonPathArray(){
        String msg = null;
        try {
            msg = HttpUtil.readUrl("http://172.16.16.1/TiMon/core/raw/dev/avm/src/main/resources/rawData.json");


            List<Integer>  locals = JsonPath.read(msg, "$.location[*].id");
            for ( Integer l : locals)
                log.info("l={}", l);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testResetLocal(){
        String msg = null;
        try {
            msg = HttpUtil.readUrl("http://172.16.16.1/TiMon/core/raw/dev/avm/src/main/resources/rawData.json");

            List<MetricRecord> ml = (List<MetricRecord>)redisUtil.lGet("METRIC:Group550", 0, -1);
            if ( null == ml || ml.size() ==0 )
                return;
            List<Alert> al = new ArrayList<Alert>();
            for ( MetricRecord mr : ml ) {
                AlertRecord ar = engine.run(msg, mr);
                if (null != ar) {
                    List<Location> locations = ar.getHeader().getLocations();
                    localUtil.resetLocal(locations);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
