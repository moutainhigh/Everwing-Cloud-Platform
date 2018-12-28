package com.everwing.coreservice.wy.dao.mapper.gating;

import com.everwing.coreservice.common.wy.dto.GatingDTO;
import com.everwing.coreservice.common.wy.dto.GatingLogStatisticsDTO;
import com.everwing.coreservice.common.wy.dto.GatingUserDto;
import com.everwing.coreservice.common.wy.dto.ProjectGatingDTO;
import com.everwing.coreservice.common.wy.entity.gating.Gating;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface GatingMapper {
	

	List<Gating> listPageGating(Gating gating) throws DataAccessException;
	Gating getGatingByid(int id) throws DataAccessException;
	Gating getGatingByEquipmentId(String equipmentId) throws DataAccessException;//根据设备id查询门控信息
	int insertGating(Gating gating) throws DataAccessException;
	int updateGating(Gating gating) throws DataAccessException;
	int deleteGating(String id) throws DataAccessException;
	List<Gating> listPagegetDevices(Gating gating) throws DataAccessException;
	List<Gating> listAllGating() throws DataAccessException;

	int getGoodsNumber(String facilityState) throws DataAccessException;

	int MAXID() throws DataAccessException;
	String updateState(String gating) throws DataAccessException;
	int gatingAccountBinding(String gatingId, String accountId) throws DataAccessException;
	
	String getGatingDistrict(String gateAccount) throws DataAccessException;
	
	int isGatingByequipmentId(String equipmentId) throws DataAccessException;
	
	//判断二维码信息是否正确
	Gating fingGatingByGateByTwoDimensionCode(String twoDimensionCode) throws DataAccessException;
	
     
	int updateStateByOpenState(String gateAccount ) throws DataAccessException;
	
	int updateOpenGatingStateBytwoDimensionCode(Map map) throws DataAccessException;
	
	Integer isWallGating(String equipmentNum) throws DataAccessException;
	
	//后台根据id获取门控机信息
	Gating backgroundGetGatingByid(String id) throws DataAccessException;
	//后台根据关键字获取门控机信息
	Gating getGatingByKey(String string) throws DataAccessException;
	//后台获取已绑定账号门控机信息
	List<Gating> getLoginGating() throws DataAccessException;
	
	List<Gating> listAllGatingByKey(Gating gating) throws DataAccessException;//根据搜索关键字获取门控机列表信息
	
	/**
	 * 查询未绑定账号的设备
	 * @return
	 */
	List<Gating> selectGating(String companyId) throws DataAccessException;
	
	/**
	 * 设备绑定账号
	 * @param temp
	 */
	int updateGatingAccount(Gating temp) throws DataAccessException;
	
	/**
	 * 修改门控机当前状态为未使用
	 * @param idList
	 */
	void updateGatingFacilityState(List<String> idList) throws DataAccessException;
	
	/**
	 * 通过id修改门控使用状态为运行中
	 * @param id
	 */
	void updateGaetById(String id) throws DataAccessException;
	
	/**
	 * 通过id修改门控使用状态为未使用
	 * @param id
	 */
	void updateGaetNotUserById(String id) throws DataAccessException;
	
	/**
	 * 根据条件及门控机状态查询门控机总数
	 * @param gating
	 * @return
	 */
	int selectGatingState(Gating gating);
	
	/**
	 * 移除账号与设备的绑定关系
	 * @param id
	 */
	void updateGateNotBinding(String id) throws DataAccessException;
	
	List<Gating> selectGatingPageList(Map<String, Object> map) throws DataAccessException;
	
	List<String> getHouseAccount(Map<String,Object> map) throws DataAccessException;

	List<Map<String,String>> selectBuildingStruct(String gatingId);

	Map<String,String> selectBuildingGating(String buildingCode);

	Map<String,String> selectLoginGating(String accountName);

	int updateStatus(Gating gating);

    List<ProjectGatingDTO> queryProjectGating();

    List<GatingDTO> queryByProjectId(@Param("projectId") String projectId);

    List<GatingLogStatisticsDTO> queryLogStatistics(@Param("mkAccountName") String mkAccountName);

	List<Gating> selectByCondition(GatingUserDto gatingUserDto);

	Integer countSelectByCondition(GatingUserDto gatingUserDto);

//	@Select("call pro_query_gating_struct(#{0},#{1})")
//	@Options(statementType = StatementType.CALLABLE)
	List<Map<String,String>> getGatingStruct(@Param("companyId")String companyId, @Param("projectId")String projectId);

//	@Select("select hu.Id buildingsId , hu.building_full_name buildingFullName  from " +
//			"(select ceng.* from tc_building dang " +
//			"LEFT JOIN tc_building ceng on dang.building_code = ceng.pid " +
//			"where dang.id = #{apartmentId} and dang.company_id = #{companyId} and dang.project_id = #{projectId})" +
//			"ceng LEFT JOIN tc_building hu on ceng.building_code = hu.pid")
	List<Map<String,String>> getBuildingsByApartmentId(@Param("companyId")String companyId, @Param("projectId")String projectId, @Param("apartmentId")String apartmentId);

    List<Map<String,String>> getGatingDataByBuildingId(String buildingId);

    void deleteWhiteList(String userId);
}
