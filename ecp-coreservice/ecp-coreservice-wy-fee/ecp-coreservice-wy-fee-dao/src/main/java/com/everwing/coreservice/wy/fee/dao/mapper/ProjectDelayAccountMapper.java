package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.ProjectDelayAccount;
import org.apache.ibatis.annotations.Param;

public interface ProjectDelayAccountMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProjectDelayAccount record);

    int insertSelective(ProjectDelayAccount record);

    ProjectDelayAccount selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProjectDelayAccount record);

    int updateByPrimaryKey(ProjectDelayAccount record);

    ProjectDelayAccount selectByProjectAccountIdAndType(@Param("projectAccountId") String projectAccountId,@Param("type") int type);

}