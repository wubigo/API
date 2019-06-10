package com.timon.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.timon.domain.Dev;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Setter
@Getter
@Component
public class MsgPreProcessor {

    @Value("${device.class.prefix}")
    private String device_class_prefix;

    @Value("${device.category}")
    private String device_category;

    /**
     * transform raw json message into device domain object
     * @param msg: raw json msg fetched from kafka
     */
    public void transform(String msg){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            final ObjectNode node = objectMapper.readValue(msg, ObjectNode.class);
            String type = node.get("nbiot_type").asText();
            if ( null == type || !device_category.contains(type) ) {
                log.error("Msg type received={} not config in category={}", type, device_category);
                return;
            }
            Dev b = objectMapper.readValue(msg, (Class<Dev>) Class.forName( device_class_prefix.concat(".").concat(type)));
            if ( Device.GROUP550.equals( type ) ) {
                log.info("Group550 msg:{}", b);
                //b.getCamera().stream().forEach( item -> log.debug( item.toString() ));
                //b.getMeeting().stream().forEach( item -> log.debug( item.toString() ));
            } else
                log.info("object={} sno={} type={}", b.toString(), b.getNbiot_sno(), b.getNbiot_type());
            // to do
            // rule engine device continues

        } catch ( Exception e) {
            log.error(e.getMessage());
        }
    }
}
