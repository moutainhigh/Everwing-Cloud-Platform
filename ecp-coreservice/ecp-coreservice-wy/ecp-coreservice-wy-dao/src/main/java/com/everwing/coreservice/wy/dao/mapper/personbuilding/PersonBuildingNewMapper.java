/**
 * @Title: PersonBuildingNewMapper.java
 * @Package com.flf.mapper
 * @Description: TODO
 * Copyright: Copyright (c) 2011 
 * Company:武汉闻风多奇软件开发有限公司
 * 
 * @author wangtao
 * @date 2015-5-25 下午1:13:29
 * @version V1.0
 */

package com.everwing.coreservice.wy.dao.mapper.personbuilding;

import com.everwing.coreservice.common.wy.dto.BuildingAndCustDTO;
import com.everwing.coreservice.common.wy.entity.personbuilding.BindBuilding;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingDto;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingInfo;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingNew;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: PersonBuildingNewMapper
 * @Description: TODO
 * @author wangyong
 * @date 2015-5-25 下午1:13:29
 *
 */

public interface PersonBuildingNewMapper {
	
	/*
	 *  客户房屋关系表
	 */
    List<PersonBuildingNew> listPagePersonBuildingNew(PersonBuildingNew personBuildingNew) throws DataAccessException;//分页查询显示客户房屋关系信息 used
    
    PersonBuildingNew getPersonBuildingNewById(String personBuildingId) throws DataAccessException;//根据id查询客户房屋关系信息 used
    
    int insertPersonBuildingNew(PersonBuildingNew personBuildingNew) throws DataAccessException; //添加客户房屋关系信息 used
    
    void deletePersonBuilding(String personBuildingId) throws DataAccessException; //删除客户房屋关系信息、 used
    
    List<PersonBuildingNew> listAllPersonAndHouseByHouseId(String houseId) throws DataAccessException;//根据房屋id查询人员建筑关系 used
    
    List<PersonBuildingNew> listAllPersonAndHouseByStoreId(String storeId) throws DataAccessException;//根据商铺id查询人员建筑关系
    
    List<PersonBuildingNew> listAllPersonAndHouseByStallId(String stallId) throws DataAccessException;//根据车位id查询人员建筑关系
   	
    List<PersonBuildingNew> listAll() throws DataAccessException;//查询所有的 used
    
    int updatePersonBuildingNewByState(String buildingStructureId) throws DataAccessException; //修改建筑结构id为此的客户房屋关系状态
    
    List<PersonBuildingNew> listAllPersonBuildingNewone(PersonBuildingNew personBuildingNew) throws DataAccessException; //查询客户房屋关系信息
    
    List<PersonBuildingNew> listPersonBuildingNewByBuildingStructureId(String buildingStructureId) throws DataAccessException;//根据建筑结构id查询人员建筑关系
    
    List<PersonBuildingNew> listPersonBuildingNewByBuildingStructureIdone(String buildingStructureId) throws DataAccessException;//根据建筑结构id查询人员建筑关系并获得人员信息 used
    
    List<PersonBuildingNew> listPersonBuildingNewByCustType(String custType) throws DataAccessException;//根据客户类型查询人员建筑关系
    
    List<PersonBuildingNew> listPagePersonBuilding(PersonBuildingNew personBuildingNew) throws DataAccessException;//分页查询显示客户房屋关系信息 used
    
    PersonBuildingNew getPersonBuildingNewByCustIdAndBsId(String custId,String buildingStructureId) throws DataAccessException;//根据客户id和建筑id查询客户房屋关系信息
    
    PersonBuildingNew getPersonBuildingNewByEnterpriseIdAndBsId(String enterpriseId,String buildingStructureId) throws DataAccessException;//根据企业客户id和建筑id查询客户房屋关系信息
    
    PersonBuildingNew selectHouseNew(String buildingStructureid) throws DataAccessException;
    
	List<PersonBuildingNew> getRelationOfemplers(PersonBuildingNew personBuildingNew) throws DataAccessException;//根据人屋关系查询员工与企业房屋的关系链 used
	
	List<PersonBuildingNew> selectPersonBuildingNewByCustId(PersonBuildingNew personBuildingNew) throws DataAccessException; //used
	
	List<PersonBuildingNew> listPersonBuildingByHouseId(String houseId) throws DataAccessException;
    
    List<PersonBuildingNew> listPersonBuildingNewByCustId(String custId) throws DataAccessException;//根据客户id查询客户房屋关系
    
    List<PersonBuildingNew> listPersonBuildingNewByCustIdAndType(String custId, String Type) throws DataAccessException;//根据客户id查询客户房屋关系
    
    PersonBuildingNew selectHouseNewByCustType(PersonBuildingNew personBuildingNew) throws DataAccessException;//根据关系id和客户类型查询关系
    
    int updataPersonBuildingById(PersonBuildingNew personBuildingNew) throws DataAccessException;// 根据关系id修改，删除（关系启用/停止）关系链信息 used
    
    List<PersonBuildingNew> getPersonBuildingNewByCustIdAndBsId_one(String custId,String buildingStructureId) throws DataAccessException;//根据客户id和建筑id查询客户房屋关系信息 used
	
