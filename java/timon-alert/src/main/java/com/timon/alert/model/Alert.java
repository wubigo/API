package com.timon.alert.model;

import lombok.Data;

@Data
public class Alert {
    private String name;
    private String area;
    private String level;
    private long time;
    private String detail;
}
