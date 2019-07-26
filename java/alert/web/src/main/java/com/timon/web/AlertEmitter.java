package com.timon.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AlertEmitter {

    List<SseEmitter> emitters = new ArrayList<SseEmitter>();

    public synchronized void addClint(SseEmitter client){
        emitters.add(client);
        log.info("{} sse clients", emitters.size() );
    }

    public void broadcast(String alert) {

        for (SseEmitter e : emitters) {
            try{
                e.send(alert);
            } catch(IOException ie){
                log.error(ie.getMessage());
                emitters.remove(e);
            }
        }
    }
}
