package com.timon.rule;


import com.timon.alert.AlertRecord;
import com.timon.alert.MetricRecord;
import com.timon.domain.Group550;
import lombok.Getter;
import lombok.Setter;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

@Setter
@Getter
@Rule(name = "Group550Rule", description = "Group550Rule", priority = 1)
public class Group550Rule {

    private MetricRecord mr;
    AlertRecord ar = AlertRecord.builder("percentPacketLoss").build();
    @Condition
    public boolean when(@Fact("percentPacketLoss") int percentPacketLoss, @Fact("metricRecord") MetricRecord metricRecord ) {
        //my rule conditions
        mr = metricRecord;
        if ( percentPacketLoss > mr.getMax()) {
            ar.setAlert_level( mr.getAlert_level());
            ar.setMessage("视频传输丢包率>10");
            return true;
        }
        return false;
    }


    @Action(order = 1)
    public void then(Facts facts) throws Exception {
        //my actions

        facts.put("AlertRecord", ar);
    }
}
