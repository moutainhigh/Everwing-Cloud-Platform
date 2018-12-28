package com.everwing.coreservice.wy.dao.mapper.business.electmeter;

import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeterImport;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeterSearch;

import java.util.List;
import java.util.Map;

public interface TcElectMeterMapper {
	/**
	 * 新增
	 * @param electMeter
	 * @return
	 */
	int addelectmeter(ElectMeter electMeter);
	
	/**
	 * 查询
	 * @param companyId
	 * @param electMeterSearch
	 * @return
	 */
	List<ElectMeter> listPageElectmeterByCompany(
			ElectMeterSearch electMeterSearch);
	
	/**
	 * 启停电表
	 * @param map
	 * @return
	 */
	int startOrstopElectMeter(Map<String,Object> map);
	
	/**
	 * 修改保存
	 * @param electMeter
	 * @return
	 */
	int editSave(ElectMeter electMeter);
	
	/**
	 * 根据电表编码查询
	 * @param code
	 * @return
	 */
	ElectMeter getElectMeterByCode(String code,String projectId);
	
	
	/**
	 * 根据电表编号和项目编号查询M等级表
	 */
	ElectMeter getElectMeterByCodeAndM(String code,String projectId);
	/**
	 * 删除
	 */
	int delElect(Map map);
	
	/**
	 * 批量新增
	 */
	int batchadd(List<ElectMeterImport> list);
	
	/**
	 * 批量修改
	 */
	int batchupdate(List<ElectMeterImport> list);
	
	/**
	 * 根据电表编号查询多个
	 */
	List<String> getElectByCodes(List<String> codes);
	
	/**
	 * 根据位置查询
	 */
	List<ElectMeter> findMetersByPositions(List<String> positions);
	
	/**
	 * 统计电表
	 * @param entity
	 * @return
	 */
	public List countMeters(ElectMeter entity);
	
	/**
	 * 通过项目编号和收费对象编号查询
	 * @param projectId
	 * @param reationId
	 * @return
	 */
	ElectMeter getElectMeterByReationId(String projectId,String reationId);
	
	
	List<String> findByparentCodeAndProjectId(String parentCode,String projectId);
	
	/**
	 * 根据父表编号和项目编号查找收费对象ID
	 */
	List<String> findRelationIdByparentCode(String parentCode,String projectId);
	
	/**
	 * 通过C级表关联起来查M级表,且一个C级表有且仅有一个对应的M级表
	 */
	ElectMeter findMByCCodeAndProjectId(String code,String projectId);
	
	/**
	 * 因为收费对象和表是一对一的关系，
	 * 根据项目编号，收费对象，
	 */
	ElectMeter findMByBuildCodeAndProjectId(String projectId,String buildCode);
	
	ElectMeter findsMByBuildCodeAndProjectId(String buildingCode);

	List<ElectMeter> findsByBuildingCode(String buildingCode);

    List<ElectMeter> listPageElectMeterByBuildingCode(ElectMeterSearch electMeterSearch);
}
