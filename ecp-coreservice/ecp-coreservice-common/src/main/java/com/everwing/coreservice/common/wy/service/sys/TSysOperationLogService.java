package com.everwing.coreservice.common.wy.service.sys;/**
 * Created by wust on 2017/6/2.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.operationLog.TSysOperationLog;
import com.everwing.coreservice.common.wy.entity.system.operationLog.TSysOperationLogSearch;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/2
 * @author wusongti@lii.com.cn
 */
public interface TSysOperationLogService {
    BaseDto listPage(String companyId, TSysOperationLogSearch condition);

    MessageMap insert(String companyId, TSysOperationLog entity);
}
