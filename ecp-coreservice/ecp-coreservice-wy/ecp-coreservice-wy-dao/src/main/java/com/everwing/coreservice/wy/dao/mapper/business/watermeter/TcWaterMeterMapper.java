package com.everwing.coreservice.wy.dao.mapper.business.watermeter;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcMeterBuilding;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TcWaterMeterMapper {
	
	public List<TcWaterMeter> listPageWaterMeterInfos(TcWaterMeter tcWatermeter);
	
	public int addWaerMeterInfo(TcWaterMeter tcWaterMeter);
	
	public int updateWaerMeterInfo(TcWaterMeter tcWaterMeter);
	
	public MessageMap replaceWaerMeterByOne(TcWaterMeter tcWaterMeter);
	
	public int deleteWaterMeterInfos(List<String> codes);
	
	public int startStopWaerMeterByOne(TcWaterMeter tcWaterMeter);
	
	public BaseDto<String, Object> loadEnclosureInfoByid(TcWaterMeter tcWaterMeter);
	
	public List<TcWaterMeter> listPageloadWaterMeterForChange(TcWaterMeter tcWaterMeter);
	
	int checkWaterMeterCode(String code);	
	
	//批量插入水表信息
	int batchInsert(List<TcWaterMeter> datas);
	//根据code查询水表信息
	TcWaterMeter selectTcWaterMeterByCode(TcWaterMeter tcWaterMeter);
	
	//根据传入的List<String> 的读表位置,获取该位置下所有的水表数据
	List<TcWaterMeter> findMetersByPositions(List<String> positions);
	
	//根据code集合查询水表信息
	List<TcWaterMeter> findMetersByCodes(List<String> codes);
	
	int bachUpdateForImport(List<TcWaterMeter> tcWaterMeterList);
	
	//执行更换水表时保证两个水表的关联收费对象一致
	int updateWaterMeterForReplace(String relationBuilding,String code);

	public List countMeters(TcWaterMeter entity);
	
	List<TcWaterMeter> listPageWaterMeterByLevel(TcWaterMeter tcWaterMeter);

	public TcWaterMeter getWaterMeterByCode(String meterCode, String projectId);
	
	public TcWaterMeter getWaterMeterByCodeAndM(String meterCode,String projectId);
	
	List<TcMeterBuilding> getBuildingAndMeter(TcWaterMeter tcWaterMeter);
	
	int checkWaterMeterByBuilding(String buildingCode);
	

	/**
	 * 根据收费对象编号、项目编号和状态是启用状态查询，这里没有考虑换表或者启停操作; 理论上是只能查询到一个，但是这里水表可能还没做表和收费对象是一对一的限制，故这里用List接受
	 */
	List<TcWaterMeter> findByRelarionId(String projectId,String relationId);

	
	/**
	 * 根据父表编号和项目编号查询
	 */
	List<String> findByparentCodeAndProjectId(String parentCode,String projectId);
	
	/**
	 * 根据父表编号和项目编号查找收费对象ID
	 */
	List<String> findRelationIdByparentCode(String parentCode,String projectId);
	
	/**
	 * @param projectId
	 * @param buildingCode
	 * @param historyType
	 * @return
	 */
	public Integer findMeterLevelBySomeParams(@Param("projectId") String projectId, @Param("buildingCode") String buildingCode, @Param("historyType")Integer historyType);
	
	/**
	 * 通过C级表关联起来查M级表,且一个C级表有且仅有一个对应的M级表
	 */
	TcWaterMeter findMByCCodeAndProjectId(String code,String projectId);
	
	/**
	 * 因为收费对象和表是一对一的关系，
	 * 根据项目编号，收费对象，
	 */
	TcWaterMeter findMByBuildCodeAndProjectId(String projectId,String buildCode);

	public String findAssetNoByTypeAndMeterCode(@Param("meterType") Integer meterType,@Param("meterCode") String meterCode, @Param("projectId") String projectId);

	public TcWaterMeter findByBuildingCode(String projectId, String buildCode);
	
	public List<TcWaterMeter> findsByBuildingCode(String buildingCode);

	List<TcWaterMeter> listPageWaterMeterByBuildingCode(TcWaterMeter tcWaterMeter);
}