package com.everwing.coreservice.common.wy.service.business.electmeter;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ChangeElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeterSearch;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;

import java.util.List;

public interface TcElectMeterService {
	
	/**
	 * 新增电表
	 * @param electMeter
	 * @return
	 * @throws Exception
	 */
	BaseDto addelectmeter(String companyId,ElectMeter electMeter);
	
	/**
	 * 查询
	 * @param companyId
	 * @param electMeterSearch
	 * @return
	 */
	BaseDto listPageElectmeterByCompany(String companyId,ElectMeterSearch electMeterSearch);
	
	/**
	 * 启停项目 该方法只是修改状态
	 * @param companyId
	 * @param electMeter
	 * @return
	 */
	BaseDto startOrstopElectMeter(String companyId,ElectMeter electMeter);
	
	/**
	 * 修改保存
	 * @param companyId
	 * @param electMeter
	 * @return
	 */
	BaseDto editSave(String companyId,ElectMeter electMeter);
	
	/**
	 * 删除
	 * @param companyId
	 * @param electMeter
	 * @return
	 */
	BaseDto delElect(String companyId,ElectMeter electMeter);
	
	/**
	 * 变更
	 */
	BaseDto changeElectMeter(String companyId,ChangeElectMeter changeElectMeter);
	
	/**
	 * 启用/停用
	 * @param companyId
	 * @param electMeter
	 * @return
	 */
	BaseDto startOrStop(String companyId,ElectMeter electMeter);

	
	/**
	 * 统计电表
	 * @param companyId
	 * @param electMeter
	 * @return
	 */
	public BaseDto countMeters(String companyId,ElectMeter electMeter);
	
	/**
	 * 查询已经被其他电表占用了的收费对象建筑
	 * @param ctx
	 * @param projectId
	 * @return
	 */
	BaseDto listPageFilterRelationBuild(WyBusinessContext ctx,TcBuildingSearch entity);

	List<ElectMeter> findsByBuildingCode(String companyId, String buildingCode);

    BaseDto listPageElectMeterByBuildingCode(WyBusinessContext ctx, ElectMeterSearch electMeterSearch);
}
