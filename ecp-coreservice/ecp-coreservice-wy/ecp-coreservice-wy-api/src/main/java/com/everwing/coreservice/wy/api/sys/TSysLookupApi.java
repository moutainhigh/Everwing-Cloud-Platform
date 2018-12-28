package com.everwing.coreservice.wy.api.sys;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.lookup.*;
import com.everwing.coreservice.common.wy.service.sys.TSysLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TSysLookupApi {

	@Autowired
	private TSysLookupService tSysLookupService;

	/**
	 * 属性主表分页查询
	 * @param condition
	 * @return
	 */
	public RemoteModelResult<BaseDto> listPageLookup(WyBusinessContext ctx, TSysLookupSearch condition){
		BaseDto pageResultDto = tSysLookupService.listPageLookup(ctx,condition);
		RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
		result.setModel(pageResultDto);
		return result;
	}

	/**
	 * 属性主表保存操作
	 * @param entity
	 * @return
	 */
	public RemoteModelResult<MessageMap> saveLookup(WyBusinessContext ctx, TSysLookup entity){
		RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
		result.setModel(tSysLookupService.saveLookup(ctx,entity));
		return result;
	}


	/**
	 * 属性主表分页查询
	 * @param condition
	 * @return
	 */
	public RemoteModelResult<BaseDto> listPageLookupItem(WyBusinessContext ctx, TSysLookupItemSearch condition){
		BaseDto pageResultDto = tSysLookupService.listPageLookupItem(ctx,condition);
		RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
		result.setModel(pageResultDto);
		return result;
	}

	/**
	 * 属性主表保存操作
	 * @param entity
	 * @return
	 */
	public RemoteModelResult<MessageMap> saveLookupItem(WyBusinessContext ctx, TSysLookupItem entity){
		RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
		result.setModel(tSysLookupService.saveLookupItem(ctx,entity));
		return result;
	}


	/**
	 * 根据父编码获取主属性表集合
	 * @param condition
	 * @return
	 */
	public  RemoteModelResult<List<TSysLookupSelectView>> findLookup(WyBusinessContext ctx, TSysLookupSelectSearch condition)  {
		RemoteModelResult<List<TSysLookupSelectView>> result = new RemoteModelResult<>();
		List<TSysLookupSelectView> list = tSysLookupService.findLookup(ctx,condition);
		result.setModel(list);
		return result;
	}



	/**
	 * 根据父编码获取子属性表集合
	 * @param condition
	 * @return
	 */
	public  RemoteModelResult<List<TSysLookupSelectView>> findLookupItem(WyBusinessContext ctx, TSysLookupSelectSearch condition) {
		RemoteModelResult<List<TSysLookupSelectView>> result = new RemoteModelResult<>();
		List<TSysLookupSelectView> list = tSysLookupService.findLookupItem(ctx,condition);
		result.setModel(list);
		return result;
	}

	/**
	 * 主表：根据名称和父级编码获取编码
	 * @param ctx
	 * @param parentCode
	 * @param name
	 * @return
	 */
	public RemoteModelResult<String> getLookupCodeByName(WyBusinessContext ctx, String parentCode, String name){
		RemoteModelResult<String> result = new RemoteModelResult<>();
		String code = tSysLookupService.getLookupCodeByName(ctx,parentCode,name);
		result.setModel(code);
		return result;
	}

	/**
	 * 子表：根据名称和父级编码获取编码
	 * @param ctx
	 * @param parentCode
	 * @param name
	 * @return
	 */
	public RemoteModelResult<String> getLookupItemCodeByName(WyBusinessContext ctx, String parentCode, String name){
		RemoteModelResult<String> result = new RemoteModelResult<>();
		String code = tSysLookupService.getLookupItemCodeByName(ctx,parentCode,name);
		result.setModel(code);
		return result;
	}

	/**
	 * lookup:根据code获取name
	 * @param ctx
	 * @param parentCode 父编码
	 * @param code      子编码
	 * @return
	 */
	public RemoteModelResult<String> getLookupNameByCode(WyBusinessContext ctx, String parentCode, String code){
		RemoteModelResult<String> result = new RemoteModelResult<>();
		String name = tSysLookupService.getLookupNameByCode(ctx,parentCode,code);
		result.setModel(name);
		return result;
	}

	/**
	 * lookupItem:根据code获取name
	 * @param ctx
	 * @param parentCode 父编码
	 * @param code      子编码
	 * @return
	 */
	public RemoteModelResult<String> getLookupItemNameByCode(WyBusinessContext ctx, String parentCode, String code){
		RemoteModelResult<String> result = new RemoteModelResult<>();
		String name = tSysLookupService.getLookupItemNameByCode(ctx,parentCode,code);
		result.setModel(name);
		return result;
	}
}
