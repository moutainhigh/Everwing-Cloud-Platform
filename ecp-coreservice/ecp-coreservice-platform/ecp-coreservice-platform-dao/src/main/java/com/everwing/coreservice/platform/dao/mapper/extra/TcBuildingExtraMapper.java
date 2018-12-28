
package com.everwing.coreservice.platform.dao.mapper.extra;

import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;

import java.util.List;


public interface TcBuildingExtraMapper
{

    List<TcBuilding> findByCondition(TcBuildingSearch condition);
    /**
     * 批量添加建筑信息
     * @param list
     * @return
     */
    int batchInsert(List<TcBuilding> list);

    /**
     * 批量删除建筑信息
     * @param list
     * @return
     */
    int batchDelete(List<String> list);

    /**
     * 修改建筑
     * @param entity
     * @return
     */
    int modify(TcBuilding entity);

    /**
     * 批量更新
     * @param list
     * @return
     */
    int batchModify(List<TcBuilding> list);

}

