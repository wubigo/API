package com.timon.common;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;




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
    public void initMetric() {

        metricUtil.initMetric("SNO11");
    }


    @Test
    public void contextLoads() {
    }
}