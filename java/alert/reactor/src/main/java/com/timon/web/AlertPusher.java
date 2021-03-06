package com.timon.web;

import com.timon.alert.MetricRecord;
import com.timon.common.JsonUtil;
import com.timon.common.MetricUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class AlertPusher {
    @Autowired
    MetricUtil metricUtil;
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
//            Subscription s = new Subscription() {
//                @Override
//                public void request(long n) {
//                    log.info("Subscriber is willing to accpt {} items", n);
//                }
//
//                @Override
//                public void cancel() {
//                    log.info("canceling the subscription");
//                }
//            };
//            emitter.onSubscribe(s);

        return f;
    }

    @KafkaListener(topics = "#{'${spring.kafka.topics}'.split(',')}",
            clientIdPrefix = "TiMon",
            groupId = "${spring.kafka.consumer.group-id}")
    public  void broadcast(ConsumerRecord<String, String> cr){

        String topic = cr.topic();
        String payload = cr.value();
        if ( topic.equals("config") ){
            log.info("config payload={}", payload);
            List<MetricRecord> mrl = metricUtil.initMetric(payload);
            JsonUtil.write(payload, mrl);
            return;
        }
        String jsonAlert = payload;
        try {
            emitter.onNext(ServerSentEvent.builder(jsonAlert).event("alert").retry(d).id(UUID.randomUUID().toString()).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            emitter.onError(e);
        }
    }
}
