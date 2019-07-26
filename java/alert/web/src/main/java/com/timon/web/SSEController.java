package com.timon.web;

import io.swagger.annotations.*;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(value="Alert sse controller", description="Alert sse controller")
public class SSEController {
    

    @Autowired
    private AlertEmitter clients;

    //@Autowired
    //ServerWebExchange exchange;


    @CrossOrigin(origins = "*")
    @GetMapping(value = "/no_sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperation(value = "Notify web client on new alert", response = ServerSentEvent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",
                    examples = @io.swagger.annotations.Example(
                    @ExampleProperty( value = "{'snapshot'：{'type': 'AAA'}}", mediaType = MediaType.TEXT_EVENT_STREAM_VALUE)))
    })
    public SseEmitter  notify(@RequestHeader HttpHeaders headers,  @RequestParam(value="location_id", defaultValue="") String location_id)
    {
        SseEmitter s = new SseEmitter();
        clients.addClint(s);
        return s;
    }


}
