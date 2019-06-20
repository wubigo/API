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
    String threshold;
    String level;
    String expCondition;       // "meetstatus == up && audiotxpackloss > 10"
    String desc;
    @JsonPOJOBuilder(withPrefix = "")
    public static class MetricCFGBuilder {

    }
}
