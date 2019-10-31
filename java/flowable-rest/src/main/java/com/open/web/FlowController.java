package com.open.web;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RestController
public class FlowController {

    @Autowired
    private Environment env;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping(value = "quota")
    public int hasQuota(@RequestBody String body){
        DocumentContext jsonContext = JsonPath.parse(body);
        String univ = jsonContext.read("$.univ");
        log.info("univ={}", univ);
        log.info("univ={} is selected", univ);
        String quota = env.getProperty("quota.univ."+univ);
        log.info("and the  quota={}", quota);
        return Integer.valueOf(quota);
    }

    /**
     * @param name
     * @return
     */
    @GetMapping(value = "/board")
    public WebResponse<String> board(@RequestParam(value = "name", defaultValue = "") String name) {

        WebResponse<String> response = new WebResponse<>();
        log.info("name={}", name);
        response.setData("Welcome onboard "+name);
        return response;
    }

    @PostMapping(value = "/onboard")
    public WebResponse<String> onboard(@RequestBody String body) {
        DocumentContext jsonContext = JsonPath.parse(body);
        String name = jsonContext.read("$.name");
        log.info("name={}", name);
        WebResponse<String> response = new WebResponse<>();
        response.setData("Welcome onboard "+name);
        return response;
    }

    @GetMapping(value = "/hi")
    public WebResponse<String> sayHi(@RequestParam(value = "name", defaultValue = "") String name) {

        log.info("name={}", name);
        WebResponse<String> response = new WebResponse<>();
        response.setData("Welcome onboard "+name);
        return response;
    }

    @GetMapping(value = "/")
    public WebResponse<String> index() {


        WebResponse<String> response = new WebResponse<>();
        response.setData("Welcome home");
        return response;
    }



    @GetMapping(value = "/status")
    public ResponseEntity<String> status() {


        WebResponse<String> response = new WebResponse<>();
        response.setData("Welcome home");
        return null;
    }

    /**
     * start process by key and set process variable
     * from the external form
     * @param processKey
     * @param body
     * @return
     */
    @PostMapping(value = "/processes/{key}")
    public String start(@PathVariable("key") String processKey, @RequestBody String body) {
        DocumentContext jsonContext = JsonPath.parse(body);
        String name = jsonContext.read("$.name");
        String addr = jsonContext.read("$.addr");
        String id = jsonContext.read("$.ID");
        UUID uuid = UUID.randomUUID();
        String sn = uuid.toString().substring(0,8);
        log.info("欢迎新同学{} 你的详细信息：地址={} 身份证={} 报名编号={}", name, addr, id, sn);
        String url = "http://localhost:8080/flowable-task/process-api/runtime/process-instances";
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject var = new JSONObject();
        var.put("name", "name");
        var.put("type", "string");
        var.put("value", name);
//        var.put("addr", addr);
//        var.put("ID", id);
//        var.put("sn", sn);
        JSONArray ja = new JSONArray();
        ja.add(var);

        JSONObject o  = new JSONObject();
        o.put("processDefinitionKey", processKey);
        o.put("businessKey", processKey);
        o.put("variables", ja);

        HttpEntity<String> request = new HttpEntity<String>(o.toJSONString(), headers);
        log.info("request={}", request);
        String result = restTemplate.postForObject(url, request, String.class);
        log.info("result={}", result);
        return result;
    }




    @PostMapping(value = "/call")
    public WebResponse<String> call(@RequestBody String ids) {
        DocumentContext jsonContext = JsonPath.parse(ids);
        String id = jsonContext.read("$.ids");
        // engine
        String url = "http://localhost:8080/flowable-rest/service/management/engine";
        url = "http://localhost:8080/flowable-rest/service/repository/process-definitions";

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        log.info("result={}", result.getBody());
        WebResponse<String> response = new WebResponse<>();
        response.setData("Welcome onboard "+id +" & result="+result.getBody());
        return response;
    }


    @PostMapping(value = "/deploys")
    public WebResponse<String> deploy(@RequestBody String ids) {
        DocumentContext jsonContext = JsonPath.parse(ids);
        String id = jsonContext.read("$.ids");

        final HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        final HttpEntity<String> entity = new HttpEntity<String>(headers);
        // engine
        String url = "http://localhost:8080/flowable-rest/service/management/engine";

        //an api that’s available over form auth authentication
        url = "http://localhost:8080/flowable-task/app/rest/runtime/app-definitions";

        //basic auth authentication
        url = "http://localhost:8080/flowable-task/process-api/repository/deployments";
        url = "http://localhost:8080/flowable-task/process-api/repository/process-definitions";
        url = "http://admin:test@localhost:8080/flowable-task/process-api/runtime/process-instances";
        url= "http://admin:test@localhost:8080/flowable-task/process-api/runtime/tasks";
        url = "http://admin:test@localhost:8080/flowable-task/actuactor/mappings";
        url = "http://admin:test@localhost:8080/flowable-task/actuator/mappings";

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        log.info("result={}", result.getBody());
        WebResponse<String> response = new WebResponse<>();
        response.setData(result.getBody());
        return response;
    }



}

