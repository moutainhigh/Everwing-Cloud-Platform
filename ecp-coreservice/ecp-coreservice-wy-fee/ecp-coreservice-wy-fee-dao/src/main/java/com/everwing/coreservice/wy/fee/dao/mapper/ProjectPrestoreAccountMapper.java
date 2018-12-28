package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.ProjectPrestoreAccount;
import org.apache.ibatis.annotations.Param;

public interface ProjectPrestoreAccountMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProjectPrestoreAccount record);

    int insertSelective(ProjectPrestoreAccount record);

    ProjectPrestoreAccount selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProjectPrestoreAccount record);

    int updateByPrimaryKey(ProjectPrestoreAccount record);

    ProjectPrestoreAccount selectByProjectAccountIdAndType(@Param("projectAccountId") String projectAccountId,@Param("prestoreType") int prestoreType);

    ProjectPrestoreAccount selectByProjectIdAndType(@Param("projectId") String projectId,@Param("type") int type);
}