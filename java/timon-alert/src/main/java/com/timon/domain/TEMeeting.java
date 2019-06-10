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
public class TEMeeting {
    /*
    if(TE50audiorxPresentPackloss>1)
    { this.type = "error";
      this.message = "音频接收丢包率较高"+TE50audiorxPresentPackloss;}
    else
    { this.type = "info";
      this.message = "音频接收丢包率为"+TE50audiorxPresentPackloss;}
     */
    String audioLossPerRev;

    /*
    if(TE50audiotxPackloss>100)
        { this.type = "error";
          this.message = "音频发送丢包数较高"+TE50audiotxPackloss;}
        else
        { this.type = "info";
          this.message = "音频发送丢包数为"+TE50audiotxPackloss;}
     */
    int audioLossNumSend;

    /*
    if(TE50videotxPresentPackloss>1)
        { this.type = "error";
          this.message = "视频发送丢包率较高"+TE50videotxPresentPackloss;}
        else
        { this.type = "info";
          this.message = "视频发送丢包率为"+TE50videotxPresentPackloss;}
     */
    String videoLossPerSend;

    /*
    if(TE50videotxPackloss>100)
        { this.type = "error";
          this.message = "视频发送丢包数较高"+TE50videotxPackloss;}
        else
        { this.type = "info";
          this.message = "视频发送丢包数为"+TE50videotxPackloss;}
     */
    int videoLossNumSend;

    String videoRateRev;

    String videoRateSend;

    String sendRate;

    String revRate;

    int joinDay;

    int joinHour;

    int joinMin;

    /*
    if(TE50audiotxPresentPackloss>1)
        { this.type = "error";
          this.message = "音频发送丢包率较高"+TE50audiotxPresentPackloss;}
        else
        { this.type = "info";
          this.message = "音频发送丢包率为"+TE50audiotxPresentPackloss;}
     */
    String audioLossPerSend;

}
