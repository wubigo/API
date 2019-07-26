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
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class FanCard {

    /*
    fanCards1status == "AppPowerOn"){
          this.type="error";
          this.message="风扇卡1上电状态";}
        else if(fanCards1status == "PowerDown"){
          this.type="error";
          this.message="风扇卡1未上电状态";}
        else if(fanCards1status == "Boot"){
          this.type="error";
          this.message="风扇卡1Boot状态";}
        else if(fanCards1status == "Boot"){
          this.type="error";
          this.message="风扇卡1Boot状态";}
        else if(fanCards1status == "None"){
          this.type="error";
          this.message="风扇卡1板卡不在位";}
        else if(fanCards1status == "Unkonw"){
          this.type="error";
          this.message="风扇卡1故障";}
        else{
          this.type="error";
          this.message="风扇卡1未知状态";

     */
    String status;
    List< PerformsinfoList> performsinfoList;

    boolean valid;



}
