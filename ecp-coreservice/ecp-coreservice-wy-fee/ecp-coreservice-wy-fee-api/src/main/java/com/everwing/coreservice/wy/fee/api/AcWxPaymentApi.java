package com.everwing.coreservice.wy.fee.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.dto.BuildingAndCustDTO;
import com.everwing.coreservice.common.wy.fee.dto.BillDetailPageDto;
import com.everwing.coreservice.common.wy.fee.dto.BillOfYearDto;
import com.everwing.coreservice.common.wy.fee.dto.BuildingAndCustInfoDto;
import com.everwing.coreservice.common.wy.fee.dto.PayDetailDto;
import com.everwing.coreservice.common.wy.fee.entity.AcWxBinding;
import com.everwing.coreservice.common.wy.fee.service.AcAccountPageService;
import com.everwing.coreservice.common.wy.fee.service.AcAccountService;
import com.everwing.coreservice.common.wy.fee.service.AcWxBindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 微信小程序支付API
 * @author zgrf
 * @create 2018/7/15
 */
@Component
public class AcWxPaymentApi {

    @Autowired
    private AcWxBindingService acWxBindingService;

    @Autowired
    private AcAccountService acAccountService;

    @Reference
    private AcAccountPageService acAccountPageService;

    public RemoteModelResult<String> bindUseridAndOpenId(String companyId,String openId){
        AcWxBinding acWxBinding = new AcWxBinding();
        acWxBinding.setOpenId(openId);
        String userId = acWxBindingService.bindOpenIdAndUserId(companyId,openId);
        return new RemoteModelResult<String>(userId);
    }

    public RemoteModelResult<Integer> bindUserIdAndMobile(String companyId,String userId,String mobile) {
        int count = acWxBindingService.bindUserAndMobile(companyId,userId,mobile);
        return new RemoteModelResult<Integer>(count);
    }

    public RemoteModelResult<AcWxBinding> queryByUserId(String companyId,String userId) {
        AcWxBinding acWxBinding = acWxBindingService.getAcWxBindingByUserId(companyId,userId);
        return new RemoteModelResult<AcWxBinding>(acWxBinding);
    }

    public RemoteModelResult<List<Map>> queryArrearsByHouseCodeNews(String companyId,List<String> houseCodenews,String projectId) {
        List<Map> list = acAccountService.queryArrearsByHouseCodeNews(companyId,houseCodenews,projectId);
        return new RemoteModelResult<List<Map>>(list);
    }

    public RemoteModelResult<Map> queryArrearsByhouseCode(String companyId,String houseCode,String projectId) {
        Map map = acAccountService.queryArrearsByhouseCode(companyId,houseCode,projectId);
        return new RemoteModelResult<Map>(map);
    }

    public RemoteModelResult<List<PayDetailDto>> getCostByHouseCode(String companyId,String houseCodeNew) {
        List<PayDetailDto> list = acAccountService.getCostByHouseCode(companyId,houseCodeNew);
        return new RemoteModelResult<List<PayDetailDto>>(list);
    }

    public RemoteModelResult<List<BuildingAndCustDTO>> getBuindingAndCustByMobile(String companyId, String mobile,String projectId) {
        List<BuildingAndCustDTO> list = acAccountService.getBuindingAndCustByMobile(companyId, mobile,projectId);
        return new RemoteModelResult<>(list);
    }

    public RemoteModelResult<List<Map>> queryArrearsByBuildingName(String companyId, String buindingName,String projectId) {
        List<Map> list = acAccountService.queryArrearsByBuildingName(companyId, buindingName,projectId);
        return new RemoteModelResult<>(list);
    }

    public RemoteModelResult<BuildingAndCustInfoDto> queryBuildingDetailsByhouseCode(String companyId,String projectId,String houseCode) {
        BuildingAndCustInfoDto BuildingAndCustInfoDto = acAccountService.queryBuildingDetailsByhouseCode(companyId,projectId,houseCode);
        return new RemoteModelResult<>(BuildingAndCustInfoDto);
    }

    public RemoteModelResult<List<Map<String,String>>> queryCustByBuildId(String companyId,String projectId,String buildId) {
        List<Map<String,String>> list = acAccountService.queryCustByBuildId(companyId,projectId,buildId);
        return new RemoteModelResult<List<Map<String,String>>>(list);
    }

    public RemoteModelResult<List<BillOfYearDto>> queryBillByhouseCodeAndYear(String companyId, String projectId, String year, String houseCode) {
        List<BillOfYearDto> list = acAccountService.queryBillByhouseCodeAndYear(companyId,projectId,year,houseCode);
        return new RemoteModelResult<>(list);
    }

    public RemoteModelResult queryBillDetailById(String companyId,String id) {
        return new RemoteModelResult<BaseDto>(acAccountPageService.downLoadBill(companyId,id));
    }

    public RemoteModelResult<List<BillDetailPageDto>> queryByHouseCodeNewAndYear (String companyId,String houseCode,String year) {
        return new RemoteModelResult<List<BillDetailPageDto>>(acAccountPageService.queryByHouseCodeNewAndYear(companyId,houseCode,year));
    }
}
