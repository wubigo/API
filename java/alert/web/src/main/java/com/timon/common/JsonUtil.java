package com.timon.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.timon.model.Alert;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

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
