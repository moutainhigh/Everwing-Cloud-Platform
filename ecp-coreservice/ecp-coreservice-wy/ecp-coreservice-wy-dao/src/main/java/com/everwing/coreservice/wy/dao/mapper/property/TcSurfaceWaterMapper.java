package com.everwing.coreservice.wy.dao.mapper.property;

import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeterSearch;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface TcSurfaceWaterMapper
{
    /**
     * 查询水表
     * @param
     * @return
     */
    List<TcWaterMeter> listPageWaterMeterInfos(TcWaterMeterSearch tcWaterMeter) throws DataAccessException;

    List<TcWaterMeter> listPageWaterTree(TcWaterMeterSearch tcWaterMeter) throws DataAccessException;

    List<TcWaterMeter> listPageParentCodeInfos(TcWaterMeterSearch tcWaterMeter) throws DataAccessException;

    int deleteSurface(String id) throws DataAccessException;

    void modify(TcWaterMeter  tcWaterMeter) throws DataAccessException;

    int repeatWaterMeterCode(String code) throws DataAccessException;

    String getCode(String projectId) throws DataAccessException;

    void insert(TcWaterMeter  tcWaterMeter) throws DataAccessException;

   TcWaterMeter SeleteByID(String id) throws DataAccessException;

}
