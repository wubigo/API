package com.timon.alert;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@Builder
@JsonDeserialize(builder = MetricRecord.MetricRecordBuilder.class)
public class MetricRecord implements Serializable {
    String device_sno;           //设备ID
    String device_type;          //设备类型
    String metric_name;          //指标名称
    String metric_path;          //采集数据指标项
    String sec_metric_path;
    String sec_metric_name;
    String[] range;              //标准取值范围
    List<MetricCFG> mcl;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MetricRecordBuilder {

    }

}
