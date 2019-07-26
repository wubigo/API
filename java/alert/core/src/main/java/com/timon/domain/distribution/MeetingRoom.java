package com.timon.domain.distribution;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;


@Setter
@Getter
@ToString
@EqualsAndHashCode
@Builder
@JsonDeserialize(builder = MeetingRoom.MeetingRoomBuilder.class)
public class MeetingRoom {
    AlertCount count;
    String name;
    String locationID;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MeetingRoomBuilder {

    }
}
