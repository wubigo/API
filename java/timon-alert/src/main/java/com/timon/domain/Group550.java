package com.timon.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group550 extends DevMsg {

    Micro micro;

    String system_status_inacall;


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


    int audioRX_packloss;
    int audioTX_packloss;
    int audioRX_jitter;
    public int audioTX_jitter;
    boolean main_camera_con_status;

    @Override
    public void toFact(){
        Meeting m0 = this.getMeeting().get(0);
        Meeting m1= this.getMeeting().get(1);
        this.audioRX_packloss = m1.getPercentPacketLoss();
        this.audioTX_packloss = m0.getPercentPacketLoss();
        this.audioRX_jitter = m1.getJitter();
        this.audioTX_jitter= m0.getJitter();

        Camera c0 = this.getCamera().get(0);
        this.main_camera_con_status = c0.isConnected();
    }

}
