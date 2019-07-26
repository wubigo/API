package com.timon.core;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.timon.common.HttpUtil;
import com.timon.common.MsgPreProcessor;
import com.timon.domain.DevMsg;
import com.timon.domain.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MsgPreProcessorTests {
    @Autowired
    MsgPreProcessor mpp;

    @Test
    public void testTransform(){
        String msg = null;
        try {
            msg = HttpUtil.readUrl("http://172.16.16.1/TiMon/core/raw/dev/avm/src/main/resources/rawData.json");

            mpp.transform(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
