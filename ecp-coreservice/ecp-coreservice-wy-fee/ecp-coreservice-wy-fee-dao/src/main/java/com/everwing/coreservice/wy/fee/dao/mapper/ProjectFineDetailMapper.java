package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AccountAddFineDetailParameter;
import com.everwing.coreservice.common.wy.fee.entity.IncomingParameter;
import com.everwing.coreservice.common.wy.fee.entity.ProjectFineDetail;

import java.util.List;

public interface ProjectFineDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProjectFineDetail record);

    int insertSelective(ProjectFineDetail record);

    ProjectFineDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProjectFineDetail record);

    int updateByPrimaryKey(ProjectFineDetail record);

    List<AccountAddFineDetailParameter> listPageSelectProjectFineDetail(IncomingParameter incomingParameter);

    AccountAddFineDetailParameter countProjectFineDetail(IncomingParameter incomingParameter);
}