package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.dto.*;
import com.everwing.coreservice.common.wy.fee.entity.AcAbnormalCharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AcAbnormalChargAccountMapper {
    List<AbnormalChargeAcccountDto>  listPageAbnormalChargAccount(AbnormalChargeAcccountDto parm);

    List<Map<String,String >> personBuildingRelation(String houseCode);

    List<String> personCustList(@Param("custId") String custId, @Param("type") Integer type);

    int updateAcAccountByHouseCodeNew(AcAbnormalCharge parm);

    int updateAcAccountByHouseCodeNewForAccount(AcAbnormalCharge parm);

    int updateByHouseCodeAndAccountType( AcAbnormalCharge parm );

    int updateByHouseCodeAndAccountTypeForAccount( AcAbnormalCharge parm );


    List<AbnormalChargeAcccountDto> listPageAbnormalCurest(AbnormalChargeAcccountDto parm);
}