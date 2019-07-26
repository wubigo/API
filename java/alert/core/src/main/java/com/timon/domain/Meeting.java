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
public class Meeting {

    /**
     * system_status_inacall :
         * videotxbit = meeting[2].actualBitRate
         * videotxbit < 300:
         *     this.type="error";
         *     this.message="视频发送码率"+videotxbit+"Kb,请检测本地摄像头";
         *
     */
    int videotxbit;

    /**
     * *     videorxbit = meeting[3].actualBitRate
     *            videorxbit<300
     *               this.type="error";
     *               this.message="视频接收码率"+videorxbit+"Kb,请检测远端视频信号";
     */
    int videorxbit;

    int actualBitRate;

    /*
    if (audiotxlatency > 2000){
        this.type="warning";
        this.message="音频发送延迟"+audiotxlatency+"ms";
    }
     */
    int latency;

    /*
    percentPacketLoss > 50
     type = "error";
    .message = "视频传输丢包率为"+percentPacketLoss+"%";
     */
    int percentPacketLoss;

    // 大于50
    // type = error
    //>视频接收出现抖动
    //<=50 视频接收未出现抖动
    // 单位：ms
    int jitter;

    String address;
    int index;
}
