package com.timon.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {
    public static String szDate(long date){
        return szDate(date, "yyyy-MM-dd_HH:mm:ssZ");
    }

    public static String szDate(long date, String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date d=new Date(date);
        return  simpleDateFormat.format(d);
    }
}
