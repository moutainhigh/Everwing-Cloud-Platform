package com.everwing.server.wy.web.controller.delivery;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.delivery.TJgStaffGrop;
import com.everwing.coreservice.wy.api.delivery.TJgStaffGropApi;

/***
 * @describe  银账交割的员工组表实现
 * @author qhc
 * @ date 2017-08-31 
 */
@Controller
@RequestMapping(value="/staffGrop")
public class TJgStaffGropController{
	
	@Autowired
	private TJgStaffGropApi tJgStaffGropApi;
	
	/**
	 * @describe 新增一条员工组信息
	 * @author QHC
	 * @date 2017-09-01
	 */
	@WyOperationLogAnnotation(moduleName= OperationEnum.Modele_BankDelivery,businessName="[银账交割] : 添加银账交割组织结构成员",operationType= OperationEnum.Insert)
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/addStaffGrop",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto addStaffGrop(HttpServletRequest req,@RequestBody TJgStaffGrop entity){
		return BaseDtoUtils.getDto(this.tJgStaffGropApi.addStaffGrop(CommonUtils.getCompanyId(req), entity));
	} 
	
	/**
	 * @describe 通过当前登录用户userid查询其上级信息
	 * @author QHC
	 * @date 2017-09-01
	 */
	@WyOperationLogAnnotation(moduleName= OperationEnum.Modele_BankDelivery,businessName="[银账交割] : 查询当前登录用户以及上级信息",operationType= OperationEnum.Search)
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@RequestMapping(value="/getPidInfoByUserId/{userId}/{projectId}",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto getPidInfoByUserId(HttpServletRequest req,@PathVariable String userId,@PathVariable String projectId){
		return tJgStaffGropApi.getPidInfoByUserId(CommonUtils.getCompanyId(req), userId,projectId).getModel();
	}
	
	
	/**
	 * @describe 通过当前登录用户userid自己信息
	 * @author QHC
	 * @date 2017-09-01
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@RequestMapping(value="/getMyselfInfoByUserId/{userId}/{projectId}",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto getMyselfInfoByUserId(HttpServletRequest req,@PathVariable String userId,@PathVariable String projectId){
		return tJgStaffGropApi.getMyselfInfoByUserId(CommonUtils.getCompanyId(req), userId,projectId).getModel();
	}
	
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@RequestMapping(value="/getProjectListForRole/{userId}",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto getProjectListForRole(HttpServletRequest req,@PathVariable String userId){
		return tJgStaffGropApi.getProjectListForRole(CommonUtils.getCompanyId(req), userId).getModel();
	}
	
	
	/**
	 * @describe 通过当前登录用户userid查询其组织结构
	 * @author QHC
	 * @date 2017-09-01
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@RequestMapping(value="/getStaffGroupInfo/{userId}/{projectId}",method=RequestMethod.GET)
	@ResponseBody
	public BaseDto getStaffGroupInfo(HttpServletRequest req,@PathVariable String userId,@PathVariable String projectId){
		return tJgStaffGropApi.getStaffGroupInfo(CommonUtils.getCompanyId(req), userId,projectId).getModel();
	}
	
	
	
	/**
	 * @describe 删除子节点
	 * @date 2017年9月4日11:32:13
	 */
    @SuppressWarnings({ "deprecation", "rawtypes" })
	@WyOperationLogAnnotation(moduleName= OperationEnum.Modele_BankDelivery,businessName="[银账交割] : 删除组织结构成员",operationType= OperationEnum.Delete)
	@GetMapping(value="/delById/{userId}")
	@ResponseBody
	public BaseDto delById(HttpServletRequest req,@PathVariable String id){
		return BaseDtoUtils.getDto(this.tJgStaffGropApi.delById(CommonUtils.getCompanyId(req),id));
	}
    
    /**
     * @describe 页面点击获取数据. 传入数据: project_id, user_id
     * @date 2017年9月4日11:32:44
     */
    @SuppressWarnings({ "deprecation", "rawtypes" })
    @WyOperationLogAnnotation(moduleName= OperationEnum.Modele_BankDelivery,businessName="[银账交割] : 查询某节点的父节点以及其下所有子节点",operationType= OperationEnum.Search)
    @PostMapping(value="/findInfos")
    @ResponseBody
    public BaseDto findInfos(HttpServletRequest req,@RequestBody TJgStaffGrop entity){
    	return BaseDtoUtils.getDto(this.tJgStaffGropApi.findInfos(CommonUtils.getCompanyId(req),entity)); 
    }
    
    /**
     * @describe 页面点击获取数据加载成树. 传入数据: project_id
     * @date 2017年9月4日11:32:44
     */
    @SuppressWarnings({ "deprecation", "rawtypes" })
    @WyOperationLogAnnotation(moduleName= OperationEnum.Modele_BankDelivery,businessName="[银账交割] : 查询项目树角色",operationType= OperationEnum.Search)
    @GetMapping(value="/loadInfosToTree/{projectId}")
    @ResponseBody
    public BaseDto loadInfosToTree(HttpServletRequest req,@PathVariable String projectId){
    	return BaseDtoUtils.getDto(this.tJgStaffGropApi.loadInfosToTree(CommonUtils.getCompanyId(req),projectId)); 
    }
    /**
     * @describe 页面点击获取数据加载成树. 传入数据: project_id
     * @date 2017年9月4日11:32:44
     */
    @SuppressWarnings({ "deprecation", "rawtypes" })
    @WyOperationLogAnnotation(moduleName= OperationEnum.Modele_BankDelivery,businessName="[银账交割] : 删除节点以及其下所有子节点",operationType= OperationEnum.Delete)
    @GetMapping(value="/delStaffGropAndChildren/{id}")
    @ResponseBody
    public BaseDto delStaffGropAndChildren(HttpServletRequest req,@PathVariable String id){
    	return BaseDtoUtils.getDto(this.tJgStaffGropApi.delStaffGropAndChildren(CommonUtils.getCompanyId(req),id)); 
    }

}
