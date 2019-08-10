package com.timon.core;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.timon.alert.AlertHeader;
import com.timon.alert.AlertProcessor;
import com.timon.common.HttpUtil;
import com.timon.common.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AlertProcessorTests {
    @Autowired
    AlertProcessor ap;

    @Test
    public void testLoadMetric(){

        ap.loadMetric("metric");
    }



    @Test
    public void testPath(){
        String msg = null;
        try {
            msg = HttpUtil.readUrl("http://172.16.16.1/TiMon/core/raw/dev/avm/src/main/resources/huawei交换机.json");
            //ap.evaluate(msg);
            DocumentContext dc = JsonPath.parse(msg);
            int a = dc.read("$.bandwidth['GigabitEthernet0/0/28'][0]");

            int result = Math.round(a*8/60/1024);

            a = dc.read("$.bandwidth.GigabitEthernet0/0/28.[0]");

            result = Math.round(a*8/60/1024);
            //int b = dc.read("$.bandwidth['GigabitEthernet1/0/1'][1]");


            log.info("a={} result={} ", a,  result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEvaluate(){
        String msg = null;
        try {
            msg = HttpUtil.readUrl("http://172.16.16.1/TiMon/core/raw/dev/avm/src/main/resources/rawData.json");
            AlertHeader header = (AlertHeader) JsonUtil.fromMap(msg);
            //ap.evaluate(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
