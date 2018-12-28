package com.everwing.coreservice.common.wy.service.common;/**
 * Created by wust on 2017/10/11.
 */

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
import com.everwing.coreservice.common.wy.entity.other.AgentCodeSearch;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/10/11
 * @author wusongti@lii.com.cn
 */
public interface WyCommonService {
    BaseDto listPageCustomer(String companyId, CustomerSearch customerSearch);

    BaseDto listPageBuilding(WyBusinessContext ctx, TcBuildingSearch condition);

	BaseDto listPageCC(String companyId, AgentCodeSearch agentCodeSearch);

	BaseDto listPageAgentCode(String companyId, AgentCodeSearch agentCodeSearch);

	BaseDto sumDepositByCust(String companyId, List<PersonCustNew> personCustNewList,
			List<EnterpriseCustNew> enterpriseCustNewList);


	BaseDto custAndBuildingInfo(String companyId, String custId, String custType,
			String buildingCode);


	/**
	 * 公共组件：客户下拉选择框
	 * @param customerSelectSearch
	 * @param ctx
	 * @return
	 */
	List<CustomerSelectList> findCustomerSelect(WyBusinessContext ctx, CustomerSelectSearch customerSelectSearch);

	/**
	 * 公共组件：资产下拉选择框
	 * @param assetSelectSearch
	 * @param ctx
	 * @return
	 */
	List<AssetSelectList> findAssetSelect(WyBusinessContext ctx, AssetSelectSearch assetSelectSearch);

	/**
	 * 公共组件：车辆下拉选择框
	 * @param vehicleSelectSearch
	 * @param ctx
	 * @return
	 * @throws DataAccessException
	 */
	List<VehicleSelectList> findVehicleSelect(WyBusinessContext ctx, VehicleSelectSearch vehicleSelectSearch);

	/**
	 * 根据buildingCode 查询欠费信息
	 * @param companyId 公司id
	 * @param buildingCodes 房屋编码
	 * @return common result
	 */
	BaseDto queryArrearageByBuildingCode(String companyId,String[] buildingCodes);
}
