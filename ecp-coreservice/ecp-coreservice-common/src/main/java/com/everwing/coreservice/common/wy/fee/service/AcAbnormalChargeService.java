package com.everwing.coreservice.common.wy.fee.service;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.fee.dto.AbnormalChargeAcccountDto;
import com.everwing.coreservice.common.wy.fee.entity.AcAbnormalCharge;

import java.util.List;

/**
 * 异常计费处理service
 */
public interface AcAbnormalChargeService {
     BaseDto listAbnormalCharge(String companyId, AcAbnormalCharge param);

     BaseDto listAbnormalChargAccount(String companyId,AbnormalChargeAcccountDto parm);

     BaseDto abnormalChargeAccountHandle(String companyId,AcAbnormalCharge parm);
}
