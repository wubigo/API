package com.timon.common;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Slf4j
public class HttpUtil {

    @NonNull
    public static String readUrl(String urlString) throws Exception{
        URL url = new URL(urlString);
        try ( BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())) ){
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        } catch ( Exception e){
            log.error(e.getMessage());
        }
        return "";
    }
}
