package com.timon.web;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(value="Alert sse controller", description="Alert sse controller")
public class SSEController {
    

    @Autowired
    private AlertPusher pusher;
    public SSEController(AlertPusher pusher)
    {
        this.pusher = pusher;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperation(value = "Notify web client on new alert", response = ServerSentEvent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",
                    examples = @io.swagger.annotations.Example(
                    @ExampleProperty( value = "{'snapshot'：{'type': 'AAA'}}", mediaType = MediaType.TEXT_EVENT_STREAM_VALUE)))
    })
    public Flux<ServerSentEvent<String>> notify(@RequestHeader HttpHeaders headers)
    {
        headers.forEach((key, value) -> {
            log.info(String.format("Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
        });
        //log.info("New alert push client:{}", WebUtils.getClientIp(request));
        return pusher.get();
    }


}
