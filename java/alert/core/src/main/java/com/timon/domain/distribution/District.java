package com.timon.domain.distribution;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import java.util.List;



@Setter
@Getter
@ToString
@EqualsAndHashCode
@Builder
@JsonDeserialize(builder = District.DistrictBuilder.class)
public class District {
    List<Company> companies;
    AlertCount count;
    String name;
    String locationID;

    @JsonPOJOBuilder(withPrefix = "")
    public static class DistrictBuilder {

    }
}
