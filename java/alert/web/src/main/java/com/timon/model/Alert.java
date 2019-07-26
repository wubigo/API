package com.timon.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name="t_alert")
public class Alert {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String sno;
    private String name;
    private String area;
    private String level;
    private long time;
    private String detail;
}
