package com.everwing.coreservice.common.wy.service.common;/**
 * Created by wust on 2018/12/19.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodataSearch;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/12/19
 * @author wusongti@lii.com.cn
 */
public interface SynchrodataService {
    BaseDto listPage(WyBusinessContext ctx, TSynchrodataSearch tSynchrodataSearch);

    MessageMap syncData(WyBusinessContext ctx,List<String> tSynchrodatas);
}
