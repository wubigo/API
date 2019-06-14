package com.timon.alert;

import com.timon.domain.DevMsg;
import com.timon.rule.G5MicroRule;
import com.timon.rule.Group550Rule;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Component
public class AlertRuleEngine {

    static RulesEngine rulesEngine;
    static Rules rules;

    @Value("${rule.class.prefix}")
    String RULE_CLASS_PREFIX;

    @Value("${ruleengine.rulePriorityThreshold}")
    int rulePriorityThreshold;
    @Value("${ruleengine.skipOnFirstAppliedRule}")
    boolean skipOnFirstAppliedRule;
    @Value("${ruleengine.skipOnFirstFailedRule}")
    boolean skipOnFirstFailedRule;
    @Value("${ruleengine.skipOnFirstNonTriggeredRule}")
    boolean skipOnFirstNonTriggeredRule;


    static {
        rules = new Rules();
        RulesEngineParameters parameters = new RulesEngineParameters()
                .skipOnFirstAppliedRule(true)
                .skipOnFirstFailedRule(true)
                .skipOnFirstNonTriggeredRule(true);
        rulesEngine = new DefaultRulesEngine(parameters);
    }

    public void fire(Facts facts, Object rule){
        rules.register(rule);
        rulesEngine.fire(rules, facts);
        facts.asMap().forEach( (k, v) -> log.info(k + ":" + v) );
        log.info("rule executed successfully");
        rules.unregister(rule);
    }

    public void run(DevMsg dm, MetricRecord mr){
        Facts facts = new Facts();
        //facts.put(mr.metric_name, );
        facts.put("metricRecord", mr);
        //String ruleClass = RULE_CLASS_PREFIX+ StringUtils.capitalize(dm.getNbiot_type())+mr.metric_name;
        String ruleClass = RULE_CLASS_PREFIX+ StringUtils.capitalize(dm.getNbiot_type())+"Rule";
        log.info("rule class={}", ruleClass);
        try {
            Object rule = Class.forName(ruleClass).newInstance();
            fire(facts, rule);
        } catch (InstantiationException e) {
            log.error(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        } catch (ClassNotFoundException e) {
            log.error("No rule found for device={} metric={}", dm.getNbiot_type(), mr.metric_name );
        }


    }



    public void testRun(){
        Facts facts = new Facts();
        facts.put("percentPacketLoss", 40);
        fire(facts, new Group550Rule());
        facts = new Facts();
        facts.put("polycom_mic", false);
        facts.put("line_in", true);
        fire(facts, new G5MicroRule());
    }

}
