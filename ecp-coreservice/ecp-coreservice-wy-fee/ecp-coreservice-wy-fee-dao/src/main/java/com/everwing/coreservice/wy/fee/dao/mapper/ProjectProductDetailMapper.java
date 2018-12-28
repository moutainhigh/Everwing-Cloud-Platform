package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AccountAddProductDetail;
import com.everwing.coreservice.common.wy.fee.entity.IncomingParameter;
import com.everwing.coreservice.common.wy.fee.entity.ProjectProductDetail;

import java.util.List;

public interface ProjectProductDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProjectProductDetail record);

    int insertSelective(ProjectProductDetail record);

    ProjectProductDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProjectProductDetail record);

    int updateByPrimaryKeyWithBLOBs(ProjectProductDetail record);

    int updateByPrimaryKey(ProjectProductDetail record);

    List<AccountAddProductDetail> listPageSelectProjectProductDetail(IncomingParameter incomingParameter);

    AccountAddProductDetail countProjectProductDetail( IncomingParameter incomingParameter);
}