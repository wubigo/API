package com.timon.alert.model;

import com.timon.alert.AlertRecord;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@ToString
@EqualsAndHashCode
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

    public static AlertBuilder builder(String sno, String name){
        return new AlertBuilder().sno(sno).name(name);
    }
}
