package com.timon.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.timon.domain.DevMsg;
import com.timon.domain.Group550;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    public DevMsg transform(String msg){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            final ObjectNode node = objectMapper.readValue(msg, ObjectNode.class);
            String type = node.get("nbiot_type").asText();
            if ( null == type ){
                log.error("nbiot_type not set in raw msg");
                return null;
            }
            type = StringUtils.capitalize(type);
            if ( !device_category.contains(type) ) {
                log.error("Msg type received={} not config in category={}", type, device_category);
                return null;
            }
            DevMsg dm = objectMapper.readValue(msg, (Class<DevMsg>) Class.forName( device_class_prefix.concat(".").concat(type)));
            dm.toFact();
            if ( type.equals( Device.Group550.name() ) ) {
                Group550 g5 = (Group550)dm;
                log.info("Group550 msg:{}", g5);
                //header.getCamera().stream().forEach( item -> log.debug( item.toString() ));
            } else
                log.info("no-group550 devmsg={} sno={} type={}", dm.toString(), dm.getNbiot_sno(), type);
            return dm;
        } catch ( Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
