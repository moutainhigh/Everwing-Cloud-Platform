package com.everwing.coreservice.dynamicreports.api.system;/**
 * Created by wust on 2018/2/5.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.dynamicreports.service.system.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * Function:
 * Reason:
 * Date:2018/2/5
 * @author wusongti@lii.com.cn
 */
@Component
public class CommonApi {
    @Autowired
    private CommonService dynamicreportsCommonService;

    public RemoteModelResult<MessageMap> initResources(){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(dynamicreportsCommonService.initResources());
        return remoteModelResult;
    }

    public RemoteModelResult<MessageMap> login(String loginName,String pwd){
        RemoteModelResult<MessageMap> remoteModelResult = new RemoteModelResult<>();
        remoteModelResult.setModel(dynamicreportsCommonService.login(loginName,pwd));
        return remoteModelResult;
    }
}
