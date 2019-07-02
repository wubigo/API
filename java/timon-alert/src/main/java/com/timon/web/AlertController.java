package com.timon.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/history")
public class AlertController {

    @GetMapping(value  = "/day")
    public String queryByDay(@RequestParam(value="name", defaultValue="World") String name) {
        log.info("trace...");
        return "hello queryByDay";
    }

    @GetMapping(value  = "/stat")
    public String stat(@RequestParam(value="name", defaultValue="World") String name) {
        log.info("trace...");
        return "hello stat";
    }



}
