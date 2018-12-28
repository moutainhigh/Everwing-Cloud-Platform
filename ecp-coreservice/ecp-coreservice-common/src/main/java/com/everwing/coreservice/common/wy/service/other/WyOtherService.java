package com.everwing.coreservice.common.wy.service.other;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.other.pojo.TcAgentCode;
import com.everwing.coreservice.common.wy.entity.other.pojo.TcAgentCodeAndSysUser;

import java.util.List;

public interface WyOtherService {

	BaseDto bindAgentCode(String companyId, TcAgentCodeAndSysUser tcAgentCodeAndSysUser);

	BaseDto delAgentCode(String companyId, List<String> agentCodeIds);

	BaseDto newAgentCode(String companyId, TcAgentCode tcAgentCode);

	BaseDto updateAgentCode(String companyId, TcAgentCode tcAgentCode);

	BaseDto getAgentCodeByUserId(String companyId, String userId);
}
