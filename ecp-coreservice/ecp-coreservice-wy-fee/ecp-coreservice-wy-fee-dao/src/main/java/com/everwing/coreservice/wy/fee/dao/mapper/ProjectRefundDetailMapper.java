package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.ProjectRefundDetail;

public interface ProjectRefundDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProjectRefundDetail record);

    int insertSelective(ProjectRefundDetail record);

    ProjectRefundDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProjectRefundDetail record);

    int updateByPrimaryKey(ProjectRefundDetail record);
}