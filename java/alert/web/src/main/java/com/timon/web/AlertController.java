package com.timon.web;


import com.google.common.base.Enums;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.timon.alert.model.Alert;
import com.timon.alert.service.AlertService;
import com.timon.common.JsonUtil;
import com.timon.common.Level;
import com.timon.common.Mark;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(value = "Alert home controller", description = "Alert home controller")
public class AlertController {

    @Autowired
    AlertService as;


    /**
     * 根据位置或状态分页返回告警设备列表
     * @param location_id
     * @param status
     * @param page_num
     * @param page_size
     * @return
     */
    @GetMapping(value = "/devices")
    public WebResponse<JSONObject> findAlertDevices(@RequestParam(value = "location_id", defaultValue = "") String location_id,
                                                    @RequestParam(value = "status", defaultValue = "abnormal") String status,
                                                    @RequestParam(value = "page_num", defaultValue = "0") int page_num,
                                                    @RequestParam(value = "page_size", defaultValue = "20") int page_size) {
        if (page_num > 0)
            page_num--;
        JSONObject obj = new JSONObject();
        List<JSONObject> alerts = new ArrayList<>();
        if (status.equals("abnormal") || status.equals("online") || status.equals("all")) {

            Page<String> pa = as.findAlertDevices(page_num, page_size);
            List<String> sl = pa.getContent();
            for (String sno : sl) {
                JSONObject o = new JSONObject();
                o.put("sno", sno);
                o.put("status", status);
                alerts.add(o);
            }
            obj.put("total_page_count", pa.getTotalPages());
            obj.put("total", pa.getTotalElements());
            obj.put("page_num", page_num);

            obj.put("page_size", page_size);
            obj.put("result", alerts);
        } else if (status.equals("offline")) {

        }

        WebResponse<JSONObject> response = new WebResponse<>();
        response.setData(obj);
        return response;
    }

    /**
     * 查询处于严重告警级别的设备列表
     * @param location_id
     * @return
     */
    @GetMapping(value = "/device/critical")
    public WebResponse<Integer> countCritical(@RequestParam(value = "location_id", defaultValue = "") String location_id) {
        int count = as.countOfLevel("critical");
        WebResponse<Integer> response = new WebResponse<>();
        response.setData(Integer.valueOf(count));
        return response;
    }


    /**
     * 设备状态统计
     * @param location_id
     * @return
     */
    @GetMapping(value = "/devices_stat")
    public WebResponse<JSONObject> devices_stat(@RequestParam(value = "location_id", defaultValue = "") String location_id) {
        JSONObject obj = new JSONObject();
        List<JSONObject> alerts = new ArrayList<>();
        int page_num = 0;
        int page_size = 100;
        Page<String> pa = as.findAlertDevices(page_num, page_size);
        obj.put("overall", pa.getTotalElements());
        obj.put("abnormal", pa.getTotalElements());
        obj.put("online", pa.getTotalElements());
        obj.put("offline", 0);


        WebResponse<JSONObject> response = new WebResponse<>();
        response.setData(obj);
        return response;
    }

    /**
     * 根据位置或级别查询已处理的告警列表
     * @param location_id
     * @param level
     * @param page_num
     * @param page_size
     * @return
     */
    @GetMapping(value = "/read/list")
    public WebResponse<JSONObject> listRead(@RequestParam(value = "location_id", defaultValue = "") String location_id,
                                            @RequestParam(value = "level", defaultValue = "") String level,
                                            @RequestParam(value = "page_num", defaultValue = "0") int page_num,
                                            @RequestParam(value = "page_size", defaultValue = "20") int page_size) {
        if (page_num > 0)
            page_num--;

        Page<Alert> pa = as.findAllUnRead(1, page_num, page_size);
        JSONObject obj = JsonUtil.pageObject(pa);
        obj.put("page_num", page_num);
        obj.put("page_size", page_size);
        WebResponse<JSONObject> response = new WebResponse<>();
        response.setData(obj);
        return response;


    }

