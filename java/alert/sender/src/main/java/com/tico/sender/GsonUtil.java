package com.tico.sender;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GsonUtil {

    public static String replaceSz(String json, String key, String value){
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        jsonObject.addProperty(key, value);
        return jsonObject.toString();
    }
}
