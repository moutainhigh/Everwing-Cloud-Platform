package com.everwing.coreservice.wy.api.personbuilding;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.dto.BuildingAndCustDTO;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.personbuilding.BindBuilding;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingNew;
import com.everwing.coreservice.common.wy.service.personbuilding.PersonbuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PersonbuildingApi {

	@Autowired
	private PersonbuildingService personbuildingService;//控制层调用api接口
	

	public RemoteModelResult<MessageMap> addPersonBuildingNewRestful(String companyId,PersonBuildingNew personBuildingNew){
		return new RemoteModelResult<MessageMap>(this.personbuildingService.addPersonBuildingNewRestful(companyId,personBuildingNew));
	}
	
	public RemoteModelResult<BaseDto> listBudildingByCustId(String custId){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.listBudildingByCustId(CommonUtils.getCompanyIdByCurrRequest(),custId));
	}
	
	public RemoteModelResult<BaseDto> listAllPersonAndHouseByHouseId(String companyId,String houseId){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.listAllPersonAndHouseByHouseId(companyId,houseId));
	}
	
	public RemoteModelResult<BaseDto> listAll(String companyId){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.listAll(companyId));
	}
	
	public RemoteModelResult<BaseDto> listPagePersonBuildingNew(String companyId,PersonBuildingNew personBuildingNew){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.listPagePersonBuildingNew(companyId,personBuildingNew));
	}
	
	
	public RemoteModelResult<BaseDto> listPersonBuildingNewone(String companyId , String buildingStructureId){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.listPersonBuildingNewone(companyId,buildingStructureId));
	}
	
	public RemoteModelResult<MessageMap> addPersonBuildingEnterpriseNewRestful(String companyId,PersonBuildingNew personBuildingNew){
		return new RemoteModelResult<MessageMap>(this.personbuildingService.addPersonBuildingEnterpriseNewRestful(companyId,personBuildingNew));
	}
	
	public RemoteModelResult<BaseDto> getRelationOfemplers(String companyId , PersonBuildingNew personBuildingNew){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.getRelationOfemplers(companyId,personBuildingNew));
	}
	
	public RemoteModelResult<BaseDto> selectPersonBuildingNewByCustId(String companyId , PersonBuildingNew personBuildingNew){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.selectPersonBuildingNewByCustId(companyId,personBuildingNew));
	}
	
	public RemoteModelResult<BaseDto> getRelationOfemplersByCustId(String companyId ,String custId){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.getRelationOfemplersByCustId(companyId,custId));
	}
	
	
	public RemoteModelResult<MessageMap> updataPersonBuildingById(String companyId,PersonBuildingNew personBuildingNew){
		return new RemoteModelResult<MessageMap>(this.personbuildingService.updataPersonBuildingById(companyId,personBuildingNew));
	}
	
	
	public RemoteModelResult<MessageMap> deletePersonBuildingById(String companyId,List<PersonBuildingNew> personBuildingNews){
		return new RemoteModelResult<MessageMap>(this.personbuildingService.deletePersonBuildingById(companyId,personBuildingNews));
	}
	
	
	public RemoteModelResult<BaseDto> getRelationBycustId(String companyId ,String custId){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.getRelationBycustId(companyId,custId));
	}
	
	public RemoteModelResult<BaseDto> listPageHouseByenterpriseId(String companyId ,PersonBuildingNew personBuildingNew){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.listPageHouseByenterpriseId(companyId,personBuildingNew));
	}
    
	public RemoteModelResult<MessageMap> deletePersonBuilding(String companyId,PersonBuildingNew personBuildingNew){
		return new RemoteModelResult<MessageMap>(this.personbuildingService.deletePersonBuilding(companyId,personBuildingNew));
	}
	
  	public RemoteModelResult<MessageMap> importFile(String companyId , Annex annex){
		return new RemoteModelResult<MessageMap>(this.personbuildingService.importFile(companyId,annex));
    }

  	public RemoteModelResult<MessageMap> inportOtherExcel(String companyId , Annex annex){
		return new RemoteModelResult<MessageMap>(this.personbuildingService.inportOtherExcel(companyId,annex));
    }
	
	public RemoteModelResult<BaseDto> getInfosByBuildingId(WyBusinessContext ctx, String buildingId){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.getInfosByBuildingId(ctx.getCompanyId(),buildingId));
	}
	

	
	
	/*********************************************   资产绑定与解绑 start   *************************************************/
	
	public RemoteModelResult<MessageMap> assetBinding(WyBusinessContext ctx,List<PersonBuildingNew> personBuildingNew){
		return new RemoteModelResult<MessageMap>(this.personbuildingService.assetBinding(ctx,personBuildingNew));
	}
	
	
	public RemoteModelResult<MessageMap> relieveAssetBinding(String companyId,List<PersonBuildingNew> personBuildingNew){
		return new RemoteModelResult<MessageMap>(this.personbuildingService.relieveAssetBinding(companyId,personBuildingNew));
	}
	
	
	/*********************************************   资产绑定与解绑 end   *************************************************/
	
	
	/*********************************************   可视对讲   *************************************************/
	
	
	public RemoteModelResult<BaseDto> gteBuildingByPersonId(String companyId,String personId){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.gteBuildingByPersonId(companyId,personId));
	}
	
	
	public RemoteModelResult<BaseDto> getHouseNewByHouseId(String companyId,String houseId){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.getHouseNewByHouseId(companyId,houseId));
	}
	
	public RemoteModelResult<BaseDto> getSipByStuctureId(String companyId,String buildingStructureId){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.getSipByStuctureId(companyId,buildingStructureId));
	}
	
	
	public RemoteModelResult<BaseDto> getPersonIdByBuildingStru(String companyId,String buildingStruId){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.getPersonIdByBuildingStru(companyId,buildingStruId));
	}
	
	
	public RemoteModelResult<BaseDto> getPersonBuildingByCustId(String companyId,String custId){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.getPersonBuildingByCustId(companyId,custId));
	}
	
	public int getPersonBuildingCountByCustId(String companyId,String custId){
		return this.personbuildingService.getPersonBuildingCountByCustId(companyId,custId);
	}
	
	
	public String getSipsByStuctureId(String companyId,String buildingStructureId){
		return this.personbuildingService.getSipsByStuctureId(companyId,buildingStructureId);
	}
	
	
	public String getPersonBuildingByCustIdList(String companyId,String ids){
		return this.personbuildingService.getPersonBuildingByCustIdList(companyId,ids);
	}
	
	
	public String gteBuildingByPersonIdList(String companyId,String ids){
		return this.personbuildingService.gteBuildingByPersonIdList(companyId,ids);
	}
	
	public int getPersonBuildingCountByCustIdList(String companyId,String ids){
		return this.personbuildingService.getPersonBuildingCountByCustIdList(companyId,ids);
	}
	
	public MessageMap exportBuildingInfos(String companyId,String isBindingAssets,BindBuilding bindBuilding){
		return this.personbuildingService.exportBuildingInfos(companyId,isBindingAssets,bindBuilding);
	}

	public RemoteModelResult<BaseDto> loadBuildingInfos(String companyId,String projectId) {
		return new RemoteModelResult<BaseDto>(this.personbuildingService.loadBuildingInfos(companyId,projectId));
	}

	public RemoteModelResult<MessageMap> importFile(WyBusinessContext ctx, String batchNo, String excelPath, String projectId) {
		return new RemoteModelResult<MessageMap>(this.personbuildingService.importFile(ctx, batchNo,excelPath,projectId));  
	}

	public RemoteModelResult<Integer> getBuildingCountByCustomerId(WyBusinessContext ctx, String custId) {
		return new RemoteModelResult<Integer>(this.personbuildingService.getBuildingCountByCustomerId(ctx,custId));
	}

	public RemoteModelResult<List<PersonBuildingNew>> findByCondition(WyBusinessContext ctx, PersonBuildingNew personBuildingNew){
		return new RemoteModelResult<List<PersonBuildingNew>>(this.personbuildingService.findByCondition(ctx,personBuildingNew));
	}

	public RemoteModelResult<List<Map>> findGreenLightItemParametersByBuildingId(WyBusinessContext ctx,String buildingId){
		List<Map> maps = this.personbuildingService.findGreenLightItemParametersByBuildingId(ctx,buildingId);
		return new RemoteModelResult<>(maps);
	}

	public RemoteModelResult<BaseDto> getBuildingDataByCustPhone(String companyId,String phone){
		return new RemoteModelResult<BaseDto>(this.personbuildingService.getBuildingDataByCustPhone(companyId,phone));
	}

	public RemoteModelResult<List<BuildingAndCustDTO>> getBuindingAndCustByMobile(String companyId, String mobile) {
		List<BuildingAndCustDTO> list = personbuildingService.getBuindingAndCustByMobile(companyId,mobile);
		return new RemoteModelResult<>(list);
	}

}
