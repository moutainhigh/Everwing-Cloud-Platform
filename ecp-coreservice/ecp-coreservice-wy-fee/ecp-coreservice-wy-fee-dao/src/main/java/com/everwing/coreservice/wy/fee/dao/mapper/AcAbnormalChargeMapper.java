package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.dto.AbnormalChargeAcccountDto;
import com.everwing.coreservice.common.wy.fee.entity.AcAbnormalCharge;

import java.util.List;

public interface AcAbnormalChargeMapper {

    List<AcAbnormalCharge> listPageAbnormalCharg(AcAbnormalCharge param);

    int insertAcAbnormalCharge(AcAbnormalCharge param);

}