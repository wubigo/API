package com.timon.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/alert")
public class AlertHistoryController {



    @CrossOrigin(origins = "*")
    @GetMapping(value = "/history")
    public void query(@RequestHeader HttpHeaders headers)
    {


    }

}
