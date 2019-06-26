package com.timon.alert;

import com.timon.common.MsgPreProcessor;
import com.timon.domain.DevMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UpstreamConsumerListener {

    @Autowired
    private  MsgPreProcessor mp;
    @Autowired
    AlertProcessor ap;

    @KafkaListener(topics = "#{'${spring.kafka.topics}'.split(',')}",
            clientIdPrefix = "TiMon",
            groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> cr) {
        String payload = cr.value();
        log.info("offset={} topic={} payload={}", cr.offset(), cr.topic(), payload);
        ap.evaluate(payload);
    }

    void doWithDomain(String payload){
        DevMsg dm = mp.transform(payload);
        AlertRecord ar = ap.evaluateDomain(dm);
        if ( null != ar  )
            log.info("Alert detected:", ar);
    }
}