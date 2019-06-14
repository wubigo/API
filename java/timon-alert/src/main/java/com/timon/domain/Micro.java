package com.timon.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Micro {

    /*
    if(micnum=="true"&&linenum=="true"){
     statue = "ml_true";
    }else if(micnum=="false"&&linenum=="false"){
        statue = "false";
    }else if(micnum=="true"&&linenum=="false"){
        statue = "mic_true";
    }else if(micnum=="false"&&linenum=="true"){
        statue = "line_true";
    }
     */
    @JsonProperty("POLYCOM_MIC")
    boolean polycom_mic;
    @JsonProperty("LINE_IN")
    boolean line_in;
}
