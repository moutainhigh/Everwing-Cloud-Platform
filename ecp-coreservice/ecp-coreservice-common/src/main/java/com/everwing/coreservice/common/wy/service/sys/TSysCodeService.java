package com.everwing.coreservice.common.wy.service.sys;/**
 * Created by wust on 2018/8/6.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.system.code.TSysCode;
import com.everwing.coreservice.common.wy.entity.system.code.TSysCodeSearch;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/8/6
 * @author wusongti@lii.com.cn
 */
public interface TSysCodeService {
    List<TSysCode> findByCondition(WyBusinessContext ctx, TSysCodeSearch tSysCodeSearch);
    int insert(WyBusinessContext ctx, TSysCode tSysCode);
    int update(WyBusinessContext ctx, TSysCode tSysCode);
}
