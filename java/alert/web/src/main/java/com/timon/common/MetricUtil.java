package com.timon.common;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.timon.alert.MetricRecord;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;


@Slf4j
@Component
public class MetricUtil {

    @Value("${device.cm.url}")
    String CM_URL;

    public void initMetric(String sno){
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
            return;
        }

        JSONArray kinds = jsonContext.read("$.data.device.kinds");
        int lastLevel = kinds.size();
        LinkedHashMap<String, String> kind = (LinkedHashMap)kinds.get(lastLevel-1);

        log.info("type={}", kind.get("cn_name"));

        JSONArray raw_metrics = jsonContext.read("$.data.device.driver.normals");

        log.info("raw metrics={}", raw_metrics.size() );

        for ( int i=0; i < raw_metrics.size(); i++ ){
            String m = raw_metrics.get(i).toString();
            log.info("m={}", m);
            DocumentContext subCtx = JsonPath.parse(m);
            JSONArray grades = subCtx.read("$.grades");
            log.info("grades={}", grades);


        }
        //for (  )
        MetricRecord mr = MetricRecord.builder()
                .device_sno(sno)
//                .device_type(Device.Group550.name())
//                .metric_name(name)
//                .metric_path("meeting[1].percentPacketLoss")
//                .mcl(al)
                .build();

        log.info("code={} conf={}", response.getStatusCode(), response.getBody());
    }
}
