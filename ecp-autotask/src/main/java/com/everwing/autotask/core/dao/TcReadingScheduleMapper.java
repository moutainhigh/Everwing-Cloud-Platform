package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.business.readingschedule.TcReadingSchedule;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author DELL shiny
 * @create 2018/6/5
 */
@Repository
public interface TcReadingScheduleMapper {

    List<TcReadingSchedule> findUsingSchedule(TcReadingSchedule entity);

    int modify(TcReadingSchedule entity);

    Integer getMeterCountByScheduleId(Map<String, Object> paramMap);

    void stopFailedSchedule();

    TcReadingSchedule getScheduleById(String id);
}
