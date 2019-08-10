package com.timon.alert.repository;

import com.timon.alert.model.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

//public interface AlertRepository extends JpaRepository<Alert, Long> {
public interface AlertRepository extends PagingAndSortingRepository<Alert, Long>{

    Page<Alert> findByIsRead(int isRead, Pageable p);

    Page<Alert> findByIsReadAndCreatedAtGreaterThan(int isRead, Date lastDay, Pageable p);

    Page<Alert> findByIsReadAndLevel(int isRead, String level, Pageable p);

    Page<Alert> findByIsReadAndSno(int isRead, String sno, Pageable p);


    //List<Alert> findByIsRead(int isRead);

    @Query(value = "select level, COUNT(*) from Alert group by level")
    public List<?> countByLevel();

    public List<Alert> findAllByLevel(String level);

    @Query(value = "SELECT DISTINCT sno from Alert where isRead=0")
    public Page<String> findAllDevices(Pageable p);

    @Query(value = "SELECT count(*) from Alert where level=:level and  isRead=0")
    public int countOfLevel(@Param("level") String level);

    // @Query(value = "select Alert from Alert where level='?1'")
    @Query(value = "SELECT * FROM t_alert a WHERE a.level = :level",
            nativeQuery = true)
    public List<Alert> findUnReadByLevel(@Param("level") String level);

    @Query(value = "SELECT count(*) FROM t_alert a WHERE a.location_id in (:ids) limit 1",
            nativeQuery = true)
    public int findByLocations(@Param("ids") String ids);

    @Query(value = "SELECT * FROM t_alert a WHERE a.level = :level and a.created_at >= DATE_SUB(NOW(),INTERVAL 1 day) ",
            nativeQuery = true)
    public List<Alert> find24HUnReadByLevel(@Param("level") String level);

    @Query(value = "select level, COUNT(*) from Alert where time<?1 and time > ?2 group by level")
    public List<?> levelOnHour(long lastHour, long currentHour);

    @Query(value = "select hour(created_at) as h , level, count(*) from t_alert  where created_at >= DATE_SUB(NOW(),INTERVAL 1 day) group by hour(created_at), level",
            nativeQuery = true)
    public List<?> trend24H();

    @Query(value = "select hour(created_at) as h , level, count(*) from t_alert  where created_at >= DATE_SUB(NOW(),INTERVAL 1 day)  group by DATE(DATE_SUB(created_at, INTERVAL 1 HOUR)), level",
            nativeQuery = true)
    public List<?> trendByHour();


    public Page<Alert> findByCreatedAtGreaterThan(Date d, Pageable p);

}
