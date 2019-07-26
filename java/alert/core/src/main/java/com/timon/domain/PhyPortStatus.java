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
public class PhyPortStatus {


    /*
    if(switchport1status=="up(1)"){
       this.type="info"
       this.message="GigabitEthernet0/0/1已连接"
     }else if(switchport1status=="down(2)"){
       this.type="error"
       this.message="GigabitEthernet0/0/1未接入"
     }else {
       this.type="warning"
       this.message="GigabitEthernet0/0/1出现未知错误"
     }
     */
    @JsonProperty("GigabitEthernet0/0/1")
    String gigabitEthernet0_0_1;
}
