package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.ProjectCycleAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectCycleAccountMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProjectCycleAccount record);

    int insertSelective(ProjectCycleAccount record);

    ProjectCycleAccount selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProjectCycleAccount record);

    int updateByPrimaryKey(ProjectCycleAccount record);

    ProjectCycleAccount selectByProjectAccountIdAndType(@Param("projectAccountId") String projectAccountId,@Param("type") short Type);

    List<ProjectCycleAccount> selectByProjectAccountIdAndAll(String projectAccountId);

    ProjectCycleAccount checkExists(@Param("projectAccountId") String projectAccountId, @Param("code") int code);
}