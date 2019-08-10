package com.timon.oauth2_res;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResController {

    @GetMapping(value = "res")
    protected String protect(){

        return "hello";
    }
}
