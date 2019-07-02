package com.timon.alert.path;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.timon.common.DataUtil;
import com.timon.common.HttpUtil;

import com.timon.common.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.Date;

@Slf4j
public class JsonTests {

    @Test
    public void testPath(){
        try {
            String content = HttpUtil.readUrl("http://172.16.16.1/TiMon/core/raw/dev/avm/src/main/resources/rawData.json");
            DocumentContext dc  = JsonPath.parse(content);
            Object camera_1_connected = dc.read("$.camera[1].connected");

            log.info("camera_1_connected={}", camera_1_connected);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testp1(){
        log.info(MessageFormat.format("视频接收出现抖动{0}毫秒{1}", 35));
        log.info("date:{}", DataUtil.szDate(new Date().getTime()));
        log.info("date:{}", DataUtil.szDate(1511658000));

        try {
            String content = HttpUtil.readUrl("http://172.16.16.1/TiMon/doc/raw/dev/static/device.json");
            log.info("id={}", JsonUtil.getContext(content).read("data.device.id").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
