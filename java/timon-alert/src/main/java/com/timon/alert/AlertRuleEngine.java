package com.timon.alert;

import com.timon.common.AlertUtil;
import com.timon.common.DataUtil;
import com.timon.common.JsonUtil;
import com.timon.domain.DevMsg;
import com.timon.rule.G5MicroRule;
import com.timon.rule.Group550Rule;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;
import org.jeasy.rules.mvel.MVELCondition;
import org.mvel2.CompileException;
import org.mvel2.PropertyAccessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
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

    public void runDomain(DevMsg dm, MetricRecord mr){
        Facts facts = new Facts();
        //facts.put(mr.metric_name, );
        facts.put("metricRecord", mr);
        //String ruleClass = RULE_CLASS_PREFIX+ StringUtils.capitalize(header.getNbiot_type())+mr.metric_name;
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

    public AlertRecord run(String json, MetricRecord mr){
        AlertRecord ar = AlertUtil.convert(json, mr);
        log.info("metric_name={} fact_value={}", mr.metric_name, ar.factValue);
        List<MetricCFG> ml = mr.getMcl();
        boolean isAlert = false;
        for ( MetricCFG m : ml ) {
            try {
                log.info("condition:{} [{}={}] threshold={}", m.getExpCondition(), mr.metric_path, ar.factValue, m.threshold);
                Condition condition = new MVELCondition(m.getExpCondition());
                Facts facts = new Facts();
                facts.put(mr.getMetric_name(), ar.factValue);
                facts.put("threshold", m.threshold);
                String secMetricName = mr.sec_metric_name;
                if ( null != secMetricName ) {
                    facts.put(secMetricName, ar.sec_fact_value);
                    log.info("sec_metric_name={}, sec_fact_value={}", secMetricName, ar.sec_fact_value);
                }
                log.info("fire rule...");
                isAlert = condition.evaluate(facts);
                if ( isAlert ) {
                    ar.alert_level = m.level;
                    ar.message = MessageFormat.format(m.desc, m.threshold);
                    break;
                }
            } catch (PropertyAccessException e) {
                log.error("property not set in facts:{}", e.getMessage());
                continue;
            }
        }


        if ( isAlert ){
            log.info("new alert: sno={} msgId={}({}) {}={} level={}",
                    ar.header.getNbiot_sno(), ar.header.getNbiot_create_time(),
                    DataUtil.szDate(ar.header.getNbiot_create_time()), mr.metric_path,
                    ar.factValue, ar.alert_level);
            // save alert history
            return ar;
        } else {
            log.info("sno={} msgId={}({}) {}({})={} is not an alert, discarded",
                    ar.header.getNbiot_sno(), ar.header.getNbiot_create_time(),
                    DataUtil.szDate(ar.header.getNbiot_create_time()), mr.metric_path,
                    mr.metric_name, ar.factValue);
        }
        return  null;
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
