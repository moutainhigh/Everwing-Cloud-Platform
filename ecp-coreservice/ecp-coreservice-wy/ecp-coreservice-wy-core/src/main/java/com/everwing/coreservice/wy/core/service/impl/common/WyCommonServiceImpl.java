package com.everwing.coreservice.wy.core.service.impl.common;/**
 * Created by wust on 2017/10/11.
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.common.select.asset.AssetSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.customer.CustomerSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.vehicle.VehicleSelectList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.common.select.asset.AssetSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.asset.AssetSelectSearch;
import com.everwing.coreservice.common.wy.entity.common.select.customer.CustomerSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.customer.CustomerSelectSearch;
import com.everwing.coreservice.common.wy.entity.common.select.vehicle.VehicleSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.vehicle.VehicleSelectSearch;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNew;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import com.everwing.coreservice.common.wy.entity.order.TcOrderExample;
import com.everwing.coreservice.common.wy.entity.other.AgentCodeSearch;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerSearch;
import com.everwing.coreservice.common.wy.service.common.WyCommonService;
import com.everwing.coreservice.wy.dao.mapper.CommonMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.tbs.assetsaccount.TBsAssetAccountMapper;
import com.everwing.coreservice.wy.dao.mapper.cust.person.PersonCustNewMapper;
import com.everwing.coreservice.wy.dao.mapper.order.TcOrderMapper;
import com.everwing.coreservice.wy.dao.mapper.personbuilding.PersonBuildingNewMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Function:系统公共服务类
 * Reason:
 * Date:2017/10/11
 * @author wusongti@lii.com.cn
 */
@Service("wyCommonServiceImpl")
public class WyCommonServiceImpl implements WyCommonService {
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private TcBuildingMapper tcBuildingMapper;
    @Autowired
    private PersonBuildingNewMapper personBuildingNewMapper;
    @Autowired
    private PersonCustNewMapper personCustNewMapper;
    @Autowired
    private TBsAssetAccountMapper tBsAssetAccountMapper;
    @Autowired
    private TcOrderMapper tcOrderMapper;
    
    @Override
    public BaseDto custAndBuildingInfo(String companyId, String custId, String custType,String buildingCode) {
    	BaseDto baseDto = new BaseDto<>();
    	List<PersonCustNew> personCustList = new ArrayList<PersonCustNew>();
		List<EnterpriseCustNew> entCustList = new ArrayList<EnterpriseCustNew>();
		List<TcBuilding> buildingList = new ArrayList<TcBuilding>();
		BigDecimal totalDeposit=new BigDecimal(0);
		if (StringUtils.isNotBlank(custId)) {// 根据custId查出客户和名下所有资产
			if("person".equals(custType)){
				personCustList.add(commonMapper.getPersonCustById(custId));
			}else if("enterprise".equals(custType)){
				entCustList.add(commonMapper.getEntCustById(custId));
			}else{
				return baseDto;
			}
			buildingList = personBuildingNewMapper.listBudildingByCustId(custId);
			// 计算总押金
		    totalDeposit= commonMapper.sumDepositByCust(personCustList, entCustList);
		} else if (StringUtils.isNotBlank(buildingCode)) {
			List<String> jsonStrList=commonMapper.selectJsonListByBuildingCode(buildingCode);
			// 计算总押金
			if(jsonStrList!=null&&jsonStrList.size()>0){
				for(String jsonStr:jsonStrList){
					JSONObject jsonObject=JSON.parseObject(jsonStr);
					Object fields=jsonObject.get("t_fields");
					if(fields!=null){
						totalDeposit= totalDeposit.add(new BigDecimal(fields.toString()));
					}
				}
			}
		}
		
		// 计算总欠费
		Double totalBalances = (buildingList== null || buildingList.size()==0)?0:tBsAssetAccountMapper.queryTotalBalances(buildingList);
		// 统计工单总数、处理中数量、已完成数量、未处理数量
		ArrayList<String> custIdList = new ArrayList<String>();
		ArrayList custList = new ArrayList();
		for(PersonCustNew p:personCustList){
			custIdList.add(p.getCustId());
			custList.add(p);
		}
		for(EnterpriseCustNew e:entCustList){
			custIdList.add(e.getEnterpriseId());
			custList.add(e);
		}
		
		long orderSum=0;
		long processingSum=0;
		long finishedSum=0;
		if (entCustList.size() + personCustList.size() > 0) {// 客户判断
			TcOrderExample totalExample = new TcOrderExample();
			totalExample.createCriteria().andCustIdIn(custIdList);
			orderSum = tcOrderMapper.countByExample(totalExample);
			
			TcOrderExample processingExample = new TcOrderExample();
			processingExample.createCriteria().andCustIdIn(custIdList).andStatusEqualTo(1);
			processingSum = tcOrderMapper.countByExample(processingExample);
			
			TcOrderExample finishedExample = new TcOrderExample();
			finishedExample.createCriteria().andCustIdIn(custIdList).andStatusEqualTo(2);
			finishedSum = tcOrderMapper.countByExample(finishedExample);
		}
		
		
    	
		//返回结果
    	HashMap<String, Object> resultMap = new HashMap<String,Object>();
    	resultMap.put("totalBalances",totalBalances);
    	resultMap.put("totalDeposit", totalDeposit);
    	resultMap.put("orderSum", orderSum);
    	resultMap.put("processingSum", processingSum);
    	resultMap.put("finishedSum", finishedSum);
    	resultMap.put("buildingList",buildingList );
    	resultMap.put("custList", custList);
    	baseDto.setObj(resultMap);
    	return baseDto ;
    }

