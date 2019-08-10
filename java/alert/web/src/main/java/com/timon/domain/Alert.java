package com.timon.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Alert {

    private String sno;
    private String name;
    private String location;
    private String level;
    private long time;
    private String detail;
    private int isRead;



    public static AlertBuilder builder(String sno, String name){
        return new AlertBuilder().sno(sno).name(name);
    }
}
