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
public class HDXMeeting {
    String rar;
    String tar;
    String rvr;
    String tvr;
    String pktloss;

    /*
    > 50 ){
         this.type = "error";
           this.message = "视频接收出现抖动"+data10+"ms";
        }else{
         this.type="info";
           this.message="视频接收未出现抖动";
        }
     */
    int rvj;

    /*
    if(data10 > 50 ){
         this.type = "error";
           this.message = "视频传输出现抖动"+data10+"ms";
        }else{
         this.type="info";
           this.message="视频传输未出现抖动";
        }
     */
    int tvj;

    /*
    if( data1 > 50 ){
         this.type = "error";
           this.message = "音频接收出现抖动"+data1+"ms";
        }else{
         this.type="info";
           this.message="音频接收未出现抖动";
        }
     */
    int raj;

    /*
    if( data1 > 0 ){
         this.type = "error";
           this.message = "音频传输出现抖动"+data1+"ms";
        }else{
         this.type="info";
           this.message="音频传输未出现抖动";
        }
     */
    int taj;
}
