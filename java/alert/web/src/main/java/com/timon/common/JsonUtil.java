package com.timon.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import com.timon.domain.Alert;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Page;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    public static void write(String sno, Object o){
        ObjectMapper objectMapper = new ObjectMapper();
        String name = DataUtil.szDate(new Date().getTime(),"-yyyy-MM-dd_HHmmssZ");
        name="";
        File f = new File("target/" + sno + name +".json");
        try ( FileOutputStream fs = new FileOutputStream(f) ) {
            objectMapper.writeValue(fs, o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static Object read(String json, String jpath){
        DocumentContext dc  = getContext(json);
        return  read(dc, jpath);
    }

    public static DocumentContext getContext(String json){
        Configuration config = Configuration.defaultConfiguration()
                .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
                .addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext dc  = JsonPath.using(config).parse(json);
        return dc;
    }

    public  static Object read(DocumentContext dc, String jpath){
        Object value = dc.read("$."+jpath);
        return value;
    }


    public static <T> JSONObject pageObject(Page<T> pa){
        JSONObject obj = new JSONObject();

        List<T> alerts = pa.getContent();
        obj.put("total_page_count", pa.getTotalPages());
        obj.put("total", pa.getTotalElements());

        obj.put("result", alerts);
        return obj;
    }

    public static Alert[] readArray(String jsonAlert){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonAlert, Alert[].class);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;

    }






}
