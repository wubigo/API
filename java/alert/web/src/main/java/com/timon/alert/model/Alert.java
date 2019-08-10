package com.timon.alert.model;

import com.timon.alert.AlertRecord;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="t_alert")
public class Alert {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String sno;
    private String name;
    private String location_id;
    private String level;
    private long time;
    private String detail;

    @Column(name="is_read")
    private int isRead;

    private Date createdAt;

    public Alert(){}

    @PrePersist
    void createdAt(){
        this.createdAt = new Date();
    }
}
