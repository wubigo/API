package com.timon.alert;

import com.timon.common.Device;
import com.timon.common.JsonUtil;
import com.timon.common.Level;
import com.timon.common.RedisUtil;
import com.timon.domain.DevMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class AlertProcessor {

    @Value("${device.prefix.metric}")
    String METRIC_PREFIX;

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    AlertRuleEngine engine;

    List<AlertRecord> evaluate(String json){
        String type = (String) JsonUtil.read(json,"nbiot_type");
        if ( null == type ){
            log.error("nbiot_type not set in raw msg");
            return null;
        }

        type = StringUtils.capitalize(type);

        if ( !Device.Group550.name().equals(type) ){
            log.info("type={} not supported yet, skip", type);
            return null;
        }
        List<MetricRecord> ml = (List<MetricRecord>)redisUtil.lGet(METRIC_PREFIX+type, 0, -1);
        if ( null == ml || ml.size() ==0 )
            return null;
        List<AlertRecord> arl = new ArrayList<AlertRecord>();
        for ( MetricRecord mr : ml ) {
            AlertRecord ar = engine.run(json, mr);
            if ( null != ar )
                arl.add(ar);
        }
        return arl;
    }

    AlertRecord evaluateDomain(DevMsg dm){
        AlertRecord ar = null;
        String sno = dm.getNbiot_sno();
        // retrieve all metric for a specified device
        List<MetricRecord> ml = (List<MetricRecord>)redisUtil.lGet(METRIC_PREFIX+sno, 0, -1);
        if ( null == ml || ml.size() ==0 )
            return null;
        ml.forEach( (mr) -> engine.runDomain(dm, mr) );

        // retrieve all metric for device type
        ml = (List<MetricRecord>)redisUtil.lGet(Device.valueOf(dm.getNbiot_type().toUpperCase())+METRIC_PREFIX+sno, 0, -1);
        for ( MetricRecord mr : ml )
            engine.runDomain(dm, mr);
        return ar;
    }


    public  void initMetric(){
        // read metric recored from cm service
        String sno="FD154430C49FD7";
        List<MetricRecord> ml = (List<MetricRecord>)redisUtil.lGet(METRIC_PREFIX+sno, 0, -1);
        if ( ml == null || ml.size() == 0 ) {
            String name = "audioRX_packloss";
            String threshold = "1";
            MetricCFG ac = MetricCFG.builder().level(Level.CRITICAL.name()).threshold(threshold).expCondition(name+">" + threshold).build();
            List<MetricCFG> al = new ArrayList<MetricCFG>();
            al.add(ac);
            MetricRecord mr = MetricRecord.builder()
                    .device_sno(sno)
                    .device_type(Device.Group550.name())
                    .metric_name(name)
                    .metric_path("meeting[1].percentPacketLoss")
                    .mcl(al)
                    .build();

            redisUtil.lSet(METRIC_PREFIX+sno, mr);

            name = "videotxbit";
            threshold = "150";
            String name2 = "meetstatus";
            ac = MetricCFG.builder().level(Level.CRITICAL.name()).threshold(threshold).expCondition(name+">"+threshold +" && "+ name2+" == \"up\"").build();
            al = new ArrayList<MetricCFG>();
            al.add(ac);
            threshold = "200";
            ac = MetricCFG.builder().level(Level.MAJOR.name()).threshold(threshold).expCondition(name+">"+threshold +" && "+ name2+" == \"up\"").build();
            al.add(ac);


            mr = MetricRecord.builder()
                    .device_sno(sno)
                    .device_type(Device.Group550.name())
                    .metric_name(name)
                    .metric_path("meeting[2].actualBitRate")
                    .mcl(al)
                    .build();
            redisUtil.lSet(METRIC_PREFIX+sno, mr);
        }

        ml = (List<MetricRecord>)redisUtil.lGet(METRIC_PREFIX+sno, 0, -1);
        //ml.forEach( (mr) -> log.info("metric cached record:{}", mr.toString()) );
        for ( MetricRecord m : ml ){
            log.info("metric cached record:{}", m.toString());
        }
    }

    public void  loadMetric(String file){
        String sno = Device.Group550.name();

        List<MetricRecord> mrl = (List<MetricRecord>)redisUtil.lGet(METRIC_PREFIX+sno, 0, -1);
        if ( null != mrl && mrl.size() > 0 ) {
            log.info("metric record for sno={} exist and config={} skip loading", sno, mrl);
            return;
        }
        List<MetricRecord> mml = JsonUtil.readList(file);
        log.info("mml={}", mml);
        for ( MetricRecord mr : mml )
            redisUtil.lSet(METRIC_PREFIX+sno, mr);

        mml = (List<MetricRecord>)redisUtil.lGet(METRIC_PREFIX+sno, 0, -1);
        for ( MetricRecord m : mrl ){
            log.info("metric cached record:{}", m.toString());
        }
    }

    public void shotMetric(String sno){
        List<MetricRecord> ml = (List<MetricRecord>)redisUtil.lGet(METRIC_PREFIX+sno, 0, -1);
        //ml.forEach( (mr) -> log.info("metric cached record:{}", mr.toString()) );
        for ( MetricRecord m : ml ){
            log.debug("metric cached record:{}", m.toString());
            JsonUtil.write(sno, ml);
        }
    }
}
