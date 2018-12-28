package com.everwing.coreservice.wy.dao.mapper.property;

import com.everwing.coreservice.common.wy.entity.property.property.*;

import java.util.List;
import java.util.Map;

public interface TPropertyChangingHistoryMapper {
    /**
     * 分页查询资产变更信息
     * @param tPropertyChangingHistorySearch
     * @return
     */
    List<TPropertyChangingHistoryList> listPage(TPropertyChangingHistorySearch tPropertyChangingHistorySearch);

    /**
     * 新增资产变更
     * @param tPropertyChangingHistory
     * @return
     */
    int insert(TPropertyChangingHistory tPropertyChangingHistory);

    /**
     * 获取原来的业主信息
     * @param buildingCode
     * @return
     */
    List<ProprietorInfo> getProprietorInfoByBuildingCode(String buildingCode);

    
    List<Map<String,Object>> listPageCustomerInEntery(CustomerSearch customerSearch);
}