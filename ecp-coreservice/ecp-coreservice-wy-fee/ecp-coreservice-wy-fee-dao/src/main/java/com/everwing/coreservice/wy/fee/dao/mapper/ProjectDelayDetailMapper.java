package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AccountAddDelayParameter;
import com.everwing.coreservice.common.wy.fee.entity.IncomingParameter;
import com.everwing.coreservice.common.wy.fee.entity.ProjectDelayDetail;

import java.util.List;

public interface ProjectDelayDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProjectDelayDetail record);

    int insertSelective(ProjectDelayDetail record);

    ProjectDelayDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProjectDelayDetail record);

    int updateByPrimaryKey(ProjectDelayDetail record);

    List<AccountAddDelayParameter> listPageSelectByAllDetailAndIdAll(IncomingParameter incomingParameter);

    AccountAddDelayParameter counCycleDetailAmount( IncomingParameter incomingParameter);

    List<ProjectDelayDetail> selectByOperaAndType(String operaId);
}
