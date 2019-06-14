package com.timon.rule;


import com.timon.alert.MetricRecord;
import com.timon.common.Level;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

@Rule(name = "G5MicroRule", description = "G5MicroRule", priority = 1)
public class G5MicroRule {

    MetricRecord mr = MetricRecord.builder()
            .alert_level(5)
            .max(50)
            .build();
    String status;
    @Condition
    public boolean when(@Fact("polycom_mic") boolean polycom_mic,
                        @Fact("line_in") boolean line_in) {


        if( polycom_mic && line_in ){
            status = "ml_true";
        }else if( !polycom_mic && !line_in ){
            status = "false";
        }else if( polycom_mic && !line_in ){
            status = "mic_true";
        }else if( !polycom_mic && line_in){
            status = "line_true";
        }

        return true;
    }


    @Action(order = 1)
    public void then(Facts facts) throws Exception {
        //my actions
        facts.put("alert msg", "mico_input_num");
        facts.put("alert level", Level.ERROR);
        facts.put("status", status);
    }
}
