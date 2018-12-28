package com.everwing.server.wy.web.controller.configuration.tbsassetaccount;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.wy.api.configuration.tbcassetacount.TBsAssetAccountStreamApi;

@Controller
@RequestMapping("/accountStream")
public class TBsAssetAccountStreamController {

	@Autowired
	private TBsAssetAccountStreamApi tBsAssetAccountStreamApi;
	
	@PostMapping("/listPage")
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Billing,businessName="账户流水: [客户维度]列表查询",operationType= OperationEnum.Search)
	public @ResponseBody BaseDto listPage(HttpServletRequest req, @RequestBody TBsAssetAccountStream stream){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBsAssetAccountStreamApi.listPage(ctx.getCompanyId(), stream));
	}
	
}