    List<PersonBuildingNew> getRelationBycustId(String custId) throws DataAccessException;	//used
    
    List<TcBuilding> listBudildingByCustId(String custId) throws DataAccessException;
	
    List<PersonBuildingNew> listPageHouseByenterpriseId(PersonBuildingNew personBuildingNew) throws DataAccessException;//根据企业客户id查询所有其相关建筑信息 used
	
	List<PersonBuildingNew> getAllPersonBuildings(String buildingStructureId) throws DataAccessException;//根据企业客户id查询所有其相关建筑信息
	
	List<PersonBuildingNew> getPersonBuildingNewsByCompany(String company) throws DataAccessException;//根据公司id获取其项目相关建筑信息
    
	List<PersonBuildingNew> getPersonBuildingByProjectId(String projectId) throws DataAccessException;//根据项目id获取项目下的客户建筑关联关系  王洲  2016.2.1
	
	List<PersonBuildingNew> getPersonBuildingByCustId(String custId) throws DataAccessException;//根据客户id获取客户建筑关联关系
	
	List<String> gteBuildingByPersonId(String personId) throws DataAccessException;
	
//	HouseNew getHouseNewByHouseId(String houseId) throws DataAccessException;//根据房屋id获取房屋信息
	
//	Sipmanage getSipByStuctureId(String buildingStructureId) throws DataAccessException;//根据建筑结构id获取sip
	
	List<String> getPersonIdByBuildingStruId(String buildingStruId) throws DataAccessException;//根据建筑结构id获取客户id
	
	List<String> getPersonIdById(String custId) throws DataAccessException;//根据客户id获取建筑结构id
	//批量插入资产绑定记录
	int insertList(List<PersonBuildingNew> insertList) throws DataAccessException;	//used
	
	List<PersonBuildingNew> getPersonBuildingHasMoreByProjectId(String projectId) throws DataAccessException;  //used
	
	int deletePersonBuildingByCustId(String custId,String buildingStructureId) throws DataAccessException;//根据租客id删除租客关系
	
	int deleteByCustId(String custId) throws DataAccessException;	//删除资产绑定关系
	
	/**
	 * 支付系统对接接口
	 * 根据个人客户id查询关联的所有建筑，包括为业主和为租客
	 * 王洲
	 * 2016.04.26
	 * @param custId
	 * @return
	 */
	List<PersonBuildingNew> listBuildingByCustId(String custId) throws DataAccessException;
	
	/**
	 * 支付系统对接接口
	 * @param cardNum
	 * @return
	 */
	List<PersonBuildingNew> listBuildingByCardNum(String cardNum) throws DataAccessException;
	
	/**
	 * 获取室内机sip
	 * @param buildingStructureId
	 * @return
	 */
//	Sipmanage getIndoorSipByStuctureId(String buildingStructureId) throws DataAccessException;
	
	/**
	 * 根据客户id集合获取客户建筑关联关系
	 * @param custIdList
	 * @return
	 */
	List<PersonBuildingNew> getPersonBuildingByCustIdList(List<String> custIdList) throws DataAccessException;
	
	List<String> gteBuildingByPersonIdList(List<String> custIdList) throws DataAccessException;
	
	/**
	 * 根据personBuidingId获取绑定的用户id
	 * @param personBuildingNew
	 * @return
	 */
	List<PersonBuildingNew> getPersonBuildingList(PersonBuildingNew personBuildingNew) throws DataAccessException;
	
	/**
	 * 根据BuildingId获取关联关系,以及对应的个人/企业用户集合
	 * @param buildingId
	 * @return
	 */
	List<PersonBuildingNew> getInfosByBuildingId(String buildingId) throws DataAccessException;	//used
	
	/**
	 * 判断该绑定关系是否已经存在
	 * @param pb
	 * @return
	 */
	List<PersonBuildingNew> isExists(PersonBuildingNew pb) throws DataAccessException;	//used
	
	void deleteBatch(Map<String,Object> paramMap) throws DataAccessException;	//used
	
	//获取未绑定的建筑信息集合
	List<BindBuilding> getNotBindBuildingInfos(Map<String, Object> paramMap);
	//获取已绑定的建筑信息集合
	List<BindBuilding> getBindedBuildingInfos(Map<String, Object> paramMap);

	List<PersonBuildingInfo> loadBuildingInfos(String projectId);

	List<String> getBuildingCodesByCustIdAndChargerId(@Param("custId") String custId, @Param("chargerId") String chargerId);
	
	// 查询客户是否有资产
	Integer getBuildingCountByCustomerId(String custId);

	List<PersonBuildingNew> findByCondition(PersonBuildingNew personBuildingNew);

	int selectByCustomer(@Param("custId") String custId,@Param("enterpriseId") String enterpriseId,@Param("buildingId") String buildingId);

	List<Map> findGreenLightItemParametersByBuildingId(String buildingId);

	List<PersonBuildingNew> getBuildingDataByCustPhone(String phone);

	List<Map> getOwerDataByBuildingId(String buildingId);

	List<BuildingAndCustDTO> queryByMobile(@Param("mobile") String mobile);

    String getCustId(@Param("buildingCode") String buildingCode,@Param("name") String name);

	List<PersonBuildingDto> fingListPersonBuildingDto(String projectId);
}
