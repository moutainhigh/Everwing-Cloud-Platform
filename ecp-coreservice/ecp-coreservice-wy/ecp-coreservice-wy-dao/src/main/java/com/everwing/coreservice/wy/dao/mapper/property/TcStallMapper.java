package com.everwing.coreservice.wy.dao.mapper.property;/**
 * Created by wust on 2017/5/19.
 */

import com.everwing.coreservice.common.wy.entity.property.stall.CountStallList;
import com.everwing.coreservice.common.wy.entity.property.stall.TcStall;
import com.everwing.coreservice.common.wy.entity.property.stall.TcStallList;
import com.everwing.coreservice.common.wy.entity.property.stall.TcStallSearch;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/5/19
 * @author wusongti@lii.com.cn
 */
public interface TcStallMapper {
    List<TcStallList> listPageStall(TcStallSearch tcStallSearch);

    List<TcStallList> findByCondition(TcStallSearch tcStallSearch);
    
    TcStall findByBuildingCode(String buildingCode);

    int insert(TcStall entity);

    int batchInsert(List<TcStall> list);

    int modify(TcStall entity);

    int batchModify(List<TcStall> list);

    int batchDelete(List<String> list);

    CountStallList countStall(String projectCode,String buildingName);

    List<TcStall> findByList();
}
