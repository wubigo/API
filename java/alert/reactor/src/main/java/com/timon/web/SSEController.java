package com.timon.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/alert")
public class SSEController {

    @Autowired
    private AlertPusher pusher;

//    @Autowired
//    ServerWebExchange exchange;

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> notify(@RequestHeader HttpHeaders headers, @RequestParam(value="location_id", defaultValue="") String location_id, ServerWebExchange exchange)
    {
        headers.forEach((key, value) -> {
            log.trace(String.format("Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
        });
        ServerHttpRequest request = exchange.getRequest();
        log.info("New alert push client:{}", request.getRemoteAddress());

        return pusher.get();
    }


    @ResponseStatus( code = HttpStatus.BAD_REQUEST, reason = "Some parameters are invalid")
    @GetMapping(value = "/status")
    public void status() {
        log.info("status code");
    }
}