    /**
     * 查询当日的告警列表
     * @param mark
     * @param page_num
     * @param page_size
     * @return
     */
    @GetMapping(value = "/today")
    public WebResponse<JSONObject> listToday(@RequestParam(value = "mark", defaultValue = "2") int mark,
                                             @RequestParam(value = "page_num", defaultValue = "0") int page_num,
                                             @RequestParam(value = "page_size", defaultValue = "20") int page_size) {
        if (page_num > 0)
            page_num--;
        JSONObject obj = new JSONObject();
        List<Alert> alerts = new ArrayList<>();
        if (mark >= 2 || mark < 0) {
            Page<Alert> pa = as.findToday(page_num, page_size);
            alerts = pa.getContent();
            obj.put("total_page_count", pa.getTotalPages());
            obj.put("total", pa.getTotalElements());
            obj.put("page_num", page_num);
            obj.put("page_size", page_size);
            obj.put("result", alerts);

        } else {
            Page<Alert> pa = as.findAllUnRead(mark, page_num, page_size);
            alerts = pa.getContent();
            obj.put("total_page_count", pa.getTotalPages());
            obj.put("total", pa.getTotalElements());
            obj.put("page_num", page_num);
            obj.put("page_size", page_size);
            obj.put("result", alerts);
        }

        WebResponse<JSONObject> response = new WebResponse<>();
        response.setData(obj);
        return response;
    }

    /**
     * 当日告警位置分布图
     * @param ids
     * @return
     */
    @PostMapping(value = "/distribution")
    public WebResponse<JSONArray> distribute(@RequestBody String ids) {
        if (null == ids) {
            return new WebResponse("ids is null");
        }
        DocumentContext jsonContext = JsonPath.parse(ids);
        JSONArray jarray = jsonContext.read("$.ids");
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        for (int i = 0; i < jarray.size(); i++) {
            String id = jarray.get(i).toString();
            log.info("id={}", id);
            int alertsByLocal = as.distribute(id);
            obj.put("id", id);
            obj.put("count", alertsByLocal);
            array.add(obj);
        }


        WebResponse<JSONArray> response = new WebResponse<>();
        response.setData(array);
        return response;
    }

    /**
     * 根据级别查询未处理的告警列表
     * @param level
     * @return
     */
    @GetMapping(value = "/unread/{level}")
    public WebResponse<List<Alert>> listTodayUnread(@PathVariable("level") String level) {
        List<Alert> alerts = null;
        if (null == level || "".equals(level)) {
            return new WebResponse("level not set ,try it again");
        }
        Level l = Enums.getIfPresent(Level.class, level.toUpperCase()).orNull();
        if (null == l) {
            return new WebResponse("level invalid ,try it again");
        }

        alerts = as.findUnReadByLevel(level);
        WebResponse<List<Alert>> response = new WebResponse<>();
        response.setData(alerts);
        return response;
    }

