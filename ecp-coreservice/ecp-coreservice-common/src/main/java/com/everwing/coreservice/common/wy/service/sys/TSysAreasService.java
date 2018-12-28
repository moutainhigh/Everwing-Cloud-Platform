package com.everwing.coreservice.common.wy.service.sys;/**
 * Created by wust on 2018/3/20.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.system.areas.TSysAreasList;
import com.everwing.coreservice.common.wy.entity.system.areas.TSysAreasSearch;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/3/20
 * @author wusongti@lii.com.cn
 */
public interface TSysAreasService {
    List<TSysAreasList> findByCondition(WyBusinessContext ctx, TSysAreasSearch tSysAreasSearch);
}
