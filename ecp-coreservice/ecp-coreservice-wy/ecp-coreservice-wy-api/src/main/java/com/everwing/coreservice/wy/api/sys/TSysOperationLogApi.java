package com.everwing.coreservice.wy.api.sys;/**
 * Created by wust on 2017/6/2.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.operationLog.TSysOperationLog;
import com.everwing.coreservice.common.wy.entity.system.operationLog.TSysOperationLogSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * Function:操作日志
 * Reason:
 * Date:2017/6/2
 * @author wusongti@lii.com.cn
 */
@Component
public class TSysOperationLogApi {
    @Autowired
    private TSysOperationLogService tSysOperationLogService;

    public RemoteModelResult<BaseDto> listPage(String companyId, TSysOperationLogSearch condition){
        BaseDto baseDto = tSysOperationLogService.listPage(companyId,condition);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(baseDto);
        return result;
    }

    public RemoteModelResult<MessageMap> insert(String companyId,TSysOperationLog entity){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(tSysOperationLogService.insert(companyId,entity));
        return remoteModelResult;
    }
}
