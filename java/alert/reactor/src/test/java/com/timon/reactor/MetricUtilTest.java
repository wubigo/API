package com.timon.reactor;

import com.timon.alert.MetricRecord;
import com.timon.common.MetricUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MetricUtilTest {

    @Autowired
    MetricUtil metricUtil;


    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDate(){
        Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1);
        log.info("c={}", calendar.getTime());
    }

    @Test
    public void initMetric() {
        String sno="SNO11";
        List<MetricRecord> mrl = metricUtil.initMetric(sno);
        log.info("total metrics for {}:{}", sno, mrl.size());
        JsonUtil.write(sno, mrl);


    }

    @Test
    public  void initHdxmetric(){
        String sno="HDX7000_101";
        List<MetricRecord> mrl = metricUtil.initMetric(sno);
        JsonUtil.write(sno, mrl);
        //metricUtil.resetMetric(sno);
        metricUtil.loadMetric(sno, mrl);
        metricUtil.resetMetric(sno);


    }

    @Test
    public void contextLoads() {
    }
}