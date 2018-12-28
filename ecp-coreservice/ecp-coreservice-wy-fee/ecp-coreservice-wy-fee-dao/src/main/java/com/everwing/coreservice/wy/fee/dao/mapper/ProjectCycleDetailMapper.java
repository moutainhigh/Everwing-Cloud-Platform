package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AccountAddCycleParameter;
import com.everwing.coreservice.common.wy.fee.entity.IncomingParameter;
import com.everwing.coreservice.common.wy.fee.entity.ProjectCycleDetail;

import java.util.List;

public interface ProjectCycleDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProjectCycleDetail record);

    int insertSelective(ProjectCycleDetail record);

    ProjectCycleDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProjectCycleDetail record);

    int updateByPrimaryKey(ProjectCycleDetail record);

    List<ProjectCycleDetail> selectByAllDetailAndIdAll(String cycleId);

    List<AccountAddCycleParameter> listPageSelectByAllDetailAndIdAll(IncomingParameter incomingParameter);
    AccountAddCycleParameter counCycleDetailAmount(IncomingParameter incomingParameter);

    List<ProjectCycleDetail> selectByOperaIdAndType(String businessOperaDetail);
}