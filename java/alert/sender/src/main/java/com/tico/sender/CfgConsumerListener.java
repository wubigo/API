package com.tico.sender;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CfgConsumerListener {

    @KafkaListener(topics = "#{'${spring.kafka.topics}'.split(',')}",
            clientIdPrefix = "TiMon",
            groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> cr) {
        String payload = cr.value();
        cr.offset();
        log.info("offset={}  partition={} topic={} payload={}", cr.offset(), cr.partition(), cr.topic(), payload);

        if (null != payload) {
            log.info("config", payload);

        }

    }

}
