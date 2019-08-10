package com.timon.common;

import com.timon.alert.MetricRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


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

        List<MetricRecord> mrl = metricUtil.initMetric("SNO11");
        JsonUtil.write("SNO11", mrl);
    }

    @Test
    public void contextLoads() {
    }
}