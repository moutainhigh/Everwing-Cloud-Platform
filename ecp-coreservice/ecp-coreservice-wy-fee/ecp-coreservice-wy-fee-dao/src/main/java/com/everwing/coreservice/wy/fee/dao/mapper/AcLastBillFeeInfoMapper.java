package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AcLastBillFeeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AcLastBillFeeInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcLastBillFeeInfo record);

    int insertSelective(AcLastBillFeeInfo record);

    AcLastBillFeeInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcLastBillFeeInfo record);

    int updateByPrimaryKey(AcLastBillFeeInfo record);
    
    //修改上月欠费总金额字段专用
    int updateByPrimaryKeyForLastBill( AcLastBillFeeInfo record );

    AcLastBillFeeInfo selectByCodeAccountIdAndType(@Param("houseCodeNew") String houseCodeNew, @Param("acAccountId") String acAccountId, @Param("type") int type);

    List<AcLastBillFeeInfo> selectByHouseCodeNew(String houseCodeNow);

    int updateByHouseCodeAndAccountId( AcLastBillFeeInfo record );

    int updateByHouseCodeAndAccountType( AcLastBillFeeInfo record );


    AcLastBillFeeInfo selectByHouseCodeAndType(@Param("houseCodeNew") String houseCode,@Param("type") Integer accountType);

    int updateAmount(@Param("amount")double i,@Param("id") String id);
}