package com.timon.rule;

import com.timon.common.Level;
import com.timon.domain.Group550;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

@Rule(name = "主摄像头连接状态", description = "主摄像头连接状态", priority = 1)
public class Group550Main_camera_con_statusRule {
    @Condition
    public boolean when(@Fact("group550") Group550 group550) {

        if ( !group550.isMain_camera_con_status() )
            return true;
        return false;
    }

    @Action(order = 1)
    public void then(Facts facts) throws Exception {
        //my actions
        facts.put("alert msg", "主摄像头处于未连接状态");
        facts.put("alert level", Level.CRITICAL);

    }
}
