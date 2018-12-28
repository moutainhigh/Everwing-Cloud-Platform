package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AcDelayAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AcDelayAccountMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcDelayAccount record);

    int insertSelective(AcDelayAccount record);

    AcDelayAccount selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcDelayAccount record);

    int updateByPrimaryKey(AcDelayAccount record);

    AcDelayAccount selectByAcAccountIdAndType(@Param("acAccountId") String acAccountId,@Param("type") int code);

    List<AcDelayAccount> selectByHouseCodeNew(String  houseCodeNew);

    Double selectByHouseCodeNewAndType(@Param("houseCodeNew") String houseCodeNew,@Param("type") int type);

}