package com.timon.domain.distribution;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@Builder
@JsonDeserialize(builder = AlertCount.AlertCountBuilder.class)
public class AlertCount {
    int critical;
    int major;
    int minor;
    int warn;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AlertCountBuilder {

    }
}
