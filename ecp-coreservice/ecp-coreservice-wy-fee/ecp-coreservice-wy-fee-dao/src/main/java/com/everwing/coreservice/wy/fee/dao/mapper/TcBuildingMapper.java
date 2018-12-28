
package com.everwing.coreservice.wy.fee.dao.mapper;

public interface TcBuildingMapper
{
	  /**
     * @describe 新增水电表位置时，提供给选择建筑地址
     * @return
     */

	
	String getBuildingCodeByHouseCode(String buildingCode);
	

}

