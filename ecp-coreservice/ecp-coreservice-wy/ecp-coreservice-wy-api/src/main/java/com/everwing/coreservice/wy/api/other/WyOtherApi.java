package com.everwing.coreservice.wy.api.other;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.other.pojo.TcAgentCode;
import com.everwing.coreservice.common.wy.entity.other.pojo.TcAgentCodeAndSysUser;
import com.everwing.coreservice.common.wy.service.other.WyOtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WyOtherApi {
    @Autowired
    private WyOtherService wyOtherService;

    
    public RemoteModelResult<BaseDto> getCurrUserAgentCode() {
    	return new RemoteModelResult<BaseDto>(wyOtherService.getAgentCodeByUserId(CommonUtils.getCompanyIdByCurrRequest(), WyBusinessContext.getContext().getUserId()));
    }
    
    public RemoteModelResult<BaseDto> bindAgentCode(TcAgentCodeAndSysUser tcAgentCodeAndSysUser) {
    	return new RemoteModelResult<BaseDto>(wyOtherService.bindAgentCode(CommonUtils.getCompanyIdByCurrRequest(),tcAgentCodeAndSysUser));
    }
    
    public RemoteModelResult<BaseDto> newAgentCode(TcAgentCode tcAgentCode) {
    	return new RemoteModelResult<BaseDto>(wyOtherService.newAgentCode(CommonUtils.getCompanyIdByCurrRequest(),tcAgentCode));
    }
    
    public RemoteModelResult<BaseDto> updateAgentCode(TcAgentCode tcAgentCode) {
    	return new RemoteModelResult<BaseDto>(wyOtherService.updateAgentCode(CommonUtils.getCompanyIdByCurrRequest(),tcAgentCode));
    }
    
    public RemoteModelResult<BaseDto> delAgentCode(List<String> agentCodeIds) {
    	return new RemoteModelResult<BaseDto>(wyOtherService.delAgentCode(CommonUtils.getCompanyIdByCurrRequest(),agentCodeIds));
    }
    
}
