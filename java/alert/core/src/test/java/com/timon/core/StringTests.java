package com.timon.core;

import com.google.common.base.Enums;
import com.timon.common.DataUtil;
import com.timon.common.Device;
import com.timon.common.Level;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Slf4j
public class StringTests {
    @Test
    public void testFormat(){

        Device dd =  Device.valueOf("Hdx7000");


        Level ol = Enums.getIfPresent(Level.class, "big").orNull();
        log.info("l={}", ol );



        String s = String.format("hello {} %s", "wu");
        log.info(s);
//        for ( int i=1; i< 25; i++) {
//            DataUtil.dateByHour(i);
//        }
        String d = DataUtil.szDate(1564020032867L);
        log.info("d={}", d);
    }
}
