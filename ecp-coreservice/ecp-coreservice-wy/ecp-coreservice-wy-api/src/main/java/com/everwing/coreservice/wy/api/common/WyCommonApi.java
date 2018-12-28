package com.everwing.coreservice.wy.api.common;/**
 * Created by wust on 2017/10/11.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.common.select.asset.AssetSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.asset.AssetSelectSearch;
import com.everwing.coreservice.common.wy.entity.common.select.customer.CustomerSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.customer.CustomerSelectSearch;
import com.everwing.coreservice.common.wy.entity.common.select.vehicle.VehicleSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.vehicle.VehicleSelectSearch;
import com.everwing.coreservice.common.wy.entity.other.AgentCodeSearch;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerSearch;
import com.everwing.coreservice.common.wy.service.common.WyCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/10/11
 * @author wusongti@lii.com.cn
 */
@Component
public class WyCommonApi {
    @Autowired
    private WyCommonService wyCommonService;

    public RemoteModelResult<BaseDto> listPageCustomer(String companyId, CustomerSearch customerSearch) {
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(wyCommonService.listPageCustomer(companyId,customerSearch));
        return result;
    }

    public RemoteModelResult<BaseDto> listPageBuilding(WyBusinessContext ctx, TcBuildingSearch tcBuildingSearch) {
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(wyCommonService.listPageBuilding(ctx,tcBuildingSearch));
        return result;
    }
    
    
    public RemoteModelResult<BaseDto> custAndBuildingInfo(String custId, String custType,String buildingCode) {
    	return new RemoteModelResult<BaseDto>(wyCommonService.custAndBuildingInfo(CommonUtils.getCompanyIdByCurrRequest(),custId,custType,buildingCode));
    }
    
    
    public RemoteModelResult<BaseDto> listPageCC(AgentCodeSearch agentCodeSearch) {
    	return new RemoteModelResult<BaseDto>(wyCommonService.listPageCC(CommonUtils.getCompanyIdByCurrRequest(),agentCodeSearch));
    }
    
    public RemoteModelResult<BaseDto> listPageAgentCode(AgentCodeSearch agentCodeSearch) {
    	return new RemoteModelResult<BaseDto>(wyCommonService.listPageAgentCode(CommonUtils.getCompanyIdByCurrRequest(),agentCodeSearch));
    }

    public RemoteModelResult queryArrearageByBuildingCode(String companyId,String[] buildingCodes){
        return new RemoteModelResult(wyCommonService.queryArrearageByBuildingCode(companyId,buildingCodes));
    }

    public RemoteModelResult<List<CustomerSelectList>> findCustomerSelect(WyBusinessContext ctx,CustomerSelectSearch customerSelectSearch) {
        RemoteModelResult<List<CustomerSelectList>> remoteModelResult = new RemoteModelResult<>();
        List<CustomerSelectList> customerSelectLists = wyCommonService.findCustomerSelect(ctx,customerSelectSearch);
        remoteModelResult.setModel(customerSelectLists);
        return remoteModelResult;
    }

    public RemoteModelResult<List<AssetSelectList>> findAssetSelect(WyBusinessContext ctx, AssetSelectSearch assetSelectSearch) {
        RemoteModelResult<List<AssetSelectList>> remoteModelResult = new RemoteModelResult<>();
        List<AssetSelectList> assetSelectLists = wyCommonService.findAssetSelect(ctx,assetSelectSearch);
        remoteModelResult.setModel(assetSelectLists);
        return remoteModelResult;
    }

    public RemoteModelResult<List<VehicleSelectList>> findVehicleSelect(WyBusinessContext ctx, VehicleSelectSearch vehicleSelectSearch) {
        RemoteModelResult<List<VehicleSelectList>> remoteModelResult = new RemoteModelResult<>();
        List<VehicleSelectList> lists = wyCommonService.findVehicleSelect(ctx,vehicleSelectSearch);
        remoteModelResult.setModel(lists);
        return remoteModelResult;
    }
}
