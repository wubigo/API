package com.timon.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.timon.alert.AlertHeader;
import com.timon.alert.MetricRecord;
import com.timon.alert.model.Alert;
import com.timon.domain.DevMsg;
import com.timon.domain.Location;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.jayway.jsonpath.Option.ALWAYS_RETURN_LIST;
import static com.jayway.jsonpath.Option.SUPPRESS_EXCEPTIONS;

@Slf4j
public class JsonUtil {


    public static String toJson(Object o){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  static Object read(String json, String jpath){
        DocumentContext dc  = getContext(json);
        return  dc.read(jpath);
    }



    public static DocumentContext getContext(String json){
        Configuration config = Configuration.defaultConfiguration()
                .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
                .addOptions(SUPPRESS_EXCEPTIONS);
        DocumentContext dc  = JsonPath.using(config).parse(json);
        return dc;
    }

    public  static void read2List(String json, String jpath){

        Location[] l = JsonPath.using(Configuration.
                defaultConfiguration().
                addOptions(
                        ALWAYS_RETURN_LIST,
                        SUPPRESS_EXCEPTIONS)).parse(json).read("$."+jpath);
        //log.info("l={}", l);


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
        File f = new File("src/main/resources/"+file+".json");
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
