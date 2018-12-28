package com.everwing.coreservice.wy.dao.mapper.other;

import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

public interface OtherMapper {
	
	@Select("SELECT ac.agent_code FROM tc_agent_code_and_sys_user ac_su JOIN tc_agent_code ac ON ac_su.agent_code_id = ac.agent_code_id WHERE ac_su.sys_user_id = #{userId}")
	public String getAgentCodeByUserId(@PathVariable("userId") String userId);
}
