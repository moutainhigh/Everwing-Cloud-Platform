package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.dto.AcBusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.BusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.entity.AcBusinessOperaDetail;

import java.util.List;

public interface AcBusinessOperaDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcBusinessOperaDetail record);

    int insertSelective(AcBusinessOperaDetail record);

    AcBusinessOperaDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcBusinessOperaDetail record);

    int updateByPrimaryKey(AcBusinessOperaDetail record);

    void insertOperaDetail(AcBusinessOperaDetailDto acBusinessOperaDetailDto);

    List<AcBusinessOperaDetail> selectByCondition(BusinessOperaDetailDto businessOperaDetailDto);
}