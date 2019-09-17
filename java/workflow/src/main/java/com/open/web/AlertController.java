package com.open.web;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@Api(value = "Alert home controller")
public class AlertController {

    @Autowired
    RestTemplate restTemplate;

    /**
     * @param name
     * @return
     */
    @GetMapping(value = "/onboard")
    public WebResponse<String> onboard(@RequestParam(value = "name", defaultValue = "") String name) {

        WebResponse<String> response = new WebResponse<>();
        response.setData("Welcome onboard "+name);
        return response;
    }


    @PostMapping(value = "/call")
    public WebResponse<String> call(@RequestBody String ids) {
        DocumentContext jsonContext = JsonPath.parse(ids);
        String id = jsonContext.read("$.ids");
        // engine
        String url = "http://localhost:8080/flowable-rest/service/management/engine";
        url = "http://localhost:8080/flowable-rest/service/runtime/process-instances";
        url = "http://localhost:8080/flowable-rest/service/repository/process-definitions";

        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        log.info("result={}", result.getBody());
        WebResponse<String> response = new WebResponse<>();
        response.setData("Welcome onboard "+id +" & result="+result.getBody());
        return response;
    }

}

