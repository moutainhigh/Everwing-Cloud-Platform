package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AcCurrentCharge;
import org.apache.ibatis.annotations.Param;

public interface AcCurrentChargeMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcCurrentCharge record);

    int insertSelective(AcCurrentCharge record);

    AcCurrentCharge selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcCurrentCharge record);

    int updateByPrimaryKey(AcCurrentCharge record);
    
    AcCurrentCharge selectByAccountIdTypeAndHouseCodeNew(@Param("accountId") String acAccountId,@Param("type") int type,@Param("houseCodeNew") String houseCodeNew);

    String selectAccountId(String houseCode);
}