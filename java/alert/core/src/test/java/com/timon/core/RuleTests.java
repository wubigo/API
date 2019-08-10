package com.timon.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.timon.alert.AlertRecord;
import com.timon.alert.AlertRuleEngine;
import com.timon.alert.MetricCFG;
import com.timon.alert.MetricRecord;
import com.timon.common.AlertUtil;
import com.timon.common.HttpUtil;
import com.timon.common.JsonUtil;
import com.timon.rule.G5MicroRule;
import com.timon.rule.Group550Rule;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.mvel.MVELCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mvel2.PropertyAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;


@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class RuleTests {

    @Autowired
    AlertRuleEngine are;

    @Test
    public  void testCondition(){
        String exp="threshold>2 and meeting_status != up";
        try {
            Condition condition = new MVELCondition(exp);
            Facts facts = new Facts();
            facts.put("threshold", "4");
            facts.put("meeting_status", "up");
            facts.put("up", "up1");
            boolean result = condition.evaluate(facts);
            log.info("result={}", result);
        } catch (ClassCastException e) {
            //log.error("exp={}", exp);
            //log.info("error:{}  {}",exp,  e.getMessage(), exp);
        }
    }

    @Test
    public void testRunRule(){

        try {
            String msg_metric = HttpUtil.readUrl("http://172.16.16.1/TiMon/core/raw/dev/avm/src/main/resources/metric-router.json");
            ObjectMapper objectMapper = new ObjectMapper();
            MetricRecord mr = objectMapper.readValue(msg_metric, MetricRecord.class);
            log.info("mr={}", mr);
            String msg = HttpUtil.readUrl("http://172.16.16.1/TiMon/core/raw/dev/avm/src/main/resources/huawei交换机.json");
            //are.run(msg, mr);

            AlertRecord ar = AlertUtil.convert(msg, mr);
            //log.info("metric_name={} fact_value={}", mr.metric_name, ar.factValue);
            List<MetricCFG> ml = mr.getMcl();
            boolean isAlert = false;
            for ( MetricCFG m : ml ) {
                try {
                    //log.info("condition:{} [{}={}] threshold={}", m.getExpCondition(), mr.metric_path, ar.factValue, m.threshold);
                    Condition condition = new MVELCondition(m.getExpCondition());
                    Facts facts = new Facts();
                    facts.put(mr.getMetric_name(), ar.getFactValue());
                    facts.put("threshold", m.getThreshold());
                    String secMetricName = mr.getSec_metric_name();
                    if ( null != secMetricName ) {
                        facts.put(secMetricName, ar.getSec_fact_value());
                        //log.info("sec_metric_name={}, sec_fact_value={}", secMetricName, ar.sec_fact_value);
                    }
                    log.info("fire rule...");
                    isAlert = condition.evaluate(facts);
                    if ( isAlert ) {
                        //ar.alert_level = m.level;
                        //ar.message = MessageFormat.format(m.desc, m.threshold);
                        break;
                    }
                } catch (PropertyAccessException e) {
                    log.error("property not set in facts:{}", e.getMessage());
                    continue;
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRun(){
        Facts facts = new Facts();
        facts.put("percentPacketLoss", 40);
        are.fire(facts, new Group550Rule());
        facts = new Facts();
        facts.put("polycom_mic", false);
        facts.put("line_in", true);
        are.fire(facts, new G5MicroRule());
    }
}
