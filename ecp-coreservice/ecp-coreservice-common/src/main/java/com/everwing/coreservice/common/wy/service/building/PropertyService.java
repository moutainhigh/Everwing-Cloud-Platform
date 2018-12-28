package com.everwing.coreservice.common.wy.service.building;/**
 * Created by wust on 2017/8/4.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.entity.order.TcOrderChangeAssetComplaint;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerSearch;
import com.everwing.coreservice.common.wy.entity.property.property.ProprietorInfo;
import com.everwing.coreservice.common.wy.entity.property.property.TPropertyChangingHistory;
import com.everwing.coreservice.common.wy.entity.property.property.TPropertyChangingHistorySearch;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/4
 * @author wusongti@lii.com.cn
 */
public interface PropertyService {
    BaseDto listPageChangingHistory(String companyId, TPropertyChangingHistorySearch tPropertyChangingHistorySearch);

    MessageMap insertChangingHistory(String companyId, TPropertyChangingHistory tPropertyChangingHistory);

    List<ProprietorInfo> getProprietorInfoByBuildingCode(String companyId,String buildingCode);

    /**
     * 抄表申请
     * @param companyId
     * @param type  0水表，1电表
     * @param tcReadingTask
     * @return
     */
    MessageMap requestReadingMeter(String companyId,String projectId,int type,TcReadingTask tcReadingTask);
    
    /**
     * 资产变更水电表申请
     */
   BaseDto requestReadingMeterNew(WyBusinessContext ctx, TcOrderChangeAssetComplaint tcOrderChangeAssetComplaint);
   
   /**
    * 根据buildCode和projectId查询最新一条账单数据
    */
   BaseDto getBillByBuildCode(WyBusinessContext ctx, String buildCode);
   
   /**
    * 手动计费
    * @param ctx
    * @param buildCode
    * @param buildName
    * @return
    */
   BaseDto manualBill(WyBusinessContext ctx, String buildCode, String buildName);

   BaseDto listPageCustomerInEntery(String companyId, CustomerSearch customerSearch);

   BaseDto checkCollingDatas(WyBusinessContext ctx, String buildingCode, String projectId);
}