	@Override
	public BaseDto queryArrearageByBuildingCode(String companyId,String[] buildingCodes) {
    	BaseDto baseDto=new BaseDto();
    	List<TcBuilding> buildingList=new ArrayList<>(buildingCodes.length);
    	for(int i=0;i<buildingCodes.length;i++){
			TcBuilding tcBuilding=new TcBuilding();
			tcBuilding.setBuildingCode(buildingCodes[i]);
			buildingList.add(tcBuilding);
		}
		List<Map<String,Object>> balanceMaps=tBsAssetAccountMapper.queryBalances(buildingList);
    	baseDto.setObj(balanceMaps);
		return baseDto;
	}

	@Override
    public BaseDto sumDepositByCust(String companyId, List<PersonCustNew> personCustNewList, List<EnterpriseCustNew> enterpriseCustNewList) {
    	BaseDto baseDto = new BaseDto<>();
    	baseDto.setObj(commonMapper.sumDepositByCust(personCustNewList, enterpriseCustNewList));
    	return baseDto ;
    }
    
    @Override
    public BaseDto listPageAgentCode(String companyId, AgentCodeSearch agentCodeSearch) {
    	BaseDto baseDto = new BaseDto<>();
    	baseDto.setLstDto(commonMapper.listPageAgentCode(agentCodeSearch));
    	baseDto.setPage(agentCodeSearch.getPage());
    	return baseDto ;
    }
    
    @Override
    public BaseDto listPageCC(String companyId, AgentCodeSearch agentCodeSearch) {
    	BaseDto baseDto = new BaseDto<>();
    	baseDto.setLstDto(commonMapper.listPageCC(agentCodeSearch));
    	baseDto.setPage(agentCodeSearch.getPage());
    	return baseDto ;
    }

    @Override
    public BaseDto listPageCustomer(String companyId, CustomerSearch customerSearch) {
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(commonMapper.listPageCustomer(customerSearch));
        baseDto.setPage(customerSearch.getPage());
        return baseDto ;
    }

    @Override
    public BaseDto listPageBuilding(WyBusinessContext ctx, TcBuildingSearch condition) {
        List<TcBuildingList> list = commonMapper.listPageBuilding(condition);

        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(condition.getPage());
        return baseDto ;
    }


	@Override
	public List<CustomerSelectList> findCustomerSelect(WyBusinessContext ctx, CustomerSelectSearch customerSelectSearch) {
		return commonMapper.findCustomerSelect(customerSelectSearch);
	}

	@Override
	public List<AssetSelectList> findAssetSelect(WyBusinessContext ctx, AssetSelectSearch assetSelectSearch) {
		return commonMapper.findAssetSelect(assetSelectSearch);
	}

	@Override
	public List<VehicleSelectList> findVehicleSelect(WyBusinessContext ctx, VehicleSelectSearch vehicleSelectSearch) {
		return commonMapper.findVehicleSelect(vehicleSelectSearch);
	}
}
