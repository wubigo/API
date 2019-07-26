package com.timon.alert.repository;

import com.timon.alert.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByIsRead(int isRead);

    @Query(value = "select level, COUNT(*) from Alert group by level")
    public List<?> countByLevel();

    @Query(value = "SELECT DISTINCT sno from Alert where isRead=0")
    public List<String> findAllDevices();

    @Query(value = "SELECT count(*) from Alert where level=:level and  isRead=0")
    public int countOfLevel(@Param("level") String level);

    // @Query(value = "select Alert from Alert where level='?1'")
    @Query(value = "SELECT * FROM t_alert a WHERE a.level = :level",
            nativeQuery = true)
    public List<Alert> findUnReadByLevel(@Param("level") String level);

    @Query(value = "SELECT count(*) FROM t_alert a WHERE a.location_id in (:ids) limit 1",
            nativeQuery = true)
    public int findByLocations(@Param("ids") String ids);

    @Query(value = "select level, COUNT(*) from Alert where time<?1 and time > ?2 group by level")
    public List<?> levelOnHour(long lastHour, long currentHour);
}
