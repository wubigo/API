package com.timon.alert.rule;

import com.timon.alert.AlertRecord;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.mvel.MVELAction;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ActionTests {

    @Test
    public void testMVELAction() {
        MVELAction markAsAdult = new MVELAction("alertRecord.setAlert_level(5)");
        Facts facts = new Facts();
        AlertRecord ar = AlertRecord.builder("metric").build();
        facts.put("alertRecord", ar);
        markAsAdult.execute(facts);
        assertThat( ar.getAlert_level() >4 );
    }
}
