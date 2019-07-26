package com.timon.web;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
public class AlertPusher {
    private final EmitterProcessor<ServerSentEvent<String>> emitter = EmitterProcessor.create();

    //The 3 seconds wait time between connections can be changed by the server.
    // To change it the server sends a retry: line together with the message.
    // The number after the colon specifies the number of milliseconds the browser
    // has to wait before he tries to reconnect
    Duration d = Duration.ofMillis(10000);

    public Flux<ServerSentEvent<String>> get()
    {
        Flux<ServerSentEvent<String>>  f = emitter.log();

            //The first event that the Subscriber will receive is through a call to onSubscribe
            //After the Publisher has sent as many items as were requested, the Subscriber can
            //call request() again to request more.
            Subscription s = new Subscription() {
                @Override
                public void request(long n) {
                    log.info("Subscriber is willing to accpt {} items", n);
                }

                @Override
                public void cancel() {
                    log.info("canceling the subscription");
                }
            };
            emitter.onSubscribe(s);

        return f;
    }

    public  void broadcast(String jsonAlert){

        try {
            emitter.onNext(ServerSentEvent.builder(jsonAlert).event("alert").retry(d).id(UUID.randomUUID().toString()).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            emitter.onError(e);
        }
    }
}
