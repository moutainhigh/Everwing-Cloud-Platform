package com.everwing.coreservice.wy.core.service.impl.configuration.bc.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.CollectionEnum;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.TBcCollectionTotal;
import com.everwing.coreservice.common.wy.entity.configuration.bc.project.TBcProject;
import com.everwing.coreservice.common.wy.service.configuration.bc.project.TBcProjectService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("tBcProjectService")
public class TBcProjectServiceImpl extends Resources implements TBcProjectService{

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPage(String companyId, TBcProject entity) {
		return new BaseDto(this.tBcProjectMapper.listPage(entity), entity.getPage());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto getById(String companyId, String id) {
		BaseDto returnDto = new BaseDto();
		TBcProject project = this.tBcProjectMapper.findById(id);
		if(project == null){
			returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_WARNING,"未找到该项目."));
		}else{
			returnDto.setObj(project);
			returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功."));
		}
		return returnDto;
	}

	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto update(String companyId, TBcProject entity) {
		if(CommonUtils.isEmpty(entity)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"传入数据为空,修改失败."));
		}
		this.tBcProjectMapper.update(entity);
		
		this.tBcUnionCollectionHeadMapper.updateShopNum(entity.getProjectId(), entity.getShopNum(), entity.getModifyId());
		
		
		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"修改成功."));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto findDataPerYear(String companyId, TBcProject entity) {
		
		BaseDto dto = new BaseDto();
		Map<String,Object> dataMap = new HashMap<String, Object>();
		dataMap.put("union", findData(entity, CollectionEnum.type_union.getV()));
		dataMap.put("jrl", findData(entity, CollectionEnum.type_jrl.getV()));
		dataMap.put("total",this.tBcCollectionMapper.findTotalPerYear(entity));
		dto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
		dto.setObj(dataMap);
		return dto;
	}
	
	private List<Map<String,Object>> findData(TBcProject entity , Integer type){
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		TBcCollectionTotal total = new TBcCollectionTotal();
		total.setSearchTime(entity.getSearchTime());
		total.setProjectId(entity.getProjectId());
		total.setCollectionType(type);
		returnList.add(this.tBcCollectionMapper.findIdPerYear(total)); 			//id
		returnList.add(this.tBcCollectionMapper.findFamilyCountPerYear(total)); 	//总户数
		returnList.add(this.tBcCollectionMapper.findTotalAmountPerYear(total)); 	//总金额
		returnList.add(this.tBcCollectionMapper.findCompleteCountPerYear(total)); 	//成功户数
		returnList.add(this.tBcCollectionMapper.findCompleteAmountPerYear(total));	//成功金额
		returnList.add(this.tBcCollectionMapper.findNotCompleteAmountPerYear(total));	//不成功金额 
		returnList.add(this.tBcCollectionMapper.findStatusPerYear(total)); 	//状态
		return returnList;
	}

	@Override
	public BaseDto getByCode(String companyId, String projectId) {
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(this.tBcProjectMapper.findByProjectId(projectId));
		return returnDto;
	}

	
}
