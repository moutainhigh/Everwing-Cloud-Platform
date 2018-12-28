package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.ProjectAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectAccountMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProjectAccount record);

    int insertSelective(ProjectAccount record);

    ProjectAccount selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProjectAccount record);

    int updateByPrimaryKey(ProjectAccount record);

    ProjectAccount selectByCompanyIdAndProjectId(@Param("companyId") String companyId,@Param("projectId") String projectId);
    
    List<ProjectAccount> selectByProjectId( @Param("projectId") String projectId );
    
    int updateByIdAndVersion(ProjectAccount projectAccount);

    List<ProjectAccount> selectProject(List<String> projectIdList);

    List<ProjectAccount> selectProjectById(@Param("projectId") String projectId);

    ProjectAccount getProjectAccountById(@Param("projectId") String projectId);
}