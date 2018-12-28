package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2018/6/4.
 */
@Repository
public interface TcMeterDataMapper {

    List<Map<String,Object>> getCountAndFeeObjByProject(Map<String,Object> map);

    Double getLastMeterReadingByCode(String meterCode,String shareFrequency,String meterType);

    List<Map<String,Object>> getCountAndFeeObjByProjct(Map<String,Object> map);

    TcMeterData findComplatintData(TcMeterData data);

    List<TcMeterData> findExistsData(TcMeterData data);

    int modify(TcMeterData data);

    int batchAdd(List<TcMeterData> datas);

    TcMeterData findNextData(TcMeterData data);

    List<TcMeterData> findByTaskIdsAndCodes(Map<String,Object> map);

    TcMeterData findMByMeterCodeAndProIdTasks(Map<String,Object> map);

    List<TcMeterData> findAllDatasByTaskId(String id);
}
