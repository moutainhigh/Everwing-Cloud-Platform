package com.everwing.server.wy.web.controller.sys;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.system.lookup.*;
import com.everwing.coreservice.wy.api.sys.TSysLookupApi;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 基础属性控制器
 */
@Controller
@RequestMapping("/system/LookupController")
public class TSysLookupController {

	@Autowired
	private TSysLookupApi tSysLookupApi;
	
	/**
	 * 分页查询方法入口
	 *
	 * @param condition
	 * @return
	 */
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Lookup,businessName="查询属性主表列表",operationType= OperationEnum.Search)
	@RequestMapping(value = "/listPageLookup",method =RequestMethod.POST)
	public @ResponseBody  BaseDto  listPageLookup(HttpServletRequest request,@RequestBody TSysLookupSearch condition){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();

		WyBusinessContext ctx = WyBusinessContext.getContext();

		RemoteModelResult<BaseDto> result = tSysLookupApi.listPageLookup(ctx,condition);
		if(result.isSuccess()){
			mm.setFlag(MessageMap.INFOR_SUCCESS);
			baseDto = result.getModel();
		}else{
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage(result.getMsg());
		}
		baseDto.setMessageMap(mm);
		return baseDto;
	}

	/**
	 * 保存方法入口
	 * @param entity
	 * @return
	 */
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Lookup,businessName="新增或修改属性主表",operationType= OperationEnum.Save)
	@RequestMapping(value="/saveLookup",method =RequestMethod.POST)
	public @ResponseBody  MessageMap saveLookup(HttpServletRequest request,@RequestBody TSysLookup entity){
		MessageMap mm = new MessageMap();

		WyBusinessContext ctx = WyBusinessContext.getContext();
		entity.setLan(ctx.getLan());
		RemoteModelResult<MessageMap> result = tSysLookupApi.saveLookup(ctx,entity);
		if(result.isSuccess()){
			mm = result.getModel();
		}else{
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage(result.getMsg());
		}
		return mm;
	}


	/**
	 * 属性子表分页查询方法入口
	 *
	 * @param condition
	 * @return
	 */
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Lookup,businessName="查询属性子表列表",operationType= OperationEnum.Search)
	@RequestMapping(value = "/listPageLookupItem",method =RequestMethod.POST)
	public @ResponseBody  BaseDto  listPageLookupItem(HttpServletRequest request,@RequestBody TSysLookupItemSearch condition){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();

		WyBusinessContext ctx = WyBusinessContext.getContext();

		RemoteModelResult<BaseDto> result = tSysLookupApi.listPageLookupItem(ctx,condition);
		if(result.isSuccess()){
			mm.setFlag(MessageMap.INFOR_SUCCESS);
			baseDto = result.getModel();
		}else{
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage(result.getMsg());
		}
		baseDto.setMessageMap(mm);
		return baseDto;
	}

	/**
	 * 属性子表保存方法入口
	 * @param entity
	 * @return
	 */
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Lookup,businessName="新增或修改属性子表",operationType= OperationEnum.Save)
	@RequestMapping(value="/saveLookupItem",method =RequestMethod.POST)
	public @ResponseBody  MessageMap saveLookupItem(HttpServletRequest request,@RequestBody TSysLookupItem entity){
		MessageMap mm = new MessageMap();

		WyBusinessContext ctx = WyBusinessContext.getContext();
		entity.setLan(ctx.getLan());
		RemoteModelResult<MessageMap> result = tSysLookupApi.saveLookupItem(ctx,entity);
		if(result.isSuccess()){
			mm = result.getModel();
		}else{
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage(result.getMsg());
		}
		return mm;
	}



	/**
	 * 根据父级编码获取子属性表集合
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/findLookupItem",method =RequestMethod.POST)
	public @ResponseBody  BaseDto  findLookupItem(HttpServletRequest request,@RequestBody TSysLookupSelectSearch condition){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();

		WyBusinessContext ctx = WyBusinessContext.getContext();

		List<TSysLookupItemList> tSysLookupItemLists = DataDictionaryUtil.getLookupItemListByLookupCodeAndParentCode(ctx.getCompanyId(),condition.getLookupCode(),condition.getParentCode());
		if(CollectionUtils.isNotEmpty(tSysLookupItemLists)){
			baseDto.setLstDto(tSysLookupItemLists);
		}else{
			mm.setFlag(MessageMap.INFOR_WARNING);
			mm.setMessage("没有找到记录");
		}
		baseDto.setMessageMap(mm);
		return baseDto;
	}
	
}