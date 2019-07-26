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
public class SwitchCisco extends DevMsg {
    BandwidthCisco bandwidth;
    PhyPortStatusCisco phyPortStatus;
    public void toFact(){

    }
}
