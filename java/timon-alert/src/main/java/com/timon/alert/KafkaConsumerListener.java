package com.timon.alert;

import com.timon.common.MsgPreProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class KafkaConsumerListener {

    @Value("${spring.kafka.topic.alert-raw}")
    private  String TOPIC ;
    @Value("${spring.kafka.consumer.group-id}")
    private  String GROUP;
    @Autowired
    private  MsgPreProcessor mp;

    @KafkaListener(topics = "${spring.kafka.topic.alert-raw}",
            clientIdPrefix = "TiMon",
            groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> cr) {
        String payload = cr.value();
        log.debug("offset={} topic={}", cr.offset(), cr.topic());
        mp.transform(payload);
    }
}
