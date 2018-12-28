package com.everwing.coreservice.wy.core.service.impl.configuration.project;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.ThreadPool.ThreadPoolUtils;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.datasource.DataSourceUtil;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingRules;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsGetRuleBuilding;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsRuleBuildingRelation;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsWaterElectBillingService;
import com.everwing.coreservice.platform.api.CompanyApi;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargeTypeMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargingRulesMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsRuleBuildingRelationMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("tBsWaterElectBillingServiceImpl")
public class TBsWaterElectBillingServiceImpl implements TBsWaterElectBillingService {

	@Autowired
	private TBsChargingRulesMapper tBsChargingRulesMapper;
	@Autowired
	private TBsRuleBuildingRelationMapper tBsRuleBuildingRelationMapper;
	@Autowired
	private TBsChargeTypeMapper tBsChargeTypeMapper;
	@Autowired
	private TcBuildingMapper tcBuildingMapper;

	@Autowired
	private CompanyApi companyApi;

	private static final Logger logger = Logger.getLogger(TBsWaterElectBillingServiceImpl.class);

	//用于递归时存放找到的父节点
//    private List<Map<String,Object>> objList = new ArrayList<Map<String,Object>>();

    @Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap addOrEditRuleInfo(final String companyId,final TBsChargingRules entity) throws ECPBusinessException{
		MessageMap msgMap = new MessageMap();
		List<TBsRuleBuildingRelation> tbsRealistNew =null;
		if(CommonUtils.isEmpty(entity)){
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("参数为空，请重试");
			return msgMap;
		}else{
			 tbsRealistNew = entity.getTbsRealtionList();
			 if(CollectionUtils.isEmpty(tbsRealistNew)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("关联建筑不能为空");
					return msgMap;
				}
		}
		//水电表的规则表  同一个方案下的规则名称不能相同
//		List<TBsChargingRules> list=this.tBsChargingRulesMapper.listPageRulesBySchemeId(entity);
		if(StringUtils.isBlank(entity.getId())){  //编号为空则是新增，新增需要校验规则名称是否为重复
			TBsChargingRules tBsChRules = this.tBsChargingRulesMapper.getByNameAndSchemId(entity.getRuleName(), entity.getChargingSchemeId());
			if(CommonUtils.isNotEmpty(tBsChRules)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("规则名称重复，请修改!");
				return msgMap;
			}
		}

		RemoteModelResult<Company> rslt =  this.companyApi.queryCompany(companyId);
		if(!rslt.isSuccess() || rslt.getModel() == null){
			logger.warn("切换数据源失败。");
			return new MessageMap(MessageMap.INFOR_WARNING,"切换数据源失败。");
		}
		final String companyStr = JSON.toJSONString(rslt.getModel());

		//这里由于对于很多个建筑的删除和插入肯恩会导致dubbo超时，所以起一个线程处理
		ThreadPoolUtils.getInstance().executeThread(new Runnable() {
			@Override
			public void run() {

				DataSourceUtil.changeDataSource(companyStr);
				//切换完成之后，重新去拿Mapper
//				TBsRuleBuildingRelationMapper tBsRuleBuildingRelationMapper = ((TBsRuleBuildingRelationMapper) SpringContextHolder.getBean("TBsRuleBuildingRelationMapper"));
//				TBsChargingRulesMapper tBsChargingRulesMapper = ((TBsChargingRulesMapper) SpringContextHolder.getBean("TBsChargingRulesMapper"));
				try {
					saveScheme(entity);
				} catch (Exception e) {
					logger.info(getLogStr(e.getMessage()));
				}

			}
		});
		msgMap.setFlag(MessageMap.INFOR_SUCCESS);
		msgMap.setMessage("异步保存成功,请稍后查看详情");
		return msgMap;
	}

