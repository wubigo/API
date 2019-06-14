package com.timon.rule;


import com.timon.domain.Group550;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

@Rule(name = "my ruleGroup550VideoTXRule", description = "Group550VideoTXRule", priority = 1)
public class Group550VideoTXRule {

    @Condition
    public boolean when(@Fact("group550") Group550 group550, @Fact("percentPacketLoss") int percentPacketLoss ) {
        //my rule conditions
        if ( percentPacketLoss > 10 )
            return true;
        return false;
    }


    @Action(order = 1)
    public void then(Facts facts) throws Exception {
        //my actions
        facts.put("alert msg", "");
        facts.put("alert level", "error");
    }
}
