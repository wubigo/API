package com.timon.msg;

import com.timon.common.JsonUtil;
import com.timon.web.AlertEmitter;
import com.timon.web.AlertPusher;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;



@Slf4j
@Service
public class AlertConsumerListener {


    @Autowired
    AlertEmitter pusher;


    @KafkaListener(topics = "#{'${spring.kafka.topics}'.split(',')}",
            clientIdPrefix = "TiMon",
            groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> cr) {
        String payload = cr.value();
        cr.offset();
        log.info("offset={}  partition={} topic={} payload={}", cr.offset(), cr.partition(), cr.topic(), payload);

        if (null != payload) {
            log.info("broadcast alert={}", payload);
            pusher.broadcast(payload);

        }

    }

}
