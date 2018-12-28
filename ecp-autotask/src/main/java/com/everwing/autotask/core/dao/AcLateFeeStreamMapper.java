package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.fee.entity.AcLateFeeStream;
import org.apache.ibatis.annotations.Param;

public interface AcLateFeeStreamMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcLateFeeStream record);

    int insertSelective(AcLateFeeStream record);

    AcLateFeeStream selectByPrimaryKey(String id);
    
    AcLateFeeStream selectLastOneByDelayAccountId(@Param("houseCodeNew") String houseCodeNew,@Param("accountType") int accountType);

    int updateByPrimaryKeySelective(AcLateFeeStream record);

    int updateByPrimaryKey(AcLateFeeStream record);

    int insertBySelect(AcLateFeeStream acLateFeeStream);

    int checkBefore(AcLateFeeStream acLateFeeStream);
    
    AcLateFeeStream getLastLateFeeStream( @Param("houseCodeNew") String houseCodeNew,@Param("accountType") int accountType);
    
}