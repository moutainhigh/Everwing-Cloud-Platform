package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.dto.BuildingAndCustDTO;
import com.everwing.coreservice.common.wy.fee.dto.*;
import com.everwing.coreservice.common.wy.fee.entity.AcAccount;
import com.everwing.coreservice.common.wy.fee.entity.FinaceAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AcAccountMapper {
    int deleteByPrimaryKey(String id);

    int insert(AcAccount record);

    int insertSelective(AcAccount record);

    AcAccount selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AcAccount record);

    int updateByPrimaryKey(AcAccount record);
    
    int updateByPrimaryKeyForAmount(AcAccount record);
    
    int updateCurrentBillByHouseCode(@Param("houseCode") String houseCode);

    AcAccount selectByHouseCodeNew(String houseCodeNew);

    List<BuildingInfoDto> listPageBuildingInfo(BuildingInfoDto buildingInfoDto);

    Map<String,Object> selectAccountInfoByBuildingCode(String buildingCode);

    List<Map<String,Object>> listPagePrestoreDetail(PreStoreInfoDto preStoreInfoDto);

    List<BuildingInfoDto> listPageBuildingInfoByCustId(BuildingInfoDto buildingInfoDto);

    List<BuildingInfoDto> listPageBuildingFinace( FinaceAccount finaceAccount);

    List<Map> queryArrearsByHouseCodeNews(@Param("houseCodeNews") List<String> houseCodeNews,@Param("projectId")String projectId);

    List<Map> queryArrearsByBuildingName(@Param("buildingName") String buildingName,@Param("projectId")String projectId);

    Map queryArrearsByhouseCode(@Param("houseCode")String houseCode, @Param("projectId")String projectId);

    List<PayDetailDto> queryCostByHouseCodeNew(@Param("houseCodeNew") String houseCodeNew);

    List<BuildingAndCustDTO> queryByMobile(@Param("mobile") String mobile,@Param("projectId")String projectId);

    BuildingAndCustInfoDto queryBuildingDetailsByhouseCode(@Param("projectId")String projectId, @Param("houseCode")String houseCode);

    List<Map<String,String>> queryCustByBuildId(@Param("projectId")String projectId,@Param("buildId") String buildId);

    List<BillOfYearDto> queryBillByhouseCodeAndYear (@Param("projectId")String projectId,@Param("year")String year,@Param("houseCode")String houseCode);

    int updateAcAccountByHouseCodeNew(AcAccount acAccount);

    int updateByLastArreasAmount(@Param("amount") double v, @Param("houseCodeNew") String houseCode);




    // AccountAbnormalDto findAccount(AbnormalDto abnormalDto);
}