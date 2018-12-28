package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.fee.entity.AcDelayAccount;
import org.apache.ibatis.annotations.Param;

public interface AcDelayAccountMapper {
	
    AcDelayAccount selectByPrimaryKey(String id);
    
    AcDelayAccount selectByProjectAndAccountType(@Param("houseCodeNew") String houseCodeNew,@Param("projectId") String projectId,@Param("accountType") int accountType);
    
    int updateByPrimaryKeySelective(AcDelayAccount acDelayAccount); 
    
    int insert(AcDelayAccount acDelayAccount);
    
    
    
}