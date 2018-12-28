package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.dto.LateFeeInfoDto;
import com.everwing.coreservice.common.wy.fee.entity.AcLateFeeStream;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AcLateFeeStreamMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcLateFeeStream record);

    int insertSelective(AcLateFeeStream record);

    AcLateFeeStream selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcLateFeeStream record);

    int updateByPrimaryKey(AcLateFeeStream record);

    int insertBySelect(AcLateFeeStream acLateFeeStream);

    int checkBefore(AcLateFeeStream acLateFeeStream);
    
    AcLateFeeStream getLastLateFeeStream( @Param("houseCodeNew") String houseCodeNew,@Param("accountType") int accountType);

    List<Map<String,Object>> selectBySearchObj(LateFeeInfoDto lateFeeInfoDto);

    List<Map<String,Object>> listPageBySearchObj(LateFeeInfoDto lateFeeInfoDto);

    List<AcLateFeeStream> selectByOperaId(String operaId);


    AcLateFeeStream selectByOperaIdandType(@Param("operaId") String operaId,@Param("houseCodeNew") String houseCodeNew,@Param("accountType") int type);
}