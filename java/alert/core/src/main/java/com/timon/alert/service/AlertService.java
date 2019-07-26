package com.timon.alert.service;


import com.timon.alert.model.Alert;
import com.timon.common.DataUtil;
import com.timon.common.LocalUtil;
import com.timon.repository.AlertRepository;
import com.timon.web.LevelWithCount;
import com.timon.web.TrendRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class AlertService {
    @Autowired
    AlertRepository repository;
    @Autowired
    LocalUtil localUtil;

    public List<Alert> findToday(){
        return repository.findAll();
    }

    public List<Alert> findUnReadByLevel(String level){
        return repository.findUnReadByLevel(level);
    }

    public List<Alert>  findAllUnRead(){
        return repository.findByIsRead(0);
    }

    public int[] distribute(String[] locations){
        int[] counts = new int[locations.length];
        for ( int i=0; i < locations.length ; i++ ){
            List<Integer> li = localUtil.getLocal(locations[i]);
            String ls="";
            for ( int id : li ){
                ls +=id;
                ls +=",";
            }
            if ( ls.endsWith(",") )
                ls += -1;

            int c = repository.findByLocations(ls);
            counts[i] = c;
            log.info("find alert with location={} result={}", ls, c);
        }
        return counts;
    }

    public List<TrendRes> trend(){
        List<TrendRes> trl = new ArrayList<>();
        for ( int i=1; i< 25; i++) {
            TrendRes tr = new TrendRes();
            tr.setHour(DataUtil.dateByHour(i-1));
            List<LevelWithCount> levelWithCounts = new ArrayList<>();
            List<?> count = repository.levelOnHour(DataUtil.dateByHour(i-1), DataUtil.dateByHour(i));
//            for ( (String k ,v) : count ){
//                LevelWithCount levelWithCount = new LevelWithCount();
//                levelWithCount.setLevel=k;
//                levelWithCount.setCount = Integer.valueOf(v);
//                levelWithCounts.add(levelWithCount);
//            }
            trl.add(tr);
        }
        return trl;
    }

    public void batchInsert(List<com.timon.domain.Alert> al) {
        List<Alert> alerts = new ArrayList<>();
        for (int i = 0; i < al.size(); i++){
            Alert a = new Alert();
            a.setSno(al.get(i).getSno());
            a.setName(al.get(i).getName());
            a.setLocation_id(al.get(i).getLocation());
            a.setLevel(al.get(i).getLevel());
            a.setTime(al.get(i).getTime());
            a.setDetail(al.get(i).getDetail());
            a.setIsRead(0);
            alerts.add(a);
        }
        repository.saveAll(alerts);
    }


    public Optional<Alert> findById(Long id){
       return repository.findById(id);
    }

    public List<Alert> find24HUnReadByLevel(String level){
        return repository.find24HUnReadByLevel(level);
    }

    public Alert insert(Alert a){
        return repository.save(a);
    }


    public String findUnRead(){
        List<Alert> la = repository.findAll();
        int unread = 0;
        for ( Alert a : la ){
            if ( a.getIsRead() == 0 )
                unread ++;
        }
        return "{unread:"+ unread + ",read:"+ (la.size()-unread) +"}";
    }

    public List<?> overview(){
        return repository.countByLevel();
    }

    public void update(Alert a){
        repository.save(a);
    }

    public Alert findAlertById(long id) {
        Optional<Alert> optionalAlert = repository.findById(id);
        //return optionalAlert.orElseThrow(() -> new ChangeSetPersister.NotFoundException("Couldn't find a Alert with id: " + id));
        return optionalAlert.get();
    }
}
