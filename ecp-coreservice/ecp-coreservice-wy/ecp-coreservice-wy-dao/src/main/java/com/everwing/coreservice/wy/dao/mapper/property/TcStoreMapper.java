package com.everwing.coreservice.wy.dao.mapper.property;/**
 * Created by wust on 2017/5/19.
 */

import com.everwing.coreservice.common.wy.entity.property.store.CountStoreList;
import com.everwing.coreservice.common.wy.entity.property.store.TcStore;
import com.everwing.coreservice.common.wy.entity.property.store.TcStoreList;
import com.everwing.coreservice.common.wy.entity.property.store.TcStoreSearch;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/5/23
 * @author wusongti@lii.com.cn
 */
public interface TcStoreMapper {
    List<TcStoreList> listPageStore(TcStoreSearch condition);

    List<TcStoreList> findByCondition(TcStoreSearch condition);

    int insert(TcStore entity);

    int batchInsert(List<TcStore> list);

    int modify(TcStore entity);

    int batchModify(List<TcStore> list);

    int batchDelete(List<String> list);

    CountStoreList countStore(String projectCode,String buildingName);
    
    TcStore findByBuildingCode(String builidngCode);
}
