package com.everwing.coreservice.wy.dao.mapper.property;

import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectMeterSearch;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeterSearch;
import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.TCKind;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface TcSurfaceElectMapper
{
    /**
     * 查询电表
     * @param
     * @return
     */
    List<TcWaterMeter> listPageElectMeterInfos(TcElectMeterSearch tcElectMeterSearch) throws DataAccessException;
    List<TcWaterMeter> listPageParentInfos(TcElectMeterSearch tcElectMeterSearch) throws DataAccessException;

    int deleteSurface(String id) throws DataAccessException;

    void modify(TcElectMeter  tcElectMeter) throws DataAccessException;

    String getCode(String projectId) throws DataAccessException;

    void insert(TcElectMeter  tcElectMeter) throws DataAccessException;

   TcElectMeter SeleteByID(String id) throws DataAccessException;

}
