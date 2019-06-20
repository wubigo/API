package com.timon.common;

import com.timon.alert.AlertHeader;
import com.timon.alert.AlertRecord;
import com.timon.alert.MetricRecord;

public class AlertUtil {
    public static AlertRecord convert(String json, MetricRecord mr){
        String jpath = mr.getMetric_path();
        Object fact_value = JsonUtil.read(json, jpath);
        jpath = mr.getSec_metric_path();
        Object sec_fact_value = null;
        AlertHeader header = (AlertHeader) JsonUtil.fromMap(json);
        if ( null != jpath )
            sec_fact_value= JsonUtil.read(json, jpath);
        return AlertRecord.builder(mr.getMetric_name())
                .factValue(fact_value)
                .sec_fact_value(sec_fact_value)
                .header(header)
                .build();
    }
}
