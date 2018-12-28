package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AcBillDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AcBillDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcBillDetail record);

    int insertSelective(AcBillDetail record);

    AcBillDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcBillDetail record);

    int updateByPrimaryKey(AcBillDetail record);
    
    List<String> getAllBuildingCodeByProjectId(String projectId);

    List<AcBillDetail> selectByHouseCodeNewAndYear(@Param("houseCode") String houseCode, @Param("year") String year);
    
    List<AcBillDetail> queryByHouseCodeNewAndYear(@Param("houseCode") String houseCode,@Param("year") String year);

}