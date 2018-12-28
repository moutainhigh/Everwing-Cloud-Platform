package com.everwing.server.wy.web.controller.project.gating;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everwing.coreservice.common.platform.entity.generated.MkjLog;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.platform.api.PlatformGatingApi;

@Controller
@RequestMapping("/gatingLogs")
public class GatingLogsController {

	
	@Autowired
	private PlatformGatingApi platformGatingApi;
	
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Gating,businessName="门控机日志查询: 操作日志查询",operationType= OperationEnum.Search)
    @PostMapping("/listPageOprLogs")
    public @ResponseBody BaseDto listPageOprLogs(HttpServletRequest req, @RequestBody MkjLog log){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.platformGatingApi.queryLogsByObj(log));
		
		
    }
	
	
	
	
	
}
