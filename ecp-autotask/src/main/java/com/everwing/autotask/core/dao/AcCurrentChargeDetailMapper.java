package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.fee.entity.AcCurrentChargeDetail;
import org.apache.ibatis.annotations.Param;

public interface AcCurrentChargeDetailMapper {
//    int deleteByPrimaryKey(String id);

//    int insert(AcCurrentChargeDetail record);

//    int insertSelective(AcCurrentChargeDetail record);

    AcCurrentChargeDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcCurrentChargeDetail record);

    int updateByPrimaryKey(AcCurrentChargeDetail record);
    
    AcCurrentChargeDetail selectNewestChargeDetail (@Param("houseCodeNew") String houseCodeNew , @Param("chargeType") int accountType);
    
    double selectChargeAmountForLateFee(@Param("houseCodeNew") String houseCodeNew,@Param("dateStr") String dateStr,@Param("accountType") int accountType);
    
//    List<AcCurrentChargeDetail> getChargeDetailListByHouseCode(@Param("houseCodeNew") String houseCodeNew,@Param("accountType") int accountType,@Param("list") List<String> monthInfo);
    
}