package com.everwing.coreservice.wy.dao.mapper.account;

import com.everwing.coreservice.common.wy.fee.entity.AcSpecialDetail;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AcSpecialDetailMapper {
    List<AcSpecialDetail> getPushDataForSpcialAccount(Map<String, String> paramMap);

}