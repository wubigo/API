package com.timon.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// note - this is a spring-boot controller, not @RestController
@Controller
public class OpenAPIController {

    @RequestMapping("/openapi")
    public String home() {
        return "redirect:swagger-ui.html";
    }
}


