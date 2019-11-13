package com.open.web;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;



@Slf4j
@RestController
public class EnrollFlowController {

    @Autowired
    RestTemplate restTemplate;

    /**
     * start process by key and set process variable
     * from the external form
     *
     * @param processKey
     * @param body
     * @return
     */
    @PostMapping(value = "/enroll/{key}")
    public String start(@PathVariable("key") String processKey, @RequestBody String body) {
        DocumentContext jsonContext = JsonPath.parse(body);
        String univ = jsonContext.read("$.univ");
        String name = jsonContext.read("$.name");
        String addr = jsonContext.read("$.addr");
        String id = jsonContext.read("$.ID");
        UUID uuid = UUID.randomUUID();
        String sn = uuid.toString().substring(0, 8);
        String url = "http://localhost:8080/flowable-task/process-api/runtime/process-instances";
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject var = new JSONObject();
        var.put("name", "univ");
        var.put("type", "string");
        var.put("value", univ);

        JSONArray ja = new JSONArray();
        ja.add(var);


        JSONObject o = new JSONObject();
        o.put("processDefinitionKey", processKey);
        o.put("businessKey", processKey);
        o.put("variables", ja);

        HttpEntity<String> request = new HttpEntity<String>(o.toJSONString(), headers);
        log.info("request={}", request);
        String result = restTemplate.postForObject(url, request, String.class);
        log.info("result={}", result);
        return result;
    }


    @PostMapping(value = "/tasks/{key}")
    public String doTask(@PathVariable("key") String taskKey, @RequestBody String body) {
        DocumentContext jsonContext = JsonPath.parse(body);
        String univ = jsonContext.read("$.univ");
        String name = jsonContext.read("$.name");
        String addr = jsonContext.read("$.addr");

        String url = "http://localhost:8080/flowable-task/process-api/runtime/tasks/";

        String t_url = url +"?taskDefinitionKey"+taskKey;
        log.info("t_url={}", t_url);
        String result = restTemplate.getForObject(url +"?taskDefinitionKey"+taskKey, String.class);
        log.info("result={}", result);
        jsonContext = JsonPath.parse(result);
        String id = jsonContext.read("$.data[0].id");
        log.info("task={}", id);
        if ( null != id ) {
            url += id;
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject var = new JSONObject();
            var.put("name", "name");
            var.put("type", "string");
            var.put("value", name);
            JSONArray ja = new JSONArray();
            ja.add(var);
            JSONObject o = new JSONObject();
            o.put("action", "complete");
            o.put("variables", ja);

            HttpEntity<String> request = new HttpEntity<String>(o.toJSONString(), headers);
            log.info("request={}", request);
            result = restTemplate.postForObject(url, request, String.class);
            log.info("result={}", result);
            if ( null == result )
                result = "";
            return result;
        }
        JSONObject o = new JSONObject();
        o.put("error", "no task");
        return o.toJSONString();

    }

}
