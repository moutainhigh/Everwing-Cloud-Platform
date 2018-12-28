package com.everwing.server.wy.web.controller.business.pay;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.wy.api.configuration.payinfo.PushPayInfoToFinanceApi;


@Controller
@RequestMapping("/pushPayInfo")
public class TBsPushPayInfoToFince {

	
	@Autowired
	private PushPayInfoToFinanceApi pushPayInfoToFinanceApi;
	
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/pushPayInfoToFince")
	@ResponseBody
	public BaseDto  pushPayInfoToFince(HttpServletRequest req ){
		return BaseDtoUtils.getDto(pushPayInfoToFinanceApi.doPushPayInfo("09841dc0-204a-41f2-a175-20a6dcee0187"));
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/tellWCPushEnd")
	@ResponseBody
	public BaseDto  tellWCPushEnd(HttpServletRequest req ){
		return BaseDtoUtils.getDto(pushPayInfoToFinanceApi.tellWCPushEnd("09841dc0-204a-41f2-a175-20a6dcee0187"));
	}
	
	
	
}
