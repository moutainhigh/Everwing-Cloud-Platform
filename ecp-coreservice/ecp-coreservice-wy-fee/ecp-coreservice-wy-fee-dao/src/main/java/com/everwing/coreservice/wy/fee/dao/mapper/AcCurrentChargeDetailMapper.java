package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.fee.dto.OrderDetailInfoDto;
import com.everwing.coreservice.common.wy.fee.entity.AcCurrentChargeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AcCurrentChargeDetailMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcCurrentChargeDetail record);

    int insertSelective(AcCurrentChargeDetail record);

    AcCurrentChargeDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcCurrentChargeDetail record);

    int updateByPrimaryKey(AcCurrentChargeDetail record);
    
    AcCurrentChargeDetail selectNewestChargeDetail (@Param("houseCodeNew") String houseCodeNew , @Param("accountType") int accountType);
    
    AcCurrentChargeDetail selectNewestChargeDetailCommon (@Param("houseCodeNew") String houseCodeNew , @Param("accountType") int accountType);
    
    // @Param("houseCodeNew") String houseCodeNew,@Param("accountType") int accountType,@Param("list") List<String> monthInfo
    List<AcCurrentChargeDetail> getChargeDetailListByHouseCode(Map<String, Object> paraMap );
    
    List<AcCurrentChargeDetail> getChargeDetailListByHouseCodeForAudit(Map<String, Object> paraMap );
    
    Map<String, Object> getBillInfoByHouseCodeNew(String houseCodeNew);
    
    AcCurrentChargeDetail getAcCurrentChargeDetail(@Param("houseCodeNew") String houseCodeNew,@Param("accountType") int accountType );

    List<Map<String,Object>> listPageBySearchObj(OrderDetailInfoDto orderDetailInfoDto);

    List<AcCurrentChargeDetail> selectByOperaId(String operaId);

    int deleteBYOperaId(String id);

    List<AcCurrentChargeDetail> selectByOperaIdAndType(@Param("operaId") String operaId,@Param("accountType") int type);

    AcCurrentChargeDetail selectChargeDetail (@Param("houseCodeNew") String houseCodeNew , @Param("accountType") int accountType);

    int updateById(AcCurrentChargeDetail record);

    AcCurrentChargeDetail selectbyHouseCodeAndType(@Param("houseCodeNew") String houseCode, @Param("accountType") Integer accountType);
}