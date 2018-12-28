package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.fee.entity.AcSpecialDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@Repository
public interface AcSpecialDetailMapper {
    List<AcSpecialDetail> getPushDataForSpcialAccount(Map<String, String> paramMap);

}