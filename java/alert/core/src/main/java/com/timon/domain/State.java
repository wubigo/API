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
public class State {
    /*
    if(TE50GKstatus == 1)
        { this.type = "info";
          this.message = "GK服务已注册";}
        else if(TE50GKstatus == 0)
        { this.type = "warning";
          this.message = "GK服务未注册";}
        else
        { this.type="warning";
          this.message="未知错误"
        }
     */
    int gk;

    /*
    if(TE50SIPstatus == 1)
        { this.type = "info";
          this.message = "SIP服务已注册";}
        else if(TE50SIPstatus == 0)
        { this.type = "warning";
          this.message = "SIP服务未注册";}
        else
        { this.type="warning";
          this.message="未知错误"
        }
     */
    int sip;



    /*
            if(TE50MicSpakerstatus == 1)
        { this.type = "info";
          this.message = "本地扬声器未静音";}
        else if(TE50MicSpakerstatus == 0)
        { this.type = "info";
          this.message = "本地扬声器已静音";}
        else
        { this.type="warning";
          this.message="未知错误"
        }
     */
    int speaker;

    /*
    if( 1)
        { this.type = "info";
          this.message = "本地Mic未静音";}
        else if( 0)
        { this.type = "info";
          this.message = "本地Mic已静音";}
        else
        { this.type="warning";
          this.message="未知错误"
        }
     */
    int mic;
}
