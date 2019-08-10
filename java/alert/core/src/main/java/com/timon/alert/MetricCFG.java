package com.timon.alert;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize(builder = MetricCFG.MetricCFGBuilder.class)
public class MetricCFG {
    String threshold;          //指标阈值
    String level;              //告警级别
    String expCondition;       // "meetstatus == up && audiotxpackloss > 10"
    String desc;               // 告警描述
    @JsonPOJOBuilder(withPrefix = "")
    public static class MetricCFGBuilder {

    }
}
