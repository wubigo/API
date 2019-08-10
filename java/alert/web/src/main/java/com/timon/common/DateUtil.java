package com.timon.common;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date lastDay() {
        Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }
}
