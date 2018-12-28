package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AcSpecialAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AcSpecialAccountMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcSpecialAccount record);

    int insertSelective(AcSpecialAccount record);

    AcSpecialAccount selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcSpecialAccount record);

    int updateByPrimaryKey(AcSpecialAccount record);

    AcSpecialAccount selectByAccountIdAndTypeAndHouseCodeNew(@Param("acAccountId") String acAccountId, @Param("type") int code,@Param("houseCodeNew") String houseCodeNew);

    List<AcSpecialAccount> selectByHouseCodeNew(String houseCodeNew);
}