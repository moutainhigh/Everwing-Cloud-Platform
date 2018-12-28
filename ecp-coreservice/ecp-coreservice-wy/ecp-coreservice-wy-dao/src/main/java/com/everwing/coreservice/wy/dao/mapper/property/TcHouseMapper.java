package com.everwing.coreservice.wy.dao.mapper.property;/**
 * Created by wust on 2017/4/21.
 */

import com.everwing.coreservice.common.wy.entity.property.house.CountHouseList;
import com.everwing.coreservice.common.wy.entity.property.house.TcHouse;
import com.everwing.coreservice.common.wy.entity.property.house.TcHouseList;
import com.everwing.coreservice.common.wy.entity.property.house.TcHouseSearch;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/4/21
 * @author wusongti@lii.com.cn
 */
public interface TcHouseMapper {
    /**
     * 分页查询房屋信息
     * @param condition
     * @return
     */
    List<TcHouseList> listPageHouse(TcHouseSearch condition);


    /**
     * 获取指定条件的房屋信息
     * @param condition
     * @return
     */
    List<TcHouseList> findByCondition(TcHouseSearch condition);


    /**
     * 增加房屋信息
     * @param entity
     * @return
     */
    int insert(TcHouse entity);

    /**
     * 批量添加
     * @param list
     * @return
     */
    int batchInsert(List<TcHouse> list);

    /**
     * 修改房屋信息
     * @param entity
     * @retun
     */
    int modify(TcHouse entity);

    /**
     * 批量更新
     * @param list
     * @return
     */
    int batchModify(List<TcHouse> list);

    /**
     * 删除房屋信息
     * @param list
     * @return
     */
    int batchDelete(List<String> list);
    
    /**
     * 获取项目下所有的房屋列表
     * @param projectId
     * @return
     */
    List<Map<String,Object>> findCanChargingHouse(String projectId);


    /**
     * 统计住宅信息
     * @param projectCode
     * @param buildingName
     * @return
     */
    CountHouseList countHouse(String projectCode,String buildingName);
    
    TcHouse findByBuildingCode(String buildingCode);
}
