
package com.everwing.coreservice.wy.dao.mapper.property;

import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingAllParent;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingList;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerSearch;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TcBuildingMapper
{
	/**
	 * 分页查询资产
	 * @param condition
	 * @return
	 */
    List<TcBuildingList> listPage(TcBuildingSearch condition) throws DataAccessException;

	/**
	 * 分页查询未关联水表资产
	 * @param condition
	 * @return
	 */
	List<TcBuildingList> listPageUnrelated(TcBuildingSearch condition) throws DataAccessException;
	/**
	 * 根据条件查询建筑树
	 * @param condition
	 * @return
	 * @throws DataAccessException
	 */
	List<TcBuildingList> findAllBuildingNodeByCondition(TcBuildingSearch condition) throws DataAccessException;


    /**
     * 获取指定条件的建筑信息
     * @param condition
     * @return
     */
    List<TcBuildingList> findByCondition(TcBuildingSearch condition) throws DataAccessException;

    /**
     * 批量添加建筑信息
     * @param list
     * @return
     */
    int batchInsert(List<TcBuilding> list) throws DataAccessException;

    /**
     * 批量删除建筑信息
     * @param list
     * @return
     */
    int batchDelete(List<String> list) throws DataAccessException;

    /**
     * 修改建筑
     * @param entity
     * @return
     */
    int modify(TcBuilding entity) throws DataAccessException;

    /**
     * 批量更新
     * @param list
     * @return
     */
    int batchModify(List<TcBuilding> list) throws DataAccessException;

	/**
	 * 修改建筑全名称
	 * @param buildingFullName
	 * @param buildingCode
	 * @return
	 */
    int renameBuildingFullName(String buildingFullName,String buildingCode) throws DataAccessException;



	/**
	 * 根据项目编码一次性加载树
	 * @param projectId
	 * @return
	 */
	List<TcBuilding> loadBuildingByProjectId(String projectId) throws DataAccessException;


    /**
     * 根据父编码找到所有子节点
     * @param pid
     * @return
     */
    List<TcBuilding> findBuildingByPid(String pid) throws DataAccessException;


	/**
	 * 汇总资产信息
	 * @param tcBuildingSearch
	 * @return
	 */
	List<Map<String,String>> collectAssetInfo(TcBuildingSearch tcBuildingSearch) throws DataAccessException;


	/**
	 * 单个删除
	 * @param id
	 * @return
	 */
	int deleteByID (String id) throws DataAccessException;






























	List<TcBuilding> loadBuildingByProjectIdWithoutTree(String projectId);
    
    /**
     * @description 三个参数只能传入其中一个
     */
    List<TcBuilding> loadBuildingByPickUpTree(@Param("projectId") String projectId,@Param("buildingCode") String buildingCode,@Param("custId")String custId);



	List<TcBuildingList> listPageBuildingInEntery(CustomerSearch customerSearch);


	/**
	 * 根据full_name集合查询建筑code信息
	 */
	List<TcBuilding> getTcBulidCodeList(Map<String, Object> paramMap);
	
	TcBuilding	getTcBulidByFullName(String projectId,String fullname);

	List<TcBuilding> findChargeBuildingByProjectCode(String projectId);
	
	List<TcBuilding> loadBuildingByProjectIdAndhousestore(String projectId);
	
	Double getSumHouseAreaByProject(TcBuilding tcBuilding);


	
	/**
	 * 根据项目编号统计为收费对象的建筑
	 * @param projectId
	 * @param isChargeObj
	 * @return
	 */
	Integer countIsChargeObjByProject(String projectId,String isChargeObj);
	































	/*****************************************************************分割线，请检查以下代码的必要性********************************/

	
	/**
	 * 根据项目编号查询过滤了已经被其他表占用了的收费对象建筑(电表)
	 */
	List<TcBuilding> listPageFilterRelationBuild(TcBuildingSearch	entity);

	/**
	 * 根据部分自定义条件查询建筑
	 * TODO 已经有findByCondition方法，为何再自己写一个？
	 * @param paramMap
	 * @return
	 */
	@Deprecated
	List<TcBuilding> findByParams(Map<String, Object> paramMap);

	/**
	 * 根据项目编号和buildCode查询
	 *  TODO 已经有findByCondition方法，为何再自己写一个？
	 */
	@Deprecated
	TcBuilding findByProjectIdAndBuildCode(String projectId,String buildCode);

	/**
	 * 根据house_code获取房屋资产数据
	 * TODO 已经有findByCondition方法，为何再自己写一个？
	 * @param houseCode
	 * @return
	 */
	@Deprecated
	List<TcBuildingList> findByCode(@Param("houseCode") String houseCode, @Param("buildingCode") String buildingCode);

	/**
	 *
	 * @param buildingCode
	 *  TODO 已经有findByCondition方法，为何再自己写一个？
	 * @return
	 */
	@Deprecated
	TcBuilding loadBuildingByBuildingCode(String buildingCode);

	List<TcBuilding> findHasBillsBuildings(@Param("projectId") String projectId,@Param("billingTime") Date billingTime,@Param("buildingCode") String buildingCode);

	/**
	 *得到账户欠费的资产信息
	 * @param projectId
	 * @return
	 */
	List<TcBuildingAllParent> fingListBuildingCodeAndArrears(String projectId);

	/**
	 * 第三方资产信息
	 * @param projectCode
	 * @return
	 */
	List<TcBuildingAllParent> fingListThirdBuildingCodeAndArrears(String projectCode);

	List<TcBuilding> findAllUnzipedBuildings(@Param("projectId")String projectId,@Param("billingTime") Date billingTime);
}

