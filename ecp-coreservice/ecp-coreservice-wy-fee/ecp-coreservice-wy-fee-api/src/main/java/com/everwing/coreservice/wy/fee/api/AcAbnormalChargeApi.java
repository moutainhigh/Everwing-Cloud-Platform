package com.everwing.coreservice.wy.fee.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.fee.dto.AbnormalChargeAcccountDto;
import com.everwing.coreservice.common.wy.fee.entity.AcAbnormalCharge;
import com.everwing.coreservice.common.wy.fee.service.AcAbnormalChargeService;
import org.springframework.stereotype.Component;

/**
 * 异常计费api
 */
@Component
public class AcAbnormalChargeApi {

    @Reference(check = false)
    private AcAbnormalChargeService acAbnormalChargeService;

    public RemoteModelResult listAbnormalCharge( AcAbnormalCharge param){

       return  new RemoteModelResult (acAbnormalChargeService.listAbnormalCharge(CommonUtils.getCompanyIdByCurrRequest(),param));
    }

    public RemoteModelResult listAbnormalChargAccount(AbnormalChargeAcccountDto parm){

        return new RemoteModelResult(acAbnormalChargeService.listAbnormalChargAccount(CommonUtils.getCompanyIdByCurrRequest(),parm));
    }

    public RemoteModelResult abnormalChargeAccountHandle(AcAbnormalCharge parm){

        return new RemoteModelResult(acAbnormalChargeService.abnormalChargeAccountHandle(CommonUtils.getCompanyIdByCurrRequest(),parm));
    }
}
