package com.timon.common;

import com.timon.alert.AlertHeader;
import com.timon.alert.AlertRecord;
import com.timon.alert.MetricRecord;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlertUtil {
    public static AlertRecord convert(String json, MetricRecord mr){
        String jpath = mr.getMetric_path();
        Object fact_value = JsonUtil.read(json, jpath);
        if ( null == fact_value ){
            log.info("raw device msg has no data under path={} type={} time={}", jpath, mr.getDevice_type());

        }
        String sec_jpath = mr.getSec_metric_path();
        Object sec_fact_value = null;
        AlertHeader header = (AlertHeader) JsonUtil.fromMap(json);
        if ( null != sec_jpath )
            sec_fact_value= JsonUtil.read(json, sec_jpath);
        return AlertRecord.builder(mr.getMetric_name())
                .factValue(fact_value)
                .sec_fact_value(sec_fact_value)
                .header(header)
                .build();
    }
}
