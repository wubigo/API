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

    @Value("${device.prefix.metric}")
    String METRIC_PREFIX;

    @Autowired
    RedisUtil redisUtil;



    public void loadMetric(String type, List<MetricRecord> jsonMr) {
        List<MetricRecord> mrl = (List<MetricRecord>) redisUtil.lGet(METRIC_PREFIX + type, 0, -1);
        if (null != mrl && mrl.size() > 0) {
            log.info("metric record for sno={} exist and config={} skip loading", type, mrl);
            return;
        }

        for (MetricRecord mr : jsonMr)
            redisUtil.lSet(METRIC_PREFIX + type, mr);

        mrl = (List<MetricRecord>) redisUtil.lGet(METRIC_PREFIX + type, 0, -1);
        for (MetricRecord m : mrl) {
            log.info("metric cached record:{}", m.toString());
        }
    }

    public List<MetricRecord> initMetric(String sno) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        if (null != sno)
            response = restTemplate.getForEntity(CM_URL + "?sno=" + sno, String.class);
        log.debug("code={} body={}", response.getStatusCode(), response.getBody());
        DocumentContext jsonContext = JsonPath.parse(response.getBody());
        //DocumentContext jsonContext = JsonPath.parse("src/main/resources/device.json");

        JSONArray e = jsonContext.read("$.errors");
        if (!e.isEmpty() && e.size() == 0) {
            log.error("api calling return error:{}", e);
            return null;
        }

        JSONArray kinds = jsonContext.read("$.data.device.kinds");
        int lastLevel = kinds.size();
        LinkedHashMap<String, String> kind = (LinkedHashMap) kinds.get(lastLevel - 1);
        String type = kind.get("cn_name");
        log.info("type={}", kind.get("cn_name"));


        JSONArray raw_metrics = jsonContext.read("$.data.device.driver.normals");

        log.info("raw metrics={}", raw_metrics.size());

        List<MetricRecord> mrl = new ArrayList<>();

        for (int i = 0; i < raw_metrics.size(); i++) {

            DocumentContext subCtx = JsonPath.parse(raw_metrics.get(i));
            String metric_name = subCtx.read("$.en_name");
            String metric_path = subCtx.read("$.operate_key");

            String threshold = null;
            String symble = null;
            String logic = null;
            String threshold2 = "";
            String rule_metric_name2 = null;
            String ruel_metric_path2 = null;
            String symble2 = "";
            String level = "";

            JSONArray grades = subCtx.read("$.grades");

            List<MetricCFG> mcfL = new ArrayList<MetricCFG>();
            if ( grades == null || grades.size() ==0 ){
                log.info("metric={} has no grades", metric_name);
                continue;
            }

            for (int k = 0; k < grades.size(); k++) {

                subCtx = JsonPath.parse(grades.get(k));
                //subCtx = JsonUtil.getContext(grades.get(k));
                level = subCtx.read("$.grade");
                JSONArray rules = subCtx.read("$.rulers");


                if (null == rules || rules.size() == 0) {
                    log.info("no rules found");
                    continue;
                }
                int count = rules.size();

                subCtx = JsonPath.parse(rules.get(0));
                threshold = subCtx.read("$.val");
                symble = subCtx.read("$.symble");

                logic = subCtx.read("$.logic");
                threshold2 = "";
                rule_metric_name2 = null;
                ruel_metric_path2 = null;
                symble2 = "";

                if (count > 1) {
                    subCtx = JsonPath.parse(rules.get(1));
                    threshold2 = subCtx.read("$.val");
                    rule_metric_name2 = subCtx.read("$.normal.en_name");
                    ruel_metric_path2 = subCtx.read("$.normal.operate_key");
                    symble2 = subCtx.read("$.symble");
                    if (logic == null || logic.equals(""))
                        logic = subCtx.read("$.logic");
                    //rules 可能没有顺序，把rule的数据进行交换
                    if (rule_metric_name2 != null && rule_metric_name2.equals(metric_name)) {
                        threshold = threshold2;
                        symble = symble2;
                        subCtx = JsonPath.parse(rules.get(0));
                        threshold2 = subCtx.read("$.val");
                        rule_metric_name2 = subCtx.read("$.normal.en_name");
                        ruel_metric_path2 = subCtx.read("$.normal.operate_key");
                        symble2 = subCtx.read("$.symble");
                    }
                    log.debug("union rule metric_name={} metric_path={} level={}  has {} rules", metric_name, metric_path, level, rules.size());
                }
                String expCond = metric_name + symble + "threshold ";
                if (null != rule_metric_name2 && rule_metric_name2.length() > 0)
                    expCond = expCond + logic + " " + rule_metric_name2 + symble2 + threshold2;
                MetricCFG mcf = MetricCFG.builder().level(level).threshold(threshold).
                        expCondition(expCond).build();
                mcfL.add(mcf);
            }


            if (mcfL.size() > 0) {
                MetricRecord mr = MetricRecord.builder()
                        .device_sno(sno)
                        .device_type(type)
                        .metric_name(metric_name)
                        .metric_path(metric_path)
                        .sec_metric_name(rule_metric_name2)
                        .sec_metric_path(ruel_metric_path2)
                        .mcl(mcfL)
                        .build();
                log.debug("grade={} metric/threshold={}/{} second metric/threshold={}/{}", level, metric_name, threshold, rule_metric_name2, threshold2);
                log.info("mr={}", mr);
                mrl.add(mr);
            } else {
                log.info("no metric record created for grade={} metric/threshold={}/{} second metric/threshold={}/{}", level, metric_name, threshold, rule_metric_name2, threshold2);
            }


        }

        //JsonPath.parse(mrl).json();
        return mrl;
    }

    public void resetMetric(String type){
        List<MetricRecord> mrl = (List<MetricRecord>) redisUtil.lGet(METRIC_PREFIX + type, 0, -1);
        if (null != mrl && mrl.size() > 0) {
            redisUtil.del(METRIC_PREFIX + type);
        }
    }
}
