/**
 * @Title: BuildingGateMapper.java
 * @Package com.everwing.mk.server.mapper
 * @Description: TODO
 * Copyright: Copyright (c) 2011 
 * Company:武汉闻风多奇软件开发有限公司
 * 
 * @author wangtao
 * @date 2015-5-23 下午4:14:50
 * @version V1.0
 */

package com.everwing.coreservice.wy.dao.mapper.gating.buildinggate;

import com.everwing.coreservice.common.wy.entity.gating.BuildingGate;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface BuildingGateMapper {
	List<BuildingGate> listPageBuildingGate(BuildingGate BuildingGate)  throws DataAccessException;
	
	List<BuildingGate> listAllBuildingGate()  throws DataAccessException;
	
	BuildingGate getBuildingGateById(String id)  throws DataAccessException;
	
    int insertBuildingGate(BuildingGate BuildingGate)  throws DataAccessException;
    
    int updateBuildingGate(BuildingGate BuildingGate)  throws DataAccessException;

    int deleteBuildingGate(String id)  throws DataAccessException;
    
    List<BuildingGate> listBuildingGateByGateId(String gateId)  throws DataAccessException;
    
    int BuildingGateBinding(Map<String,Object> map)  throws DataAccessException;
    
    int BuildingGateUnwrap(Map<String,Object> map)  throws DataAccessException;
    
    //根据房屋id获取门控机账号
    List<String> getGateIdByBuildingId(List<String> ids)  throws DataAccessException;
    
    int batchInsertBuildingGate(List<BuildingGate> list)  throws DataAccessException;
    
    List<BuildingGate> getBuildingGateByBuildingId(String buildingId)  throws DataAccessException;
    
    //根据门控机的code,删除房屋门控机关联关系
    int deleteBuildingGateByGateCode(String gateCode) throws DataAccessException;
    
    
}
