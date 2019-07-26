package com.timon.domain.history;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.timon.common.JsonUtil;
import com.timon.domain.distribution.AlertCount;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@ToString
@EqualsAndHashCode
@Builder
@JsonDeserialize(builder = Trend.TrendBuilder.class)
public class Trend {
   List<AlertCount> alertCounts;


    @JsonPOJOBuilder(withPrefix = "")
    public static class TrendBuilder {

    }

    public static void main(String[] args) {
        List<AlertCount> alertCounts = new ArrayList<AlertCount>();
        for ( int i=0; i <24 ; i++){
            AlertCount ac = AlertCount.builder().critical(i).build();
            alertCounts.add(ac);
        }

        JsonUtil.write("ac", alertCounts);
    }
}
