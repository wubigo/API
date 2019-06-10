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
public class Group550 extends Dev {

    String system_status_inacall;

    /*
    if( now  - networkcon > 180000 ){
                alert: error;
                alert: .message = "网络不通或已关机，请关注. ";}
     */
    private  long nbiot_create_time;

    /*
    Camera.name.Input_3.connected == flase
      type="warning";
      message="双流口处于未连接状态"
     */
    private List<Camera> camera;

    /*
    meeting[0].percentPacketLoss > 5
        type = "error";
        message = "音频传输丢包率为{}" packetLoss;
     */
    private List<Meeting> meeting;

    /*
     "up"{
         this.type = "INFO";
         this.message = "全向麦已经连接";
     "off"{
         this.type="error";
         this.message="未连接全向麦";
      else{
         this.type="info";
         this.message="未知错误"
     }
     */
    private String system_status_microphones;

    /*
    "false"{
        this.type = "INFO";
        this.message = "设备未静音";
    "true"{
        this.type="info";
        this.message="设备已静音";
     }
     else{
        this.type="info";
        this.message="未知"
    }
     */
    private boolean micro_muted;

}
