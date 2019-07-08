package com.timon.web;

import com.timon.alert.AlertRecord;
import com.timon.alert.model.Alert;
import com.timon.common.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class AlertPusher {
    private final EmitterProcessor<ServerSentEvent<String>> emitter = EmitterProcessor.create();

    public Flux<ServerSentEvent<String>> get()
    {
        Flux<ServerSentEvent<String>>  f = emitter.log();
        return f;
    }

    public  void broadcast(String jsonAlert){

        try {
            emitter.onNext(ServerSentEvent.builder(jsonAlert).event("alert").id(UUID.randomUUID().toString()).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            emitter.onError(e);
        }
    }
}
