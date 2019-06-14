package com.timon.alert.rule;

import com.timon.alert.AlertRecord;
import com.timon.domain.Group550;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.mvel.MVELAction;
import org.jeasy.rules.mvel.MVELCondition;
import org.jeasy.rules.spel.SpELCondition;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class ConditionTests {

    @Test
    public void testMVELCondition() {

        Condition isAdult = new MVELCondition("group550.audioTX_jitter > 18");
        Facts facts = new Facts();
        Group550 group550 = new Group550();
        group550.audioTX_jitter = 100;
        facts.put("group550", group550);
        boolean evaluationResult = isAdult.evaluate(facts);
        assertThat(evaluationResult).isTrue();
    }


    @Test
    public void testMVELAndCondition() {
        Condition isAdult = new MVELCondition("group550.audioTX_jitter > 18 && group550.getAudioRX_jitter() > 50 ");
        //Condition isAdult = new MVELCondition("group550.audioTX_jitter > 18 && group550.audioRX_jitter() > 50 ");

        Facts facts = new Facts();

        Group550 group550 = new Group550();
        group550.audioTX_jitter = 100;
        group550.setAudioRX_jitter(60);
        facts.put("group550",group550);

        boolean evaluationResult = isAdult.evaluate(facts);

        assertThat(evaluationResult).isTrue();
        if ( evaluationResult ) {
            MVELAction markAsAdult = new MVELAction("alertRecord.setAlert_level(5)");
            facts = new Facts();
            AlertRecord ar = AlertRecord.builder("metric").build();
            facts.put("alertRecord", ar);
            markAsAdult.execute(facts);

            assertThat(ar.getAlert_level() == 5 );
        }


    }


    @Test
    public void testSpELCondition() {

        Condition isAdult = new SpELCondition("group550.audioTX_jitter > 18");
        Facts facts = new Facts();
        Group550 group550 = new Group550();
        group550.audioTX_jitter = 100;
        facts.put("group550",group550);

    }


}
