package com.timon.alert;

import com.timon.domain.DevMsg;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AlertRecord{
    String  alert_level;
    String metric_name;
    String sec_metric_name;
    String message;
    Object factValue;
    Object sec_fact_value;
    AlertHeader header;

    public static AlertRecordBuilder builder(String metric_name){
        return new AlertRecordBuilder().metric_name(metric_name);
    }
}
