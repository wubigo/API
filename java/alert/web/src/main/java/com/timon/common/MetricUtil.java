package com.timon.common;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.timon.alert.MetricCFG;
import com.timon.alert.MetricRecord;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


@Slf4j
@Component
public class MetricUtil {

    @Value("${device.cm.url}")
    String CM_URL;

    public List<MetricRecord> initMetric(String sno){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        if ( null != sno )
            response = restTemplate.getForEntity(CM_URL+"?sno="+sno, String.class);
        log.info("code={} body={}", response.getStatusCode(), response.getBody());
        DocumentContext jsonContext = JsonPath.parse(response.getBody());
        //DocumentContext jsonContext = JsonPath.parse("src/main/resources/device.json");

        JSONArray e = jsonContext.read("$.errors");
        if ( !e.isEmpty() && e.size() == 0  ){
            log.error("api calling return error:{}", e);
            return null;
        }

        JSONArray kinds = jsonContext.read("$.data.device.kinds");
        int lastLevel = kinds.size();
        LinkedHashMap<String, String> kind = (LinkedHashMap)kinds.get(lastLevel-1);
        String type = kind.get("cn_name");
        log.info("type={}", kind.get("cn_name"));

        JSONArray raw_metrics = jsonContext.read("$.data.device.driver.normals");

        log.info("raw metrics={}", raw_metrics.size() );

        List<MetricRecord> mrl = new ArrayList<>();

        for ( int i=0; i < raw_metrics.size(); i++ ){

            DocumentContext subCtx = JsonPath.parse(raw_metrics.get(i));
            String metric_name = subCtx.read("$.en_name");
            String metric_path = subCtx.read("$.operate_key");

            JSONArray grades = subCtx.read("$.grades");
            log.debug("grades={}", grades);
            for ( int k=0; k < grades.size(); k++ ){
                subCtx = JsonPath.parse(grades.get(k));
                JSONArray rules = subCtx.read("$.rules");;
                String level = subCtx.read("$.grade");
                if ( null == rules || rules.size() == 0 )
                    continue;
                int count = rules.size();
                subCtx = JsonPath.parse(rules.get(0));
                String threshold = subCtx.read("$.val");
                String symble = subCtx.read("$.symble");
                String rule_metric_name = subCtx.read("$.normal.en_name");
                String logic = subCtx.read("$.logic");
                String threshold2 = "";
                String rule_metric_name2 = "";
                String symble2="";
                if ( count == 1 ) {
                   if ( rule_metric_name == null || !rule_metric_name.trim().equals(metric_name.trim()) )
                        log.error("simple rule: rule_metric_name= {} metric_name={}", rule_metric_name, metric_name);
                    log.info("simple rule metric_name={} metric_path={} level={} threshold={} has {} rules", metric_name, metric_path, level, threshold, rules.size());
                } else {
                    subCtx = JsonPath.parse(rules.get(1));
                    threshold2 = subCtx.read("$.val");
                    rule_metric_name2 = subCtx.read("$.normal.en_name");
                    symble2 = subCtx.read("$.symble");
                    if ( logic == null || logic.equals("") )
                        logic = subCtx.read("$.logic");
                    if ( rule_metric_name2 != null && rule_metric_name2.equals(metric_name) ){
                        threshold = threshold2;
                        symble = symble2;
                        subCtx = JsonPath.parse(rules.get(0));
                        threshold2 = subCtx.read("$.val");
                        rule_metric_name2 = subCtx.read("$.normal.en_name");
                        symble2 = subCtx.read("$.symble");
                    }

                    log.debug("union rule metric_name={} metric_path={} level={}  has {} rules", metric_name, metric_path, level, rules.size());
                }

                MetricCFG ac = MetricCFG.builder().level(level).threshold(threshold).
                        expCondition(rule_metric_name+symble + "threshold "+ logic + " "+rule_metric_name2+symble2 +threshold2).build();
                List<MetricCFG> al = new ArrayList<MetricCFG>();
                al.add(ac);
                MetricRecord mr = MetricRecord.builder()
                        .device_sno(sno)
                        .device_type(type)
                        .metric_name(rule_metric_name)
                        .metric_path(metric_path)
                        .mcl(al)
                        .build();
                log.debug("grade={} metric/threshold={}/{} second metric/threshold={}/{}", level, metric_name, threshold, rule_metric_name2, threshold2);
                log.info("mr={}", mr);
                mrl.add(mr);
            }
        }

        //JsonPath.parse(mrl).json();
        return mrl;
    }
}
