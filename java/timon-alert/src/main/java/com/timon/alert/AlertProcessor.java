package com.timon.alert;

import com.timon.common.Device;
import com.timon.common.RedisUtil;
import com.timon.domain.DevMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AlertProcessor {

    @Value("${device.prefix.metric}")
    String METRIC_PREFIX;

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    AlertRuleEngine engine;

    AlertRecord evaluate(DevMsg dm){
        AlertRecord ar = null;
        String sno = dm.getNbiot_sno();
        // retrieve all metric for a specified device
        List<MetricRecord> ml = (List<MetricRecord>)redisUtil.lGet(METRIC_PREFIX+sno, 0, -1);
        if ( null == ml || ml.size() ==0 )
            return null;
        ml.forEach( (mr) -> engine.run(dm, mr) );

        // retrieve all metric for device type
        ml = (List<MetricRecord>)redisUtil.lGet(Device.valueOf(dm.getNbiot_type().toUpperCase())+METRIC_PREFIX+sno, 0, -1);
        for ( MetricRecord mr : ml )
            engine.run(dm, mr);
        return ar;
    }


    public  void prepareTest(){
        String sno="FD154430C49FD7";
        List<MetricRecord> ml = (List<MetricRecord>)redisUtil.lGet(METRIC_PREFIX+sno, 0, -1);
        if ( ml == null || ml.size() == 0 ) {
            MetricRecord mr = MetricRecord.builder()
                    .metric_name("percentPacketLoss")
                    .alert_level(5)
                    .max(10)
                    .build();
            redisUtil.lSet(METRIC_PREFIX+sno, mr);
            mr = MetricRecord.builder()
                    .metric_name("connect_status")
                    .alert_level(5)
                    .max(10)
                    .build();
            redisUtil.lSet(METRIC_PREFIX+sno, mr);
        }

        ml = (List<MetricRecord>)redisUtil.lGet(METRIC_PREFIX+sno, 0, -1);
        ml.forEach( (mr) -> log.info("metric cached record:{}", mr.toString()) );

    }

}
