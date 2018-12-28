package com.everwing.server.wy.web.controller.other;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.other.AgentCodeSearch;
import com.everwing.coreservice.common.wy.entity.other.pojo.TcAgentCode;
import com.everwing.coreservice.common.wy.entity.other.pojo.TcAgentCodeAndSysUser;
import com.everwing.coreservice.wy.api.common.WyCommonApi;
import com.everwing.coreservice.wy.api.other.WyOtherApi;

/**
 * 坐席号相关
 */
@Controller
@RequestMapping(value="/agentCode")
@SuppressWarnings("rawtypes")
public class AgentCodeController {
	private Logger logger = LogManager.getLogger(getClass());
	@Autowired
    private WyCommonApi commonApi;
	@Autowired
	private WyOtherApi otherApi;
	
	
	
	@PostMapping("/updateAgentCode")
	public @ResponseBody BaseDto updateAgentCode(@RequestBody TcAgentCode tcAgentCode) {
		return BaseDtoUtils.getDto(otherApi.updateAgentCode(tcAgentCode));
	}
	
	@PostMapping("/newAgentCode")
	public @ResponseBody BaseDto newAgentCode(@RequestBody TcAgentCode tcAgentCode) {
		return BaseDtoUtils.getDto(otherApi.newAgentCode(tcAgentCode));
	}
	
	@PostMapping("/delAgentCodes")
	public @ResponseBody BaseDto delAgentCode(String agentCodeIds) {
		return BaseDtoUtils.getDto(otherApi.delAgentCode(Arrays.asList(agentCodeIds.split(","))));
	}
	
	@GetMapping("/getCurrUserAgentCode")
	public @ResponseBody BaseDto getCurrUserAgentCode() {
		return BaseDtoUtils.getDto(otherApi.getCurrUserAgentCode());
	}
	
	@PostMapping("/bindAgentCode")
	public @ResponseBody BaseDto bindAgentCode(@RequestBody TcAgentCodeAndSysUser tcAgentCodeAndSysUser) {
		return BaseDtoUtils.getDto(otherApi.bindAgentCode(tcAgentCodeAndSysUser));
	}
	
	@PostMapping("/listPageAgentCode")
	public @ResponseBody BaseDto listPageAgentCode(@RequestBody AgentCodeSearch agentCodeSearch) {
		return BaseDtoUtils.getDto(commonApi.listPageAgentCode(agentCodeSearch));
	}
	
	/**
	 * @description 分页查询客服账号
	 */
	@PostMapping("/listPageDatas")
	public @ResponseBody BaseDto listPageDatas(@RequestBody AgentCodeSearch agentCodeSearch) {
		return BaseDtoUtils.getDto(commonApi.listPageCC(agentCodeSearch));
	}

}