package com.timon.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@Builder
@JsonDeserialize(builder = AlertResponse.AlertResponseBuilder.class)
public class AlertResponse{
    int unread;
    int read;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AlertResponseBuilder {

    }

}
