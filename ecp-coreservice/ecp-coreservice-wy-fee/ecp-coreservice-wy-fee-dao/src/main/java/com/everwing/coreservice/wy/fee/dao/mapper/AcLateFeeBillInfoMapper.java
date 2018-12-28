package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.entity.AcLateFeeBillInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AcLateFeeBillInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcLateFeeBillInfo record);

    int insertSelective(AcLateFeeBillInfo record);

    AcLateFeeBillInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcLateFeeBillInfo record);

    int updateByPrimaryKey(AcLateFeeBillInfo record);
    
    /**
     * 根据传递参数的违约金计费规则信息查询是否已经存在
     * @param entity 违约金计费规则信息
     * @return 规则信息集合，如果存在，不需要新建，如果不存在则作废之前的，新增这一个
     */
    List<AcLateFeeBillInfo> getLateFeeBillInfoByContent ( AcLateFeeBillInfo entity );
    
    AcLateFeeBillInfo getAcLateFeeBillInfoByProject(@Param("projectId") String projectId,@Param("accountType") int accountType);
    
    /**
     * 修改当前正在使用的规则为不再使用
     */
    int updateIsUsedToNo(String projectId);
    
    double getNewestPrincipalByHouseCode(@Param("houseCodeNew") String houseCodeNew,@Param("deadLine") String deadLine,@Param("accountType") int accountType);

    
}