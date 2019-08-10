package com.timon.common;


import com.timon.domain.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Component
public class LocalUtil {

    @Autowired
    RedisUtil redisUtil;

    @Value("${device.prefix.location}")
    String LOCATION_PREFIX;

    public  void initLocal(){
        // read metric recored from cm service
        String lid="11005";
        String local_prefix="local:";
        List<Integer> ml = (List<Integer>)redisUtil.lGet(local_prefix+lid, 0, -1);
        if ( ml == null || ml.size() == 0 ) {

            redisUtil.lSet(local_prefix+lid, 11004);
            redisUtil.lSet(local_prefix+lid, 11003);
            redisUtil.lSet(local_prefix+lid, 11001);
        }

        ml = (List<Integer>)redisUtil.lGet(local_prefix+lid, 0, -1);
        //ml.forEach( (mr) -> log.info("metric cached record:{}", mr.toString()) );
        for ( Integer m : ml ){
            log.info("metric cached record:{}", m.toString());
        }
    }

    public <T> List<T> getLocal(String locationId){
        List<T> li = (List<T>) redisUtil.lGet(LOCATION_PREFIX + locationId);
        return li;
    }

    public void resetLocal(List<Location> locations){
        locations.forEach( location -> log.info("local={}", location));
        int len = locations.size();

        for ( int i=0; i< len; i++) {
            reset(i, locations);
        }

        //set last  location to itself
        int last=len-1;
        redisUtil.del(LOCATION_PREFIX + locations.get(last).getId());
        redisUtil.lSet(LOCATION_PREFIX + locations.get(last).getId(), locations.get(last).getId());


        for ( int k=0; k< len; k++) {
            List<Integer> li = (List<Integer>) redisUtil.lGet(LOCATION_PREFIX + locations.get(k).getId());
            log.info("local {}", locations.get(k).getId());
            li.forEach(l -> log.info("l={}", l));
        }
    }

    private void reset(int start, List<Location> locations) {

        redisUtil.del(LOCATION_PREFIX + locations.get(start).getId());
        int i = start + 1;
        while (i < locations.size()) {
            redisUtil.lSet(LOCATION_PREFIX + locations.get(start).getId(), locations.get(i).getId());
            i++;
        }
    }

}
