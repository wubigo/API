package bigo.easyrule;


import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

@Rule(name = "my rule", description = "my rule description", priority = 1)
public class Group550Rule {

    @Condition
    public boolean when(@Fact("group550") Group550 group550, @Fact("percentPacketLoss") int percentPacketLoss ) {
        //my rule conditions
        if ( ! group550.connected )
            return true;

        if ( group550.percentPacketLoss  > 5 )
            return true;

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
