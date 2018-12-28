package com.everwing.server.wy.web.controller.order;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.wy.api.order.TcOrderCompleteApi;

/**
 * 工单类型
 */
@Controller
@RequestMapping(value = "/orderType")
@SuppressWarnings("rawtypes")
public class TcOrderTypeController {

	@Autowired
	private TcOrderCompleteApi tcOrderApi;

	@GetMapping("/subTypeList")
	public @ResponseBody BaseDto subType(String typeId) {
		return StringUtils.isBlank(typeId) ? BaseDtoUtils.getDto(tcOrderApi.levelOneType())
				: BaseDtoUtils.getDto(tcOrderApi.subType(typeId));
	}

}