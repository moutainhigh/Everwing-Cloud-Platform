package com.everwing.coreservice.common.wy.service.personbuilding;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.dto.BuildingAndCustDTO;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.personbuilding.BindBuilding;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingImportBean;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingNew;

import java.util.List;
import java.util.Map;

public interface PersonbuildingService {


	public MessageMap addPersonBuildingNewRestful(String companyId,PersonBuildingNew personBuildingNew);
	
	public BaseDto listAllPersonAndHouseByHouseId( String companyId, String houseId);
	
	public BaseDto listAll( String companyId);
	
	public BaseDto listPagePersonBuildingNew( String companyId, PersonBuildingNew personBuildingNew);
	
	public BaseDto listPersonBuildingNewone( String companyId ,  String buildingStructureId);
	
	public MessageMap addPersonBuildingEnterpriseNewRestful( String companyId, PersonBuildingNew personBuildingNew);
	
	public BaseDto getRelationOfemplers( String companyId ,  PersonBuildingNew personBuildingNew);
	
	public BaseDto selectPersonBuildingNewByCustId( String companyId ,  PersonBuildingNew personBuildingNew);
	
	public BaseDto getRelationOfemplersByCustId( String companyId , String custId);
	
	public MessageMap updataPersonBuildingById( String companyId, PersonBuildingNew personBuildingNew);
	
	public MessageMap deletePersonBuildingById( String companyId, List<PersonBuildingNew> personBuildingNew);
	
	public BaseDto getRelationBycustId( String companyId , String custId);
	
	public BaseDto listPageHouseByenterpriseId( String companyId , PersonBuildingNew personBuildingNew);
	
	public MessageMap deletePersonBuilding( String companyId, PersonBuildingNew personBuildingNew);
  	
	public MessageMap importFile( String companyId ,  Annex annex);
  	
	public MessageMap inportOtherExcel( String companyId ,  Annex annex);
	
	public BaseDto getInfosByBuildingId( String companyId, String buildingId);

	
	
	/*********************************************   资产绑定与解绑 start   *************************************************/
	
	
	
	
	public MessageMap assetBinding( WyBusinessContext ctx, List<PersonBuildingNew> personBuildingNew);
	
	public MessageMap relieveAssetBinding( String companyId, List<PersonBuildingNew> personBuildingNew);
	
	public BaseDto gteBuildingByPersonId( String companyId, String personId);
	
	public BaseDto getHouseNewByHouseId( String companyId, String houseId);
	
	public BaseDto getSipByStuctureId( String companyId, String buildingStructureId);
	
	public BaseDto getPersonIdByBuildingStru( String companyId, String buildingStruId);
	
	public BaseDto getPersonBuildingByCustId( String companyId, String custId);
	
	public int getPersonBuildingCountByCustId( String companyId, String custId);
	
	public String getSipsByStuctureId( String companyId, String buildingStructureId);
	
	public String getPersonBuildingByCustIdList( String companyId, String ids);
	
	public String gteBuildingByPersonIdList( String companyId, String ids);
	
	public int getPersonBuildingCountByCustIdList( String companyId, String ids);
	
	public MessageMap exportBuildingInfos(String companyId,String isBindingAssets,BindBuilding bindBuilding);

	public BaseDto listBudildingByCustId(String companyId,String custId);

	public BaseDto loadBuildingInfos(String companyId,String projectId);

	public MessageMap importFile(WyBusinessContext ctx, String batchNo, String excelPath, String projectId);



	// 查询客户是否有资产
	Integer getBuildingCountByCustomerId(WyBusinessContext ctx,String custId);

	List<PersonBuildingNew> findByCondition(WyBusinessContext ctx,PersonBuildingNew personBuildingNew);

	public MessageMap importData(WyBusinessContext ctx, String batchNo,List<PersonBuildingImportBean> pbImportBeans, String oprUserId);

	List<Map> findGreenLightItemParametersByBuildingId(WyBusinessContext ctx,String buildingId);

	public BaseDto getBuildingDataByCustPhone(String companyId,String phone);

	public List<BuildingAndCustDTO> getBuindingAndCustByMobile(String companyId,String mobile);


}

