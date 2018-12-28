package com.everwing.coreservice.wy.dao.mapper;/**
 * Created by wust on 2017/10/11.
 */

import com.everwing.coreservice.common.wy.entity.common.select.asset.AssetSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.asset.AssetSelectSearch;
import com.everwing.coreservice.common.wy.entity.common.select.customer.CustomerSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.customer.CustomerSelectSearch;
import com.everwing.coreservice.common.wy.entity.common.select.vehicle.VehicleSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.vehicle.VehicleSelectSearch;
import com.everwing.coreservice.common.wy.entity.cust.enterprise.EnterpriseCustNew;
import com.everwing.coreservice.common.wy.entity.cust.person.PersonCustNew;
import com.everwing.coreservice.common.wy.entity.other.AgentCodeSearch;
import com.everwing.coreservice.common.wy.entity.other.pojo.TcAgentCode;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerList;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerSearch;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Function:一些公共方法
 * Reason:注意，该模块是存放公共组件的地方，不要什么代码都加进来
 * Date:2017/10/11
 * @author wusongti@lii.com.cn
 */
public interface CommonMapper {
    /**
     * 分页查询客户信息
     * @param customerSearch
     * @return
     */
    List<CustomerList> listPageCustomer(CustomerSearch customerSearch) throws DataAccessException;
    
    /**
     * 分页查询资产信息
     * @param condition
     * @return
     */
    List<TcBuildingList> listPageBuilding(TcBuildingSearch condition) throws DataAccessException;
    
    /**
     * @description 按条件分页列出客服账号
     */
    List<Map> listPageCC(AgentCodeSearch agentCodeSearch) throws DataAccessException;
    
    List<TcAgentCode> listPageAgentCode(AgentCodeSearch agentCodeSearch) throws DataAccessException;
    
    BigDecimal sumDepositByCust(@Param("personCustList") List<PersonCustNew> personCustNewList, @Param("entCustList") List<EnterpriseCustNew> enterpriseCustNewList) throws DataAccessException;
    
    PersonCustNew getPersonCustById(String custId) throws DataAccessException;
    
    EnterpriseCustNew getEntCustById(String entId) throws DataAccessException;

    List<String> selectJsonListByBuildingCode(@Param("buildingCode")String buildingCode) throws DataAccessException;


    /**
     * 公共组件：客户下拉选择框
     * @param customerSelectSearch
     * @return
     * @throws DataAccessException
     */
    List<CustomerSelectList> findCustomerSelect(CustomerSelectSearch customerSelectSearch) throws DataAccessException;

    /**
     * 公共组件：资产下拉选择框
     * @param assetSelectSearch
     * @return
     * @throws DataAccessException
     */
    List<AssetSelectList> findAssetSelect(AssetSelectSearch assetSelectSearch) throws DataAccessException;

    /**
     * 公共组件：车辆下拉选择框
     * @param vehicleSelectSearch
     * @return
     * @throws DataAccessException
     */
    List<VehicleSelectList> findVehicleSelect(VehicleSelectSearch vehicleSelectSearch) throws DataAccessException;
}
