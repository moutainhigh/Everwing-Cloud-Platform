package com.everwing.coreservice.wy.dao.mapper.property;

import com.everwing.coreservice.common.wy.entity.property.Engineering.TcConstruction;
import com.everwing.coreservice.common.wy.entity.property.Engineering.TcConstructionSearch;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface TcConstructionMapper {
    /**
     * 查询工程施工
     * @param tcConstruction
     * @return
     */

    List<TcConstruction> loadbyConstructionlistPage(TcConstructionSearch tcConstruction)  throws DataAccessException;
    /**
     * 查询未关联水电的工程施工
     * @param tcConstruction
     * @return
     */

    List<TcConstruction> loadbyWaterElectlistPage(TcConstructionSearch tcConstruction)  throws DataAccessException;

    /**
     * 查询历史施工
     * @param tcConstruction
     * @return
     */
    List<TcConstruction> loadbyEhistorylistPage(TcConstructionSearch tcConstruction)throws DataAccessException;


    int deleteConstruction(String id)throws DataAccessException;

    void modify(TcConstruction tcConstruction)throws DataAccessException;
    void reviseComplete(TcConstruction tcConstruction)throws DataAccessException;
    void suspend(TcConstruction tcConstruction)throws DataAccessException;
    void startUp(TcConstruction tcConstruction)throws DataAccessException;
    void editDelay(TcConstruction tcConstruction)throws DataAccessException;

    String getCode(String projectId)throws DataAccessException;

    void insert(TcConstruction tcConstruction)throws DataAccessException;

    TcConstruction SeleteByID(String id)  throws DataAccessException;
}
