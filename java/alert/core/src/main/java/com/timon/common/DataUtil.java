package com.timon.common;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DataUtil {
    public static String szDate(long date){
        return szDate(date, "yyyy-MM-dd_HH:mm:ssZ");
    }

    public static String szDate(long date, String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date d=new Date(date);
        return  simpleDateFormat.format(d);
    }

    public  static long dateByHour(int hourBack){
        Calendar cal = Calendar.getInstance();
        // remove next line if you're always using the current time.
        // cal.setTime(currentDate);
        cal.add(Calendar.HOUR, -hourBack);
        Date oneHourBack = cal.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH");
        log.info("date={} long={}", simpleDateFormat.format(oneHourBack), oneHourBack.getTime());
        return oneHourBack.getTime();
    }

}
