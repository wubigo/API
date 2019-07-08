package com.timon.alert;

import com.timon.alert.model.Alert;
import com.timon.alert.repository.AlertRepository;
import com.timon.common.JsonUtil;
import com.timon.common.MsgPreProcessor;
import com.timon.domain.DevMsg;
import com.timon.web.AlertPusher;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class UpstreamConsumerListener {

    @Autowired
    private  MsgPreProcessor mp;
    @Autowired
    AlertProcessor ap;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value("${spring.kafka.producer.topic}")
    String alert_dump_topic;
    @Autowired
    AlertPusher pusher;
    @Autowired
    AlertRepository ar;

    @KafkaListener(topics = "#{'${spring.kafka.topics}'.split(',')}",
            clientIdPrefix = "TiMon",
            groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> cr) {
        String payload = cr.value();
        cr.offset();
        log.trace("offset={}  partition={} topic={} payload={}", cr.offset(), cr.partition(), cr.topic(), payload);
        List<Alert> al = ap.evaluate(payload);
        if ( null != al && al.size() > 0  ) {
            String jsonAlert = JsonUtil.toJson(al);
            if ( null != jsonAlert ) {
                log.info("broadcast alert={}", jsonAlert);
                pusher.broadcast(jsonAlert);
                ar.saveAll(al);
                dumpAlert(jsonAlert);
            }
        }
    }

    void dumpAlert(String jsonAlert){
        log.info("dump alert to topic {}:{}", alert_dump_topic, jsonAlert);
        this.kafkaTemplate.send(alert_dump_topic, jsonAlert);
    }

    void doWithDomain(String payload){
        DevMsg dm = mp.transform(payload);
        AlertRecord ar = ap.evaluateDomain(dm);
        if ( null != ar  )
            log.info("Alert detected:", ar);
    }
}
