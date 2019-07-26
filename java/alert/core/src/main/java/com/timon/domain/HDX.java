package com.timon.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HDX extends DevMsg {
    HDXMeeting meeting;
    int totaltimeipcalls;
    boolean mic_status;
    String muted;
    public void toFact(){

    }
}
