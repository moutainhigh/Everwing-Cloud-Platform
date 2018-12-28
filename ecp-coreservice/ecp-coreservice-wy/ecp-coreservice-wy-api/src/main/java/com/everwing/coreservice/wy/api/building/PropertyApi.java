package com.everwing.coreservice.wy.api.building;/**
 * Created by wust on 2017/8/4.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.entity.order.TcOrderChangeAssetComplaint;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerSearch;
import com.everwing.coreservice.common.wy.entity.property.property.ProprietorInfo;
import com.everwing.coreservice.common.wy.entity.property.property.TPropertyChangingHistory;
import com.everwing.coreservice.common.wy.entity.property.property.TPropertyChangingHistorySearch;
import com.everwing.coreservice.common.wy.service.building.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/4
 * @author wusongti@lii.com.cn
 */
@Component
public class PropertyApi {
    @Autowired
    private PropertyService propertyService;

    public RemoteModelResult<BaseDto> listPageChangingHistory(String companyId, TPropertyChangingHistorySearch tPropertyChangingHistorySearch) {
        RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
        result.setModel(propertyService.listPageChangingHistory(companyId,tPropertyChangingHistorySearch));
        return result;
    }

    public RemoteModelResult<MessageMap> insertChangingHistory(String companyId, TPropertyChangingHistory tPropertyChangingHistory){
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(propertyService.insertChangingHistory(companyId,tPropertyChangingHistory));
        return result;
    }

    public RemoteModelResult<List<ProprietorInfo>> getProprietorInfoByBuildingCode(String companyId, String buildingCode){
        RemoteModelResult<List<ProprietorInfo>> result = new RemoteModelResult<>();
        result.setModel(propertyService.getProprietorInfoByBuildingCode(companyId,buildingCode));
        return result;
    }

    
    public RemoteModelResult<BaseDto> listPageCustomerInEntery(String companyId, CustomerSearch customerSearch) {
    	RemoteModelResult<BaseDto> result = new RemoteModelResult<>();
    	result.setModel(propertyService.listPageCustomerInEntery(companyId,customerSearch));
    	return result;
    }



    public RemoteModelResult<MessageMap> requestReadingMeter(String companyId,String projectId, int type,TcReadingTask tcReadingTask){
        MessageMap mm = propertyService.requestReadingMeter(companyId,projectId,type,tcReadingTask);
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(mm);
        return result;
    }
    
    public RemoteModelResult<BaseDto> requestReadingMeterNew(WyBusinessContext ctx, TcOrderChangeAssetComplaint tcOrderChangeAssetComplaint){
    	
    	return new RemoteModelResult<BaseDto>(this.propertyService.requestReadingMeterNew(ctx, tcOrderChangeAssetComplaint));
    }
    
    /**
     * 根据buildCode,projectId查询最新一期账单
     */
    public RemoteModelResult<BaseDto> getBillByBuildCode(WyBusinessContext ctx,String buildCode){
    	return new RemoteModelResult<BaseDto>(this.propertyService.getBillByBuildCode(ctx, buildCode));
    }
    
    /**
     * 手动计费
     */
    public RemoteModelResult<BaseDto> manualBill(WyBusinessContext ctx,String buildCode,String buildName){
    	return new RemoteModelResult<BaseDto>(this.propertyService.manualBill(ctx, buildCode, buildName));
    }

	public RemoteModelResult<BaseDto> checkCollingDatas(WyBusinessContext ctx,String buildingCode, String projectId) {
    	return new RemoteModelResult<BaseDto>(this.propertyService.checkCollingDatas(ctx, buildingCode, projectId));
	}
}
