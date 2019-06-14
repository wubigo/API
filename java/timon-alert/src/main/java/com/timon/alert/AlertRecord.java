package com.timon.alert;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AlertRecord {
    String device_sno;
    int  alert_level;
    String metric_name;
    String message;
    int nbiot_create_time;
    public static AlertRecordBuilder builder(String metric_name){
        return new AlertRecordBuilder().metric_name(metric_name);
    }
}
