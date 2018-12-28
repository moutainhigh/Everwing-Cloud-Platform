package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AcSpecialDetail;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AcSpecialDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcSpecialDetail record);

    int insertSelective(AcSpecialDetail record);

    AcSpecialDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcSpecialDetail record);

    int updateByPrimaryKey(AcSpecialDetail record);

    BigDecimal selectNewestAmount(String houseCodeNew);

    void insertBySelect(AcSpecialDetail acSpecialDetail);

    int checkInsert(AcSpecialDetail acSpecialDetail);

    List<AcSpecialDetail> selectByOperaId(String operaId);


    AcSpecialDetail selectByOperaIdAndType(@Param("operaId") String operaId,@Param("houseCodeNew") String houseCodeNew,@Param("acoountType") int type);

    double findDeductionByDetail(@Param("businessType") int businessType,@Param("chargingType") int chargingType,@Param("projectId") String projectId,@Param("startTime") String startTime,@Param("endTime") String endTime);
}