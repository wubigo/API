package com.timon.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.timon.alert.AlertHeader;
import com.timon.alert.MetricRecord;
import com.timon.domain.DevMsg;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
public class JsonUtil {

    public  static Object read(String json, String jpath){
        DocumentContext dc  = getContext(json);
        return  read(dc, jpath);
    }

    public static DocumentContext getContext(String json){
        DocumentContext dc  = JsonPath.parse(json);
        return dc;
    }

    public  static Object read(DocumentContext dc, String jpath){
        Object value = dc.read("$."+jpath);
        return value;
    }

    public static Object fromMap(String json){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, AlertHeader.class);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static MetricRecord[] readArray(String file){
        File f = new File("target/"+file+".json");
        try ( FileInputStream fs = new FileInputStream(f) ) {

            return (MetricRecord[])new ObjectMapper().readValue(fs, MetricRecord[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    public static List<MetricRecord> readList(String file){
        File f = new File("target/"+file+".json");
        try ( FileInputStream fs = new FileInputStream(f) ) {

            return new ObjectMapper().readValue(fs, new TypeReference<List<MetricRecord>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static void write(String sno, Object o){
        ObjectMapper objectMapper = new ObjectMapper();
        String name = DataUtil.szDate(new Date().getTime(),"-yyyy-MM-dd_HHmmssZ");
        File f = new File("target/" + sno + name +".json");
        try ( FileOutputStream fs = new FileOutputStream(f) ) {
            objectMapper.writeValue(fs, o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
