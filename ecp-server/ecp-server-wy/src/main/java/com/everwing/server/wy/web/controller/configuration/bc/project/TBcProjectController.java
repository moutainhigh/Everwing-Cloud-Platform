package com.everwing.server.wy.web.controller.configuration.bc.project;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.wy.api.configuration.bc.project.TBcProjectApi;


@Controller
@RequestMapping("/bc/project")
public class TBcProjectController {

	
	@Autowired
	private TBcProjectApi tBcProjectApi; 
	
	
	/**
	 * @TODO 分页显示
	 * @param req
	 * @param entity
	 * @return
	 */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="查询托收项目列表",operationType= OperationEnum.Search)
	@PostMapping("/listPage")
	public @ResponseBody BaseDto listPage(HttpServletRequest req , @RequestBody TBcProject entity){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBcProjectApi.listPage(ctx.getCompanyId(),entity));
	}
    
    /**
     * @TODO 单个详情
     * @param req
     * @param id
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="查询托收项目详情",operationType= OperationEnum.Search)
    @GetMapping("/getById/{id}")
    public @ResponseBody BaseDto getById(HttpServletRequest req , @PathVariable String id){
    	WyBusinessContext ctx = WyBusinessContext.getContext();
    	return BaseDtoUtils.getDto(this.tBcProjectApi.getById(ctx.getCompanyId(),id));
    }
    
    /**
     * 修改
     * @param req
     * @param entity
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="修改托收项目详情",operationType= OperationEnum.Modify)
    @PostMapping("/update")
    public @ResponseBody BaseDto getById(HttpServletRequest req , @RequestBody TBcProject entity){
    	WyBusinessContext ctx = WyBusinessContext.getContext();
    	return BaseDtoUtils.getDto(this.tBcProjectApi.update(ctx.getCompanyId(),entity)); 
    }
    
    
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="查看托收数据",operationType= OperationEnum.Search)
    @PostMapping("/findDataPerYear")
    public @ResponseBody BaseDto findDataPerYear(HttpServletRequest req , @RequestBody TBcProject entity){
    	WyBusinessContext ctx = WyBusinessContext.getContext();
    	return BaseDtoUtils.getDto(this.tBcProjectApi.findDataPerYear(ctx.getCompanyId(),entity)); 
    }
    
	
	
	
}
