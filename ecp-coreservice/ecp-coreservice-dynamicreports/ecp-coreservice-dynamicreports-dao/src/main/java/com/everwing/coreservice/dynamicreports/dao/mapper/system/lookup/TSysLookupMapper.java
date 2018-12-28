package com.everwing.coreservice.dynamicreports.dao.mapper.system.lookup;


import com.everwing.coreservice.common.dynamicreports.entity.system.lookup.*;

import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */
public interface TSysLookupMapper {
    List<TSysLookupVO> listPage(TSysLookupQO tSysLookupQO);

    List<TSysLookupVO> findByCondition(TSysLookupQO tSysLookupQO);

    List<TSysLookupSelectVO> findLookupSelect(TSysLookupSelectQO tSysLookupSelectQO);

    int insert(TSysLookup entity);

    int modify(TSysLookup entity);
}
