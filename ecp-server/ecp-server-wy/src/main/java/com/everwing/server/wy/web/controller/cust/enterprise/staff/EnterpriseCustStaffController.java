package com.everwing.server.wy.web.controller.cust.enterprise.staff;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.staff.EnterpriseCustStaffNew;
import com.everwing.coreservice.wy.api.cust.enterprise.staff.EnterpriseCustStaffApi;

/**
 * @describe 企业客户下  员工业务控制器
 * @author QHC
 *	@date 2017-04-10
 */
@Controller
@RequestMapping(value="/EnterpriseCustStaffNew")
public class EnterpriseCustStaffController {

	@Autowired
	private EnterpriseCustStaffApi enterpreiseCustStaffApi;//控制层调用api接口注入
	
	/**
	 * @describe 分页查询
	 * @param CommonUtils.getCompanyId(req)  所有方法都需要此cookie值用于获取数据源信息 
	 * @author QHC
	 * @return BaseDto
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/listEnterpriseCustStaffPage",method=RequestMethod.POST)
	@ResponseBody
	public BaseDto listEnterpriseCustStaffPage(HttpServletRequest req,@RequestBody EnterpriseCustStaffNew enterpreiseCustStaff){
		return BaseDtoUtils.getDto(this.enterpreiseCustStaffApi.listEnterpriseCustStaffPage(CommonUtils.getCompanyId(req),enterpreiseCustStaff));
	}
	
	/**
	 * @describe 新增
	 * @author QHC
	 * @param CommonUtils.getCompanyId(req)  所有方法都需要此cookie值用于获取数据源信息 
	 * @return MessageMap
	 */
	@RequestMapping(value="/addEnterpriseCustStaff",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap addEnterpriseCustStaff(HttpServletRequest req,@RequestBody EnterpriseCustStaffNew enterpreiseCustStaff){
		return  this.enterpreiseCustStaffApi.addEnterpriseCustStaff(CommonUtils.getCompanyId(req),enterpreiseCustStaff).getModel();
	}
	
	/**
	 * @describe 修改
	 * @param CommonUtils.getCompanyId(req)  所有方法都需要此cookie值用于获取数据源信息 
	 * @author QHC
	 * @return MessageMap
	 */
	@RequestMapping(value="/updateEnterpriseCustStaff",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap updateEnterpriseCustStaff(HttpServletRequest req,@RequestBody EnterpriseCustStaffNew enterpreiseCustStaff){
		return this.enterpreiseCustStaffApi.updateEnterpriseCustStaff(CommonUtils.getCompanyId(req),enterpreiseCustStaff).getModel();
	}
	
	/**
	 * @describe 删除
	 * @param CommonUtils.getCompanyId(req)  所有方法都需要此cookie值用于获取数据源信息 
	 * @author QHC
	 * @return MessageMap
	 */
	@RequestMapping(value="/deleteEnterpriseCustStaff/{ids}",method=RequestMethod.POST)
	@ResponseBody
	public MessageMap deleteEnterpriseCustStaff(HttpServletRequest req,@PathVariable String ids){
		return this.enterpreiseCustStaffApi.deleteEnterpriseCustStaff(CommonUtils.getCompanyId(req),ids).getModel();
	}
	
	
}
