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
@JsonDeserialize(builder = Company.CompanyBuilder.class)
public class Company {
    String name;
    String locationID;
    List<MeetingRoom> meetingRooms;


    @JsonPOJOBuilder(withPrefix = "")
    public static class CompanyBuilder {

    }
}

