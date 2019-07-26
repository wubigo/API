package com.timon.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bird666 extends DevMsg {

    private List<FanCard> fanCards;

    private  List<PowerCard> powerCards;

    private List<OutputCard> outputCards;

    public void toFact(){

    }

}
