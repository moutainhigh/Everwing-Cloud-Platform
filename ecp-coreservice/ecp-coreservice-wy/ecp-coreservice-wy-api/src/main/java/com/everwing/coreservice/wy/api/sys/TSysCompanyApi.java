package com.everwing.coreservice.wy.api.sys;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompany;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompanySearch;
import com.everwing.coreservice.common.wy.service.sys.TSysCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
@Component
public class TSysCompanyApi {
    @Autowired
    private TSysCompanyService tSysCompanyService;

    public RemoteModelResult<BaseDto> listPage(String companyId,TSysCompanySearch condition){
        BaseDto baseDto = tSysCompanyService.listPage(companyId,condition);
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(baseDto);
        return result;
    }

    public RemoteModelResult<MessageMap> save(String companyId,TSysCompany entity){
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tSysCompanyService.save(companyId,entity));
        return result;
    }

    public RemoteModelResult<MessageMap> delete(String companyId,TSysCompany entity){
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tSysCompanyService.delete(companyId,entity));
        return result;
    }
}
