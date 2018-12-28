package com.everwing.coreservice.common.wy.utils;

import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.configuration.rebilling.TBsRebillingInfo;
import com.everwing.coreservice.common.wy.entity.configuration.support.BillingSupEntity;
import org.springframework.amqp.core.AmqpTemplate;

import java.util.ArrayList;
import java.util.List;

public class BillingUtils {

	@SuppressWarnings("unchecked")
	public static void sendInsertList(List<?> insertList, String routeKey, String companyId, AmqpTemplate amqpTemplate){
		if(insertList.isEmpty())
			return;
		BillingSupEntity se = new BillingSupEntity();
		String clazzName = insertList.get(0).getClass().getName();
		if(CommonUtils.isEquals(clazzName, TBsChargeBillHistory.class.getName())){
			se.setInsertList((ArrayList<TBsChargeBillHistory>) insertList);
		}
		else if(CommonUtils.isEquals(clazzName, TBsAssetAccount.class.getName())){
			se.setInsertAccountList((ArrayList<TBsAssetAccount>) insertList);
		}
		else if(CommonUtils.isEquals(clazzName, TBsAssetAccountStream.class.getName())){
			se.setInsertStreamList((ArrayList<TBsAssetAccountStream>) insertList);
		}
		else if(CommonUtils.isEquals(clazzName, TBsOwedHistory.class.getName())){
			se.setInsertOhList((ArrayList<TBsOwedHistory>) insertList);
		}
		else if(CommonUtils.isEquals(clazzName, TBsRebillingInfo.class.getName())){
			se.setInsertInfoList((ArrayList<TBsRebillingInfo>)insertList);
		}
		
		MqEntity entity = new MqEntity(null, se);
		entity.setCompanyId(companyId);
		amqpTemplate.convertAndSend(routeKey, entity);
		insertList.clear();
	}
	
	@SuppressWarnings("unchecked")
	public static void sendUpdateList(List<?> updateList, String routeKey, String companyId, AmqpTemplate amqpTemplate){
		BillingSupEntity se = new BillingSupEntity();
		String clazzName = updateList.get(0).getClass().getName();
		if(CommonUtils.isEquals(clazzName, TBsChargeBillHistory.class.getName())){
			se.setUpdateList((ArrayList<TBsChargeBillHistory>) updateList);
		}
		else if(CommonUtils.isEquals(clazzName, TBsAssetAccount.class.getName())){
			se.setUpdateAccountList((ArrayList<TBsAssetAccount>) updateList);
		}
		else if(CommonUtils.isEquals(clazzName, TBsAssetAccountStream.class.getName())){
			se.setUpdateStreamList((ArrayList<TBsAssetAccountStream>) updateList);
		}
		else if(CommonUtils.isEquals(clazzName, TBsOwedHistory.class.getName())){
			se.setUpdateOhList((ArrayList<TBsOwedHistory>) updateList);
		}
		MqEntity entity = new MqEntity(null, se);
		entity.setCompanyId(companyId);
		amqpTemplate.convertAndSend(routeKey, entity);
		updateList.clear();
	}
}
