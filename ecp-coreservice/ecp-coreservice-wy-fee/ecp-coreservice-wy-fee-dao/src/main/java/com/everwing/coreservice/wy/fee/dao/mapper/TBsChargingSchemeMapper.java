package com.everwing.coreservice.wy.fee.dao.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @describe （物业，本体，水费，电费）方案相关
 * @author QHC
 *
 */
public interface TBsChargingSchemeMapper {

	Double findTaxRate(@Param("schemeType") Integer type, @Param("buildingCode") String buildingCode);

}
