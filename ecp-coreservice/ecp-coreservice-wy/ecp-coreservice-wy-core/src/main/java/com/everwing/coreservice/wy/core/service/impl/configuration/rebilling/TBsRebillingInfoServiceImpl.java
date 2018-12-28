package com.everwing.coreservice.wy.core.service.impl.configuration.rebilling;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.service.configuration.rebilling.TBsRebillingInfoService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("tBsRebillingInfoService")
public class TBsRebillingInfoServiceImpl extends Resources implements TBsRebillingInfoService{

	private static final Logger logger = LogManager.getLogger(TBsRebillingInfoServiceImpl.class);
	
	@Value("${queue.wy2Wy.single.rebilling.key}")
	private String route_key_manaul_rebilling;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageInfos(String companyId, TBsRebillingInfo entity) {
		return new BaseDto(this.tBsRebillingInfoMapper.listPage(entity), entity.getPage());
	}

	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto rebilling(String companyId, TBsRebillingInfo entity,Integer type) {
		MessageMap msgMap = null;
		
		if(CommonUtils.paramsHasNull(entity,entity.getBuildingCode(),entity.getFullName(),entity.getRebillingStartTime()) 
				|| entity.getType() == null || entity.getType() < 0 || entity.getType() > 5){
			msgMap = new MessageMap(MessageMap.INFOR_WARNING,"传入参数为空,无法重新计费.");
			logger.warn("手动重新计费: 传入参数为空,无法重新计费. 传入参数: {}",(entity == null) ? "" : entity.toString());
		}else{
			
			//检验传入的buildingCode是否含有非计费对象
			List<String> buildingCodes = CommonUtils.str2List(entity.getBuildingCode(), Constants.STR_COMMA);
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("buildingCodes", buildingCodes);
			paramMap.put("projectId", entity.getProjectId());
			paramMap.put("isChargeObj", LookupItemEnum.yesNo_yes.getStringValue());
			List<TcBuilding> buildings = this.tcBuildingMapper.findByParams(paramMap);
			
			if(buildings.isEmpty()){
				msgMap = new MessageMap(MessageMap.INFOR_WARNING,"所有建筑都为非计费节点,无法重新计费.");
				logger.warn("手动重新计费: 所有建筑都为非计费节点,无法重新计费. 数据:{}.",entity.getBuildingCode());
				return new BaseDto(msgMap);
			}else if(buildings.size() < buildingCodes.size()){
				msgMap = new MessageMap(MessageMap.INFOR_SUCCESS,"有部分建筑为非计费节点,无法重新计费. ");
				logger.warn("手动重新计费: 有部分建筑为非计费节点. 传入数据: {}, 可计费数据: {}.",entity.getBuildingCode(), JSON.toJSONString(buildings));
			}else {
				msgMap = new MessageMap(null,"手动重新计费: 所有建筑节点都符合重新计费条件. ");
			}
			logger.info("手动重新计费: 将可计费数据投递至消息队列. 开始组装数据. ");
			MqEntity me = new MqEntity();
			me.setCompanyId(companyId);
			me.setOpr(type);
			//投递至 SingleRebillingListener
			if(entity.getType() == 5){
				//全部进行重新计费,丢四次消息队列
				for(int i = 1 ; i < 5 ; i++){
					entity.setType(i);
					me.setData(entity);
					this.amqpTemplate.convertAndSend(route_key_manaul_rebilling, me);
					logger.info("手动重新计费: 将消息投递到消息队列完成. routeKey:{},数据:{}",route_key_manaul_rebilling,me.toString());
				}
			}else{
				me.setData(entity);
				this.amqpTemplate.convertAndSend(route_key_manaul_rebilling, me);
				logger.info("手动重新计费: 将消息投递到消息队列完成. routeKey:{},数据:{}",route_key_manaul_rebilling,me.toString());
			}
			msgMap.setMessage(msgMap.getMessage().concat(" 开始异步计费 , 详细结果请稍后查看. "));
		}
		return new BaseDto(msgMap);
	}

	
	
	
}
