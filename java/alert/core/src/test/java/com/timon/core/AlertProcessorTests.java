package com.timon.core;


import com.timon.alert.AlertProcessor;
import com.timon.common.HttpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    public void testEvaluate(){
        String msg = null;
        try {
            msg = HttpUtil.readUrl("http://172.16.16.1/TiMon/core/raw/dev/avm/src/main/resources/rawData.json");
            ap.evaluate(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