    /**
     * 根据位置，或sno，或级别查询告警列表
     * @param location_id
     * @param nbiot_sno
     * @param level
     * @param page_num
     * @param page_size
     * @return
     */
    @GetMapping(value = "/unread/list")
    public WebResponse<JSONObject> listUnread(@RequestParam(value = "location_id", defaultValue = "") String location_id,
                                              @RequestParam(value = "nbiot_sno", defaultValue = "") String nbiot_sno,
                                              @RequestParam(value = "level", defaultValue = "") String level,
                                              @RequestParam(value = "page_num", defaultValue = "0") int page_num,
                                              @RequestParam(value = "page_size", defaultValue = "20") int page_size) {
        if (nbiot_sno.length() > 0 && location_id.length() > 0) {
            log.error("device and location can be set at same time");
            return null;
        }
        if (page_num > 0)
            page_num--;
        JSONObject obj = new JSONObject();
        List<Alert> alerts = new ArrayList<>();

        if (null == level || "".equals(level)) {
            Page<Alert> pa = as.findAllUnRead(0, page_num, page_size);
            alerts = pa.getContent();
            obj.put("total_page_count", pa.getTotalPages());
            obj.put("total", pa.getTotalElements());
            obj.put("page_num", page_num);
            obj.put("page_size", page_size);
            obj.put("result", alerts);
            WebResponse<JSONObject> response = new WebResponse<>();
            response.setData(obj);

            return response;
        }

        if ( location_id.length() > 0  ){
            log.info("location_id={}", location_id);
        }

        Level l = Enums.getIfPresent(Level.class, level.toUpperCase()).orNull();
        if (null != l) {
            log.info("unread by Level={}", level);
            Page<Alert> pa = as.findUnReadByLevel(page_num, page_size, level);
            alerts = pa.getContent();
            obj.put("total_page_count", pa.getTotalPages());
            obj.put("total", pa.getTotalElements());
            obj.put("page_num", page_num);
            obj.put("page_size", page_size);
            obj.put("result", alerts);
            WebResponse<JSONObject> response = new WebResponse<>();
            response.setData(obj);
            return response;
        } else if ( location_id.length() > 0  ){
            log.info("unread by location={}", location_id);
        } else if ( nbiot_sno.length() > 0  ){

            log.info("unread by sno={}", nbiot_sno);
            Page<Alert> pa = as.findUnReadBySno(page_num, page_size, level);
            alerts = pa.getContent();
            obj.put("total_page_count", pa.getTotalPages());
            obj.put("total", pa.getTotalElements());
            obj.put("page_num", page_num);
            obj.put("page_size", page_size);
            obj.put("result", alerts);
            WebResponse<JSONObject> response = new WebResponse<>();
            response.setData(obj);
            return response;
        }

        return null;


    }

    /**
     * 今日告警概览
     *
     * @param locationID
     * @return unread: {} read: {}
     */
    @GetMapping(value = "/overview")
    public WebResponse<AlertResponse> overview(@RequestParam(value = "location_id", defaultValue = "") String location_id) {
        log.info("overview...");
        ResponseEntity e = ResponseEntity.ok("ov");
        log.info("response:{}", e);
        AlertResponse response = AlertResponse.builder().unread(527).read(0).build();
        WebResponse<AlertResponse> res = new WebResponse<AlertResponse>(response);
        return res;
    }

    /**
     * 标记告警为已处理
     * @param id
     * @return
     */
    @GetMapping(value = "/id")
    public WebResponse mark(@RequestParam(value = "id") long id) {
        // check if alert id exist
        Alert opt = as.findAlertById(id);

        if (null == opt) {

        } else {

            opt.setIsRead(1);
            as.update(opt);
            log.info("id={} has been read", opt);
        }
        return new WebResponse();

    }

    /**
     * 告警实时概览（当天未处理的告警）
     *
     * @param name
     * @return :   "critical": {},
     * "major": {},
     * "minor": {},
     * "warning": {}
     */
    @GetMapping(value = "/realtime")
    public WebResponse<JSONArray> findUnread(@RequestParam(value = "name", defaultValue = "") String name) {
        log.info("findUnread...");
        List<Object[]> levelCount = (List<Object[]>) as.overview();
        JSONArray array = new JSONArray();
        for (Object[] o : levelCount) {
            JSONObject obj = new JSONObject();
            obj.put("level", o[0]);
            obj.put("count", o[1]);
            array.add(obj);
        }
        WebResponse<JSONArray> wr = new WebResponse<>();
        wr.setData(array);
        return wr;
    }

    @GetMapping(value = "/trend")
    public WebResponse<List<?>> trend(@RequestParam(value = "location_id", defaultValue = "") String location_id) {
        List<?> rs = as.trend24H();
        WebResponse wr = new WebResponse();
        wr.setData(rs);
        return wr;
    }

    /**
     * 告警过去24小时趋势图
     * @param location_id
     * @return
     */
    @GetMapping(value = "/trend24")
    public WebResponse<List<?>> trend24(@RequestParam(value = "location_id", defaultValue = "") String location_id) {
        List<?> rs = as.trend();
        WebResponse wr = new WebResponse();
        wr.setData(rs);
        return wr;
    }
}
