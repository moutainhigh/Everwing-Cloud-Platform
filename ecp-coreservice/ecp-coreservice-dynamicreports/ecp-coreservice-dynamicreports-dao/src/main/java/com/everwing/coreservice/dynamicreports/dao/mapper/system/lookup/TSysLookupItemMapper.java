package com.everwing.coreservice.dynamicreports.dao.mapper.system.lookup;

import com.everwing.coreservice.common.dynamicreports.entity.system.lookup.TSysLookupItem;
import com.everwing.coreservice.common.dynamicreports.entity.system.lookup.TSysLookupItemQO;
import com.everwing.coreservice.common.dynamicreports.entity.system.lookup.TSysLookupItemVO;

import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */
public interface TSysLookupItemMapper {

    List<TSysLookupItemVO> listPage(TSysLookupItemQO tSysLookupItemQO);

    List<TSysLookupItemVO> findByCondition(TSysLookupItemQO tSysLookupItemQO);

    int insert(TSysLookupItem tSysLookupItem);

    int modify(TSysLookupItem tSysLookupItem);
}
