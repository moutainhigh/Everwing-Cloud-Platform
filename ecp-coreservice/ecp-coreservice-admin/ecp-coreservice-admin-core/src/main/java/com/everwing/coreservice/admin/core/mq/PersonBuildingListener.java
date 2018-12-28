package com.everwing.coreservice.admin.core.mq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.admin.entity.cust.personbuilding.PersonBuilding;
import com.everwing.coreservice.common.constant.MqConstants;
import com.everwing.coreservice.common.platform.entity.extra.AccountAndHouseSipData;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.SipUtils;
import com.everwing.coreservice.common.wy.common.enums.RabbitMQEnum;
import com.everwing.coreservice.platform.dao.mapper.cust.personbuilding.PersonBuildingExtraMapper;
import com.everwing.coreservice.platform.dao.mapper.extra.AccountExtraMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("personBuildingListener")
public class PersonBuildingListener implements MessageListener{
	
	private static final Logger LOG = LoggerFactory.getLogger(PersonBuildingListener.class);

	@Autowired
	private PersonBuildingExtraMapper personBuildingMapper;
	
	@Autowired
	private AccountExtraMapper accountExtraMapper;

	@Autowired
	private SipUtils sipUtils;

	@Override
	public void onMessage(Message message) {
		try {
			JSONObject jsonObject = (JSONObject) JSONObject.parse(new String(message.getBody(), "UTF-8"));

			if (null == jsonObject) {
				LOG.info("消息队列: 传入个人客户数据为空: {}", jsonObject);
				return;
			}


			String opr = jsonObject.get(RabbitMQEnum.opt.name()).toString();
			JSONObject dataJsonObject = jsonObject.getJSONObject(RabbitMQEnum.data.name());



		if(RabbitMQEnum.insert.name().equals(opr)){
			List<PersonBuilding> personBuildings=new ArrayList<>(1);
			PersonBuilding personBuilding=JSONObject.toJavaObject(dataJsonObject, PersonBuilding.class);
			personBuildings.add(personBuilding);
			this.personBuildingMapper.add(personBuilding);
			userBuildingAdd(personBuildings);
		}else if(RabbitMQEnum.del.name().equals(opr)){
			List<PersonBuilding> personBuildings=new ArrayList<>(1);
			PersonBuilding personBuilding=JSONObject.toJavaObject(dataJsonObject, PersonBuilding.class);
			personBuildings.add(personBuilding);
			this.personBuildingMapper.del(personBuilding);
			userBuildingDel(personBuildings);
		}
		
			LOG.info(CommonUtils.log("消息队列: 操作客户资产数据时出现完成 , 数据: " + jsonObject ));
		} catch (Exception e) {
			LOG.error(CommonUtils.log("消息队列: 操作客户资产数据时出现异常 , 数据: "  +  " . 异常信息: " + e.getMessage() ));
		}
	}


	private void userBuildingAdd(List<PersonBuilding> addList){
		List<String> custIdList=new ArrayList<>(addList.size());
		List<String> buildingIdList=new ArrayList<>(addList.size());
		for(PersonBuilding personBuilding:addList){
			String custId=personBuilding.getCustId();
			String buildingId=personBuilding.getBuildingId();
			custIdList.add(custId);
			buildingIdList.add(buildingId);
		}
		List<Map<String,String>> datas=accountExtraMapper.selectAccountCodesAndCodesByCustIdsAndBIds(custIdList,buildingIdList);
		if (datas!=null){
			List<AccountAndHouseSipData> aahList=new ArrayList<>(datas.size());
			List<Map<String,String>> sysValue=new ArrayList<>(datas.size());
			for(int i=0;i<datas.size();i++){
				Map<String,String> data=datas.get(i);
				AccountAndHouseSipData accountAndHouseSipData=new AccountAndHouseSipData(data.get("accountCode"),data.get("buildingCode"));
				aahList.add(accountAndHouseSipData);
				Map<String,String> paramMap=new HashMap<>(3);
				paramMap.put("mobile",data.get("mobile"));
				paramMap.put("accountCode",data.get("accountCode"));
				paramMap.put("buildingCode",data.get("buildingCode"));
				sysValue.add(paramMap);
			}
			accountExtraMapper.batchInsertAccountHouse(sysValue);
			sipUtils.bind(aahList);
			LOG.info("用户房间关系绑定成功!");
		}else {
			LOG.error("用户房屋绑定关系不匹配！");
		}
	}
	/**
	 * 删除账号房间绑定关系
	 * @param delList 需要解绑的数据
	 */
	private void userBuildingDel(List<PersonBuilding> delList){
		List<String> custIdList=new ArrayList<>(delList.size());
		List<String> buildingIdList=new ArrayList<>(delList.size());
		for(PersonBuilding personBuilding:delList){
			String custId=personBuilding.getCustId();
			String buildingId=personBuilding.getBuildingId();
			custIdList.add(custId);
			buildingIdList.add(buildingId);
		}
		List<Map<String,String>> datas=accountExtraMapper.selectAccountCodesAndCodesByCustIdsAndBIds(custIdList,buildingIdList);
		if(datas!=null){
			List<AccountAndHouseSipData> aahList=new ArrayList<>(datas.size());
			List<Map<String,String>> value=new ArrayList<>(datas.size());
			for (Map<String, String> dataMap : datas) {
				Map<String, String> valueMap = new HashMap<>(2);
				AccountAndHouseSipData accountAndHouseSipData = new AccountAndHouseSipData(dataMap.get("accountCode"), dataMap.get("buildingCode"));
				aahList.add(accountAndHouseSipData);
				valueMap.put("username", dataMap.get("buildingCode"));
				valueMap.put("bindusername", dataMap.get("accountCode"));
				value.add(valueMap);
			}
			accountExtraMapper.delUserHouseByCode(value);
			sipUtils.unbind(aahList);
			LOG.info("用户房间关系解除绑定成功!");
		}else {
			LOG.error("用户房屋绑定关系不匹配！");
		}
	}
}
