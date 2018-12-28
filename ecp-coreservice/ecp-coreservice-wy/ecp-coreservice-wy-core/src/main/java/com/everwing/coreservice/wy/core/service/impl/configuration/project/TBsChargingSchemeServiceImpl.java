package com.everwing.coreservice.wy.core.service.impl.configuration.project;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsChargingSchemeService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("tBsChargingSchemeServiceImpl")
public class TBsChargingSchemeServiceImpl extends Resources implements TBsChargingSchemeService {
	
	private static final Logger logger = Logger.getLogger(TBsChargingSchemeServiceImpl.class);

	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap addChargingScheme(String companyId, TBsChargingScheme entity) {
		if(CommonUtils.isEmpty(entity)){
			return new MessageMap(MessageMap.INFOR_ERROR,"参数为空，请重试");
		}
		//查询校验
		if(hasConfilcScheme(entity)){
			return new MessageMap(MessageMap.INFOR_WARNING,"已经存在生效中方案,请修改当前方案为停用状态");
		}
		int num=this.tBsChargingSchemeMapper.insert(entity);
		if(1 == num){
			return new MessageMap(MessageMap.INFOR_SUCCESS,"新增方案成功!");
		}
		return new MessageMap(MessageMap.INFOR_ERROR,"新增方案失败!");
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto getChargingScheme(WyBusinessContext ctx, String schemeType,
									 String projectId) {
		MessageMap msgMap = new MessageMap();
		BaseDto baseDto = new BaseDto();
		try {
			TBsChargingScheme tBsChargingScheme = this.tBsChargingSchemeMapper.getChargSchByTypeAndProjectId(schemeType, projectId);
			baseDto.setObj(tBsChargingScheme);
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
		} catch (Exception e) {
			logger.info(e.getMessage());
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("查找失败!");
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto editedScheme(String companyId, TBsChargingScheme scheme) {
		
		//判断当前scheme是否含有id,若含有id,则表示该scheme为修改保存
		if(CommonUtils.isEmpty(scheme.getId())){
			//新增
			//判断该scheme的生效日期,启用状态,以及现在是否存在已经启用的scheme
			if(hasConfilcScheme(scheme)){
				return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"已经存在生效中方案,请修改当前方案为停用状态"));
			}
			//若当前scheme不为启用状态,直接保存即可
			this.tBsChargingSchemeMapper.insert(scheme);
		}else{
			//修改保存,在此处保存的都是启用中的scheme,直接保存即可
			if(hasConfilcScheme(scheme)){
				return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"已经存在生效中方案,且与当前方案执行时间冲突,当前方案无法保存"));
			}
			this.tBsChargingSchemeMapper.updateSchemeInfo(scheme);
		}
		logger.info("保存scheme成功.");
		return new BaseDto(new MessageMap(null,"保存成功"));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto  listPageSchemes(String companyId,TBsChargingScheme scheme){
		return new BaseDto(this.tBsChargingSchemeMapper.listPageSchemes(scheme), scheme.getPage());
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto findUsingScheme(String companyId,TBsChargingScheme scheme){
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(this.tBsChargingSchemeMapper.findUsingScheme(scheme));
		return returnDto;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto stopSchemes(String companyId,List<TBsChargingScheme> schemes) {
		if(CommonUtils.isEmpty(schemes)){
			logger.warn("传入参数为空.");
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"传入参数为空."));
		}
		this.tBsChargingSchemeMapper.stopSchemes(schemes);
		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS, "停用成功."));
	}
	
	
	private boolean hasConfilcScheme(TBsChargingScheme scheme){
		if(scheme == null) return true;
		if(scheme.getIsUsed() == 0) {
			List<TBsChargingScheme> isUsedSchemes = this.tBsChargingSchemeMapper.findConfilcScheme(scheme);
			if(CommonUtils.isNotEmpty(isUsedSchemes)){
				logger.info("当前已存在生效中scheme" + isUsedSchemes.get(0).toString());
				return true;
			}
		}
		return false;
	}
	
	
	
}
