package com.timon.alert.rule;

import com.timon.alert.AlertRecord;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.mvel.MVELAction;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ActionTests {

    @Test
    public void testMVELAction() {
        MVELAction markAsAdult = new MVELAction("alertRecord.setAlert_level(\"major\"); alertRecord.setMessage(msg)");
        Facts facts = new Facts();
        AlertRecord ar = AlertRecord.builder("metric").build();
        facts.put("alertRecord", ar);
        facts.put("msg", "testing msg2");
        markAsAdult.execute(facts);
        assertThat( ar.getAlert_level().equals("major") );
        log.info("msg={}", ar.getMessage());
        assertThat(ar.getMessage().equals("testing msg1"));
    }
}
