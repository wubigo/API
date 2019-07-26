package com.timon.web;


import com.google.common.base.Enums;
import com.timon.alert.model.Alert;
import com.timon.alert.service.AlertService;
import com.timon.common.Level;
import com.timon.common.Mark;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(value="Alert home controller", description="Alert home controller")
public class AlertController {

    @Autowired
    AlertService as;

    @GetMapping(value  = "/devices")
    public WebResponse<List<String>> findAlertDevices(@RequestParam(value="location_id", defaultValue="") String location_id){
        List<String> ds = as.findAlertDevices();
        WebResponse<List<String>> response = new WebResponse<>();
        response.setData(ds);
        return response;
    }

    @GetMapping(value  = "/device/critical")
    public WebResponse<Integer> countCritical(@RequestParam(value="location_id", defaultValue="") String location_id){
        int count = as.countOfLevel("critical");
        WebResponse<Integer> response = new WebResponse<>();
        response.setData(Integer.valueOf(count));
        return response;
    }

    @GetMapping(value  = "/today")
    public WebResponse<List<Alert>> listToday(@RequestParam(value="mark", defaultValue="") String mark){
        List<Alert> alerts = null;
        if ( null ==mark || "".equals(mark) ){
            alerts = as.findToday();
        } else if  ( "unread".equals(mark) )
            alerts = as.findAllUnRead();

        WebResponse<List<Alert>> response = new WebResponse<>();
        response.setData(alerts);
        return response;
    }


    @GetMapping(value  = "/distribution")
    public WebResponse<int[]> distribute(@RequestParam(value="ids", defaultValue="") String ids, @RequestParam(value="depth", defaultValue="") String depth){
        if ( null == ids ){
            return new WebResponse("ids is null");
        }
        String[] locations = ids.split(",");
        int[] alertsByLocal = as.distribute(locations);
        WebResponse<int[]> response = new WebResponse<>();
        response.setData(alertsByLocal);
        return response;
    }



    @GetMapping(value  = "/unread/{level}")
    public WebResponse<List<Alert>> listTodayUnread(@PathVariable("level") String level){
        List<Alert> alerts = null;
        if ( null ==level || "".equals(level) ){
           return new WebResponse("level not set ,try it again");
        }
        Level l = Enums.getIfPresent(Level.class, level.toUpperCase()).orNull();
        if ( null == l  ){
            return new WebResponse("level invalid ,try it again");
        }

        alerts = as.findUnReadByLevel(level);
        WebResponse<List<Alert>> response = new WebResponse<>();
        response.setData(alerts);
        return response;
    }

    @GetMapping(value  = "/unread/list")
    public WebResponse<List<Alert>> listUnread(@RequestParam(value="location_id", defaultValue="") String location_id, @RequestParam(value="level", defaultValue="") String level){
        if ( null ==level || "".equals(level) ) {
            List<Alert> alerts = as.findAllUnRead();
            WebResponse<List<Alert>> response = new WebResponse<>();
            response.setData(alerts);
            return response;
        }

        Level l = Enums.getIfPresent(Level.class, level.toUpperCase()).orNull();
        if ( null == l  ){
            return new WebResponse("level invalid ,try it again");
        }
        List<Alert> alerts = as.findUnReadByLevel(level);
        WebResponse<List<Alert>> response = new WebResponse<>();
        response.setData(alerts);
        return response;
    }

    /**
     * 今日告警概览
     * @param locationID
     * @return  unread: {} read: {}
     */
    @GetMapping(value  = "/overview")
    public WebResponse<AlertResponse> overview(@RequestParam(value="location_id", defaultValue="") String location_id) {
        log.info("overview...");
        ResponseEntity e = ResponseEntity.ok("ov");
        log.info("response:{}", e);
        AlertResponse response = AlertResponse.builder().unread(527).read(0).build();
        WebResponse<AlertResponse> res = new WebResponse<AlertResponse>( response );

        return res;
    }


    @GetMapping(value  = "/{id}")
    public WebResponse mark(@PathVariable("id") long id, @RequestParam(value="mark") String mark) {
        // check if alert id exist
        Alert opt = as.findAlertById(Long.valueOf(id));

        if ( null == opt ){

        } else {
            if (Mark.READ.name().equals(mark)) {

                opt.setIsRead( 1 );
                as.update(opt);

            }
        }
        return new WebResponse();

    }

    /**
     * 告警实时概览（当天未处理的告警）
     * @param name
     * @return :   "critical": {},
                    "major": {},
                    "minor": {},
                    "warning": {}

     */
    @GetMapping(value  = "/unread")
    public WebResponse<List<?>> findUnread(@RequestParam(value="name", defaultValue="") String name) {
        log.info("findUnread...");
        List<?> levelCount = as.overview();

        WebResponse<List<?>> wr = new WebResponse<>();
        wr.setData(levelCount);
        return wr;
    }

    @GetMapping(value  = "/trend")
    public WebResponse<List<TrendRes> > trend(@RequestParam(value="location_id", defaultValue="") String location_id) {
        List<TrendRes> rs = as.trend();
        WebResponse wr = new WebResponse();
        wr.setData(rs);
        return wr;
    }



}