	@Transactional(rollbackFor=Exception.class)
	private MessageMap saveScheme(TBsChargingRules entity) throws ECPBusinessException{
		MessageMap msgMap = new MessageMap();
		List<String> tbsRealistOld = null;
		List<TBsRuleBuildingRelation> machlist = new ArrayList<TBsRuleBuildingRelation>(); //用来存放设置了ChargingRuleId的建筑关联对象
//		throw new ECPBusinessException("className.methodName.msg1");
		try {
			//根据规则编码查找关联建筑
//			List<TBsRuleBuildingRelation> tbsRealistOld= this.tBsRuleBuildingRelationMapper.selectAssetsByRuleId(entity.getId());
			if(StringUtils.isNotBlank(entity.getId())){
				tbsRealistOld = this.tBsRuleBuildingRelationMapper.selectIdsByRuleId(entity.getId());
			}
			List<TBsRuleBuildingRelation> tbsReaNewlist = entity.getTbsRealtionList();
			 int commitSize = 500;//默认每次提交数量
				//保存规则
				 if(StringUtils.isBlank(entity.getId())){ //新增
					   entity.setId(CommonUtils.getUUID());
					   this.tBsChargingRulesMapper.insertRule(entity);
					 logger.info(String.format("当前时间 : %s , 详情  -> %s" ,CommonUtils.getDateStr(),"新增规则成功"));
				   }else{ //编辑保存
					   this.tBsChargingRulesMapper.updateChargeRuleType(entity);
					 logger.info(String.format("当前时间 : %s , 详情  -> %s" ,CommonUtils.getDateStr(),"修改规则成功"));
				   }
				 //给关联建筑对象设置ChargingRuleId
				for(TBsRuleBuildingRelation tb : tbsReaNewlist){
					//后来发现这种做法不行，因为水费的计费规则也电费的计费规则记录在同一张表里，所以这样做的话，这一个收费对象只能记一种费用
//					TBsRuleBuildingRelation  tBsRuleBuildingRelation = this.tBsRuleBuildingRelationMapper.findByBuildCodeAndType(tb.getRelationBuildingCode(), tb.getRelationBuildingType());
//					if(CommonUtils.isNotEmpty(tBsRuleBuildingRelation)) continue; //如果这个建筑收费对象已经在规则关联表存在则跳过，因为一个收费对象在规则关系表是唯一的。一个收费对象只存在一个计费规则里
					tb.setChargingRuleId(entity.getId());
					machlist.add(tb);
				}
				if(CollectionUtils.isEmpty(machlist)){
					throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
				}
				if(!CollectionUtils.isEmpty(tbsRealistOld)){
					//删除关联建筑关系
					int sizeIds = tbsRealistOld.size();
					if(sizeIds <=commitSize){
						this.tBsRuleBuildingRelationMapper.batchDelRules(tbsRealistOld);
						logger.info(String.format("当前时间 : %s , 详情  -> %s" ,CommonUtils.getDateStr(),"删除了["+sizeIds+"]条建筑关联关系!"));
					}else{
						 if(sizeIds % commitSize == 0){
			        		   int count = sizeIds / commitSize;
			        		   for(int i=0;i<count;i++){
			                        int fromIndex = i * commitSize;
			                        int toIndex = (i+1) * commitSize;
			                        this.tBsRuleBuildingRelationMapper.batchDelRules(tbsRealistOld.subList(fromIndex,toIndex));
								   logger.info(String.format("当前时间 : %s , 详情  -> %s" ,CommonUtils.getDateStr(),"删除了["+commitSize+"]条建筑关联关系!"));
			                    }
					}else{
						 int endIndex = 0;
		                    int count = sizeIds / commitSize;
		                    for(int i=0;i<count;i++){
		                        int fromIndex = i * commitSize;
		                        int toIndex = (i+1) * commitSize;
		                        endIndex = toIndex;
		                        this.tBsRuleBuildingRelationMapper.batchDelRules(tbsRealistOld.subList(fromIndex,toIndex));
								logger.info(String.format("当前时间 : %s , 详情  -> %s" ,CommonUtils.getDateStr(),"删除了["+commitSize+"]条建筑关联关系!"));
		                    }
		                    this.tBsRuleBuildingRelationMapper.batchDelRules(tbsRealistOld.subList(endIndex,sizeIds));
							 logger.info(String.format("当前时间 : %s , 详情  -> %s" ,CommonUtils.getDateStr(),"删除了["+(sizeIds-endIndex)+"]条建筑关联关系!"));
						}
					}
				}
				//保存建筑关系
				 int size = machlist.size();
				 if(size <= commitSize){
					 this.tBsRuleBuildingRelationMapper.batchInsert(machlist);
					 logger.info(String.format("当前时间 : %s , 详情  -> %s" ,CommonUtils.getDateStr(),"保存了["+(size)+"]条建筑关联关系!"));
		           }else{
		        	   if(size % commitSize == 0){
		        		   int count = size / commitSize;
		        		   for(int i=0;i<count;i++){
		                        int fromIndex = i * commitSize;
		                        int toIndex = (i+1) * commitSize;
		                        this.tBsRuleBuildingRelationMapper.batchInsert(machlist.subList(fromIndex,toIndex));
							   logger.info(String.format("当前时间 : %s , 详情  -> %s" ,CommonUtils.getDateStr(),"保存了["+(commitSize)+"]条建筑关联关系!"));
		                    }
		        	   }else{
		        		   int endIndex = 0;
		                    int count = size / commitSize;
		                    for(int i=0;i<count;i++){
		                        int fromIndex = i * commitSize;
		                        int toIndex = (i+1) * commitSize;
		                        endIndex = toIndex;
		                        this.tBsRuleBuildingRelationMapper.batchInsert(machlist.subList(fromIndex,toIndex));
								logger.info(String.format("当前时间 : %s , 详情  -> %s" ,CommonUtils.getDateStr(),"保存了["+(commitSize)+"]条建筑关联关系!"));
		                    }
		                    this.tBsRuleBuildingRelationMapper.batchInsert(machlist.subList(endIndex,size));
						   logger.info(String.format("当前时间 : %s , 详情  -> %s" ,CommonUtils.getDateStr(),"保存了["+(size-endIndex)+"]条建筑关联关系!"));
		        	   }
		           }
				msgMap.setFlag(MessageMap.INFOR_SUCCESS);
				msgMap.setObj(entity.getId());//新插入规则的id，页面上需要使用
				msgMap.setMessage("保存成功!");
				return msgMap;

		} catch (Exception e) {
			logger.info(getLogStr(e.getMessage()));
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("保存失败！");

			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageRulesBySchemeId(String companyId, TBsChargingRules entity) {
		return new BaseDto(this.tBsChargingRulesMapper.listPageRulesBySchemeId(entity),entity.getPage());
	}


	@Override
	@Transactional(rollbackFor=Exception.class)
	public MessageMap deleteRulesByIds(String companyId, String ids) {
		//这里删除规则需要同时删除规则下的关联建筑信息，删除规则下的收费类别信息
		if(CommonUtils.isEmpty(ids)){
			return new MessageMap(MessageMap.INFOR_ERROR,"参数为空无法执行删除！");
		}
		int num=0;
		//删除关联建筑信息
		List<String> ruleIds=CommonUtils.str2List(ids, ",");
		for (String ruleId : ruleIds) {
			this.tBsRuleBuildingRelationMapper.deleteRelationBuilding(ruleId);
			this.tBsChargeTypeMapper.deleteByRuleId(ruleId);
		}

		num=this.tBsChargingRulesMapper.batchDelRules(ruleIds);
		if(num>0){
			return new MessageMap(MessageMap.INFOR_SUCCESS,"删除操作执行成功");
		}
		return new MessageMap(MessageMap.INFOR_ERROR,"删除操作执行失败");
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public MessageMap updateRule(String companyId, TBsChargingRules entity) {
		if(CommonUtils.isEmpty(entity) || CommonUtils.isEmpty(entity.getId())){
			return new MessageMap(MessageMap.INFOR_ERROR,"参数不允许为空！");
		}
		int num= this.tBsChargingRulesMapper.updateChargeRuleType(entity);
		if(num > 0) return new MessageMap(MessageMap.INFOR_SUCCESS,"修改操作成功！");
		return new MessageMap(MessageMap.INFOR_SUCCESS,"修改操作失败！");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto getRuleById(WyBusinessContext ctx, String Id) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			List<TBsChargingRules> tBsChargingRules = this.tBsChargingRulesMapper.getById(Id);
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			baseDto.setLstDto(tBsChargingRules);
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			logger.info("根据规则编号查询规则异常:"+e.getMessage());
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}



	/**
	 * 查询过滤后的建筑
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BaseDto getFilterZeeTree(WyBusinessContext ctx,TBsGetRuleBuilding ruleBuilding)throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {

			List<Map<String,Object>> result = this.tBsChargingRulesMapper.getFilterZeeTree(ruleBuilding);
			//对于还有子节点的父节点不能过滤掉
//			this.tBsRuleBuildingRelationMapper.getRelationByRuleIdAndType(ruleId, buildingType)
			//清空全局变量
//			if(objList.size()>0){
//				for(int i=0;i<objList.size();i++){
//					objList.remove(objList.get(i));
//				}
//			}
//			List<Map<String,Object>> list =notFilterHasChildFatger(chargingSchemeId);
//			if(CollectionUtils.isNotEmpty(result) && CollectionUtils.isNotEmpty(list)){
//				result.addAll(list);
//			}
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			baseDto.setLstDto(result);
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			logger.info(String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(),e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}

	@Override
	public BaseDto getReaBuildByChargRuleId(WyBusinessContext ctx,
			TBsRuleBuildingRelation tbsRuleRelation) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			List<TBsRuleBuildingRelation>  result =tBsRuleBuildingRelationMapper.getReaBuildByChargRuleId(tbsRuleRelation);
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			baseDto.setLstDto(result);
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			logger.info(String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(),e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}


//	private List<Map<String,Object>> notFilterHasChildFatger(String chargingSchemeId){
//		//查规则
//		List<TBsChargingRules> ruleList = this.tBsChargingRulesMapper.getTBsChargingRulesBySchemeId(chargingSchemeId);
//		for(TBsChargingRules tRule:ruleList){
//			List<TBsRuleBuildingRelation>  relaList = this.tBsRuleBuildingRelationMapper.getRelationByRuleIdAndType(tRule.getId(), "qi");
//			if(CollectionUtils.isNotEmpty(relaList)){
//				List<Map<String,Object>> listOne= convertRelation(relaList, tRule);
//				if(!objList.containsAll(listOne)){
//					objList.addAll(listOne);
//				}
//			}
//			List<TBsRuleBuildingRelation>  relaListQu = this.tBsRuleBuildingRelationMapper.getRelationByRuleIdAndType(tRule.getId(), "qu");
//			if(CollectionUtils.isNotEmpty(relaList)){
//				List<Map<String,Object>> listTwo= convertRelation(relaListQu, tRule);
//				if(!objList.containsAll(listTwo)){
//					objList.addAll(listTwo);
//				}
//			}
//			List<TBsRuleBuildingRelation>  relaListDongZuo = this.tBsRuleBuildingRelationMapper.getRelationByRuleIdAndType(tRule.getId(), "dongzuo");
//			if(CollectionUtils.isNotEmpty(relaList)){
//				List<Map<String,Object>> listThree= convertRelation(relaListDongZuo, tRule);
//				if(!objList.containsAll(listThree)){
//					objList.addAll(listThree);
//				}
//			}
//			List<TBsRuleBuildingRelation>  relaListDanYuanRuKou = this.tBsRuleBuildingRelationMapper.getRelationByRuleIdAndType(tRule.getId(), "danyuanrukou");
//			if(CollectionUtils.isNotEmpty(relaList)){
//				List<Map<String,Object>> listFoure= convertRelation(relaListDanYuanRuKou, tRule);
//				if(!objList.containsAll(listFoure)){
//					objList.addAll(listFoure);
//				}
//			}
//			List<TBsRuleBuildingRelation>  relaListCeng = this.tBsRuleBuildingRelationMapper.getRelationByRuleIdAndType(tRule.getId(), "ceng");
//			if(CollectionUtils.isNotEmpty(relaList)){
//				List<Map<String,Object>> listFive= convertRelation(relaListCeng, tRule);
//				if(!objList.containsAll(listFive)){
//					objList.addAll(listFive);
//				}
//			}
//		}
//		return objList;
//	}


//	private List<Map<String,Object>> convertRelation(List<TBsRuleBuildingRelation> relaList,TBsChargingRules tRule){
//		for(TBsRuleBuildingRelation tbRelation:relaList){
//			String buildingCode= tbRelation.getRelationBuildingCode();
//			List<TBsRuleBuildingRelation> relaListOne = this.tBsRuleBuildingRelationMapper.getRuleIdAndPid(tRule.getId(), buildingCode);
//			//查建筑里
//			List<TcBuilding> tcBuildList= this.tcBuildingMapper.findBuildingByPid(buildingCode);
//			if(relaListOne.size() != tcBuildList.size()){
//				String pId = tbRelation.getRelationBuildingPid();
//				String ruleId=tbRelation.getChargingRuleId();
//				if(StringUtils.isNotBlank(pId) && StringUtils.isNotBlank(ruleId)){
//					List<Map<String,Object>> listObj = recursionFindFather(pId,ruleId); //递归一直往上找父节点
//					if(!objList.containsAll(listObj)){
//						objList.addAll(listObj);
//					}
//				}
//				Map<String,Object> map = convertObject(tbRelation);
//				if(!objList.contains(map)){
//					objList.add(map);
//				}
//			}
//		}
//
//		return objList;
//	}

//	//递归找父节点
//	private List<Map<String,Object>> recursionFindFather(String buildCode,String ruleId){
//		TBsRuleBuildingRelation tbs = this.tBsRuleBuildingRelationMapper.getRelationByBuilCodeAndRuleId(buildCode,ruleId);
//		if(CommonUtils.isNotEmpty(tbs)){
//			Map<String,Object> map = convertObject(tbs);
//			if(!objList.contains(map)){
//				objList.add(map);
//			}
//			String pId = tbs.getRelationBuildingPid();
//			String rulId = tbs.getChargingRuleId();
//			if(StringUtils.isNotBlank(pId) && StringUtils.isNotBlank(rulId)){
//				recursionFindFather(pId,rulId);
//			}
//
//		}
//		return objList;
//	}

	private Map<String,Object> convertObject(TBsRuleBuildingRelation tbRelation){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", tbRelation.getId());
		map.put("buildingCode", tbRelation.getRelationBuildingCode());
		map.put("buildingFullName", tbRelation.getRelatioBuildingFullName());
		map.put("buildingName", tbRelation.getRelationBuildingName());
		map.put("buildingType", tbRelation.getRelationBuildingType());
		map.put("pid", tbRelation.getRelationBuildingPid());
		return map;
	}

	private String getLogStr(String error){
		return String.format("当前时间 : %s , 异常  -> %s" ,CommonUtils.getDateStr(),error);
	}

	/**
	 * 批量删除
	 */
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings({ "rawtypes" })
	@Override
	public BaseDto batchRuleByIds(WyBusinessContext ctx,
			TBsChargingRules tBsChargingRules) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			if(CommonUtils.isEmpty(tBsChargingRules)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("传入的参数为空!");
			}else{
				List<String> ids = tBsChargingRules.getIds();
				if(CollectionUtils.isEmpty(ids)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("传入的要删除的规则id为空!");
				}else{
					//删除.先删除关联建筑，在删除费用项，最后删除规则
					//删除关联建筑
					this.tBsRuleBuildingRelationMapper.delByRuleId(ids);
					//删除相关费用项
					this.tBsChargeTypeMapper.delByRuleId(ids);
					//删除规则
					this.tBsChargingRulesMapper.delByRuleId(ids);

					msgMap.setFlag(MessageMap.INFOR_SUCCESS);
				}
			}

			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			logger.info(getLogStr(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}

	@Override
	public BaseDto getFeeRuleById(WyBusinessContext ctx, String ruleId) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			TBsChargingRules tBsChargingRules = tBsChargingRulesMapper.getChargeRuleById(ruleId);
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			baseDto.setObj(tBsChargingRules);
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			logger.info(getLogStr(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
		 
	}

	
}
