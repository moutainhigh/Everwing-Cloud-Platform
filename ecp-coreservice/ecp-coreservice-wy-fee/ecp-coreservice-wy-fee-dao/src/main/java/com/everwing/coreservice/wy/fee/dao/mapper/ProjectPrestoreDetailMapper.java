package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AccountAddPrestoreParameter;
import com.everwing.coreservice.common.wy.fee.entity.IncomingParameter;
import com.everwing.coreservice.common.wy.fee.entity.ProjectPrestoreDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectPrestoreDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProjectPrestoreDetail record);

    int insertSelective(ProjectPrestoreDetail record);

    ProjectPrestoreDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProjectPrestoreDetail record);

    int updateByPrimaryKey(ProjectPrestoreDetail record);


    List<AccountAddPrestoreParameter> listPageSelectByAllPrestoreAndIdAll(IncomingParameter incomingParameter);

    AccountAddPrestoreParameter counPrestoreDetailAmount(IncomingParameter incomingParameter);

    List<ProjectPrestoreDetail> selectByOperaIdAndType(@Param("businessOperaDetailId")String operaId);

    ProjectPrestoreDetail selectProjectPrestoreDetailByOrderId(String orderId);


}