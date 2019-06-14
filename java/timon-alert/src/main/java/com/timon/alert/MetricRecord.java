package com.timon.alert;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@Builder
@JsonDeserialize(builder = MetricRecord.MetricRecordBuilder.class)
public class MetricRecord implements Serializable {
    String device_sno;           //设备ID
    String device_type;          //设备类型
    String metric_name;    //指标名称
    int  alert_level;            //告警级别
    int  max;                    //指标上限
    int  min;
//
//    public static MetricRecordBuilder builder(String metric_name){
//        return new MetricRecordBuilder().metric_name(metric_name);
//    }
    @JsonPOJOBuilder(withPrefix = "")
    public static class MetricRecordBuilder {

    }

}
