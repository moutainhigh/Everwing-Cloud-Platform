package com.everwing.coreservice.wy.core.service.impl.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargeType;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingRules;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsRuleBuildingRelation;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsWaterSchemeService;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillHistoryMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillTotalMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargeTypeMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargingRulesMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargingSchemeMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsRuleBuildingRelationMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("tBsWaterSchemeServiceImpl")
public class TBsWaterSchemeServiceImpl implements TBsWaterSchemeService {

	
	@Autowired
	private TBsChargingSchemeMapper TBsWaterSchemeMapper;
	@Autowired
	private TBsChargingRulesMapper tBsChargingRulesMapper;
	@Autowired
	private TBsChargeTypeMapper tBsChargeTypeMapper;
	@Autowired
	private TBsRuleBuildingRelationMapper tBsRuleBuildingRelationMapper;
	@Autowired
	private TcMeterDataMapper tcMeterDataMapper;
	@Autowired
	private TBsChargeBillTotalMapper tBsChargeBillTotalMapper;
	@Autowired
	private TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;
	
	
	private static Logger logger=Logger.getLogger(TBsWaterSchemeServiceImpl.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto selectWaterSchemeInfo(String companyId,TBsChargingScheme scheme) {
		BaseDto returnDto = new BaseDto();
		List<TBsChargingScheme> schemes = this.TBsWaterSchemeMapper.selectSchemeInfo(scheme);
		returnDto.setLstDto(schemes);
		return returnDto; 
	}

	/**
	 * 根据项目id，对项目进行手动点击计费
	 */
	@Transactional(rollbackFor=Exception.class)
	@Override
	public MessageMap manualChargingForWater(String companyId, String projectId,String userId) {
		logger.info("==============开始项目："+projectId+"水费的计费操作============");
		// TODO Auto-generated method stub
		/*     水费：
		 *  						根据建筑  查询水费的   计费规则（这里不同的房屋会有不同的计费规则）
		 *  						根据计费规则  查询 每个计费规则的    计费类型（具体收费项）
		 *  						根据此建筑的收费项     计算所有收费项的收费  ，然后汇总   
		 *  						根据建筑 (查询水表信息)  --用水量  * price 
		 *  						。。其他收费项
		 *  						违约金*/
		TBsChargingScheme tbsChargin=new TBsChargingScheme();
		tbsChargin.setProjectId(projectId);
		tbsChargin.setIsEffective(0);
		tbsChargin.setIsUsed(0);
		tbsChargin.setChargingType(3);
		//1.查询此项目当前正在使用的计费方案
		TBsChargingScheme tbsCharingScheme= TBsWaterSchemeMapper.selectSchemeInfo(tbsChargin).get(0);
		if(CommonUtils.isEmpty(tbsCharingScheme)){
			return new MessageMap(MessageMap.INFOR_ERROR,"此项目当前无可用计费方案，无法计费");
		}
		//手动计费值计费方案为手动计费的   0：自动   1： 手动
		if( 0 == tbsCharingScheme.getChargingType() ){
			return new MessageMap(MessageMap.INFOR_ERROR,"此方案为自动计费，无法手动计费");
		}
		
		
		/**计费可分为四个部分   1.当月水费应交     2.以往欠费    3.欠费产生的违约金   4.当月分摊（如果有）  **/
		//查询此计费方案下的收费类型
		TBsChargingRules tbsChargingRule=new TBsChargingRules();
		tbsChargingRule.setChargingSchemeId(tbsCharingScheme.getId());
		List<TBsChargingRules> rules=tBsChargingRulesMapper.listPageRulesBySchemeId(tbsChargingRule);
		if(CommonUtils.isEmpty(rules)){
			return new MessageMap(MessageMap.INFOR_ERROR,"此项目当前计费方案无内容，无法计费");
		}
		
		//查询本次计费的  每期计费总计表是否存在对应的数据
		TBsChargeBillTotal tBsChargeBillTotal= tBsChargeBillTotalMapper.selectChargeBillByType(projectId,"3");
		boolean isFirst= CommonUtils.isEmpty(tBsChargeBillTotal) ? true : false;//标记是否为第一次
		//校验此方案的计费日期是否符合当前时间
		Calendar calendar = Calendar.getInstance();
		//第一次计费，比较计费日和当前日期
		if( isFirst && (tbsCharingScheme.getChargeData() > calendar.get(Calendar.DATE)) ){
			return new MessageMap(MessageMap.INFOR_ERROR,"还未到计费日，无法进行手动计费操作");
		}
		
		if( !isFirst ){
			//比较当前日期和（上次计费日期+频率）
			Date now=new Date();
			Date fuDate=CommonUtils.addMonth(tBsChargeBillTotal.getBillingTime(), tbsCharingScheme.getFrequency());
			if(fuDate.getTime()>now.getTime()){
				return new MessageMap(MessageMap.INFOR_ERROR,"还未到计费日，无法进行手动计费操作");
			}
		}
		
		TBsChargeBillTotal tBsChargeBillTotalLast=new TBsChargeBillTotal();
		MessageMap msg= insertBillTotalInfo(tBsChargeBillTotal,tBsChargeBillTotalLast,projectId,userId,tbsCharingScheme.getFrequency());
		if(CommonUtils.isNotEmpty(msg)){
			return msg;
		}
		
		//水费计算
		chargingProcessing(rules,tBsChargeBillTotal,tBsChargeBillTotalLast,isFirst);
		
		logger.info("==============项目："+projectId+"水费的计费操作结束============");
		return null;
	}

	
	/**
	 * @describe （如果没有--第一次计费需要写入两条总计数据（本次和下次）   如果有回写本次，添加下次）
	 * @param tBsChargeBillTotal 需要插入的本次总计pojo
	 * @param projectId 项目id
	 * @param userId 操作人id
	 * @param frequency 此方案的执行频率
	 * @return MessageMap 如果为null继续后面的代码，如果is not null 返回
	 * @author QHC
	 */
	@Transactional(rollbackFor=Exception.class)
	public MessageMap insertBillTotalInfo(TBsChargeBillTotal tBsChargeBillTotal,TBsChargeBillTotal tBsChargeBillTotalLast, String projectId,String userId,int frequency){
		if(CommonUtils.isEmpty(tBsChargeBillTotal)){
			//如果还没有，创建水表的总计表信息(当月的)
			tBsChargeBillTotal.setBillingTime(new Date());
			tBsChargeBillTotal.setId(CommonUtils.getUUID());
			int num=insertChargeBillTotal(tBsChargeBillTotal,projectId,userId);
			if(num <=0){
				logger.info("==============新增计费总计费表数据失败============");
				return new MessageMap(MessageMap.INFOR_ERROR,"创建总计表信息失败，无法计费");
			}
		}
		
		//插入下月的
		tBsChargeBillTotalLast.setBillingTime(CommonUtils.addMonth(new Date(),frequency));
		tBsChargeBillTotalLast.setId(CommonUtils.getUUID());
		
		int numLast=insertChargeBillTotal(tBsChargeBillTotalLast,projectId,userId);
		if(numLast <=0){
			logger.info("==============新增下月计费总计费表数据失败============");
			return new MessageMap(MessageMap.INFOR_ERROR,"创建总计表信息失败，无法计费");
		}
		return null;
	}
	
	
	/**
	 * 插入一条  每期总计费表数据
	 */
	@Transactional(rollbackFor=Exception.class)
	public int insertChargeBillTotal(TBsChargeBillTotal tBsChargeBillTotal,String projectId,String userId){
		TBsChargeBillTotal chargeBill=new TBsChargeBillTotal();
		chargeBill.setType(3);
		chargeBill.setChargingType(1);
		chargeBill.setIsRebilling(1);
		chargeBill.setAuditStatus(0);//新建待审核
		chargeBill.setCreateTime(new Date());
		tBsChargeBillTotal.setProjectId(projectId);
		tBsChargeBillTotal.setCreateId(userId);
		
		return tBsChargeBillTotalMapper.insertChargeBillTotal(chargeBill);
	}
	
	
	/**
	 * @describe 根据计费规则进行计费处理
	 */
	@Transactional(rollbackFor=Exception.class)
	public MessageMap chargingProcessing(List<TBsChargingRules> rules,TBsChargeBillTotal entity,TBsChargeBillTotal entityLast,
			boolean isFirst){
		int chargeNum=0;
		StringBuilder erroInfo=new StringBuilder();
		//按照收费规则进行计费处理（单个物业只能对应一个水费收费规则）
		for (TBsChargingRules tBsChargingRules : rules) {
			//单个计费规则下有多个收费类型
			List<TBsChargeType> tBsChargeTypes=tBsChargeTypeMapper.selectChargeType(tBsChargingRules.getId());
			if(CommonUtils.isEmpty(tBsChargeTypes)){
				logger.warn("======="+tBsChargingRules.getRuleName()+"  下未找到可用的收费类型======");
				continue;
			}
			//单个计费规则下有对应的房屋（单个物业需要交  多个收费类型的费用）
			List<TBsRuleBuildingRelation> buildings=tBsRuleBuildingRelationMapper.selectAssetsByRuleId(tBsChargingRules.getId());
			if(CommonUtils.isEmpty(buildings)){
				logger.warn("======="+tBsChargingRules.getRuleName()+"  下未找到关联建筑信息======");
				continue;
			}
			
			//本月总水费金额
			Double chargeForWater=0.0;
			//存在收费类型 && 有关联资产信息
			for (TBsRuleBuildingRelation tBsRuleBuildingRelation : buildings) {
				//查询此建筑的水表信息,抄表信息
				TcMeterData meterData = tcMeterDataMapper.getMeterDataForCharge(tBsRuleBuildingRelation.getRelationBuildingCode(),"0");
			 	if(CommonUtils.isEmpty(meterData)){
			 		logger.warn("=======水表"+meterData.getMeterCode()+"  未找到抄表数据，无法计费======");
			 		continue;
			 	}
			 	//查询到抄表数据
			 	for (TBsChargeType chargeType : tBsChargeTypes) {
					//TODO 每个类型都要收费,根据计算公式算出单项费用
			 		
//			 		chargeForWater+=;
				}
			 
			 	//插入账单计费元数据（回写本月，插入下月）
			 	MessageMap msg= insertBillHistory(entity,entityLast,tBsRuleBuildingRelation,chargeForWater,isFirst);
			 	if(CommonUtils.isNotEmpty(msg)){
			 		erroInfo.append(tBsRuleBuildingRelation.getRelationBuildingCode()+",");
			 		continue;
			 	}
			 	chargeNum++;//成功计费一条
			}
		}
		return new MessageMap(MessageMap.INFOR_SUCCESS,"成功计费"+chargeNum+"条，未计费成功建筑信息如下："+erroInfo);
	}
	
	
	
	
	/**
	 * @describe 插入一条 账单计费元数据表 数据信息
	 * 				如果是第一次需要插入两条数据，一条上次，一条本次（下次计费为上月）数据
	 * 				如果不是第一次  回写上月，新增下月
	 * @param history 上次水费账单计费元数据信息
	 * @param entity 上次水费总计信息
	 * @param entityLast 本次水费总计 信息
	 * @param buildingCode 关联建筑code
	 * @param chargeForWater 上月水费
	 * @return 
	 * @author QHC
	 */
	@Transactional(rollbackFor=Exception.class)
	public MessageMap insertBillHistory(TBsChargeBillTotal entity,TBsChargeBillTotal entityLast,
					TBsRuleBuildingRelation tBsRuleBuildingRelation,Double chargeForWater,boolean isFirst){
		
		TBsChargeBillHistory tBsChargeBillHistory=new TBsChargeBillHistory();
		TBsChargeBillHistory history=new TBsChargeBillHistory();
		int num=0;
		if( isFirst ){
			//第一次，不存在上期数据信息，直接插入
			tBsChargeBillHistory=getNewTBsChargeBillHistory(entity,tBsRuleBuildingRelation);
			tBsChargeBillHistory.setCurrentFee(chargeForWater);
	 		num=tBsChargeBillHistoryMapper.insertBillHistory(tBsChargeBillHistory);
	 		
	 		if(num == 1){
	 			//产生下月的   这里统计的账单数据只有本期水费费用，其他准确数据需要在  分摊，违约金，都计费成功后的扣减处理
	 			tBsChargeBillHistory.setLastBillId(tBsChargeBillHistory.getId());
	 			tBsChargeBillHistory.setId(CommonUtils.getUUID());
	 			tBsChargeBillHistory.setChargeTotalId(entityLast.getId());
	 			num=tBsChargeBillHistoryMapper.insertBillHistory(tBsChargeBillHistory);
	 		}
		}else{
			//将本期产生的金额回写到对应  元数据
			tBsChargeBillHistory.setBuildingCode(tBsRuleBuildingRelation.getRelationBuildingCode());
			tBsChargeBillHistory.setChargeTotalId(entity.getId());
			history=tBsChargeBillHistoryMapper.selectChargeHistoryOne(tBsChargeBillHistory);
			history.setCurrentFee(chargeForWater);//本期产生费用
			history.setModifyId(entity.getId());
			history.setModifyTime(new Date());
			num=tBsChargeBillHistoryMapper.updateBillHistory(history);
			
			if(num == 1 ){
				TBsChargeBillHistory historyLast=getNewTBsChargeBillHistory(entity,tBsRuleBuildingRelation);
				historyLast.setChargeTotalId(entityLast.getId());
				historyLast.setLastBillId(history.getId());
				historyLast.setLastBillFee(history.getCurrentBillFee());//上期账单金额
				historyLast.setId(CommonUtils.getUUID());
	 			num=tBsChargeBillHistoryMapper.insertBillHistory(tBsChargeBillHistory);
			}
		}
		
		if(num != 1){
			logger.info("==============房屋编号"+tBsRuleBuildingRelation.getRelationBuildingCode()+"插入账单元数据失败===============");
			return new MessageMap(MessageMap.INFOR_ERROR,tBsRuleBuildingRelation.getRelationBuildingCode()+"插入账单元数据失败");
		}
		return null;
		
	}
	
	
	/**
	 * @describe 公用字段设置---用于新增一个  TBsChargeBillHistory,特殊字段单独维护
	 * @param entityTotal 总计费信息
	 * @param tBsRuleBuildingRelation 关联对象
	 * @return
	 */
	public TBsChargeBillHistory getNewTBsChargeBillHistory(TBsChargeBillTotal entityTotal,TBsRuleBuildingRelation tBsRuleBuildingRelation){
		
		TBsChargeBillHistory entity=new TBsChargeBillHistory();
		entity=new TBsChargeBillHistory();
		entity.setChargeTotalId(entityTotal.getId());
		entity.setProjectId(entityTotal.getProjectId());
		entity.setBuildingCode(tBsRuleBuildingRelation.getRelationBuildingCode());
		entity.setFullName(tBsRuleBuildingRelation.getRelationBuildingName());
		entity.setCreateId(entityTotal.getCreateId());
		entity.setId(CommonUtils.getUUID());
		entity.setCreateTime(new Date());
		entity.setLastBillFee(0.0);
		entity.setLastPayed(0.0);
		entity.setIsUsed(0);
		entity.setCurrentFee(0.0);
		return entity;
	}

	
}
