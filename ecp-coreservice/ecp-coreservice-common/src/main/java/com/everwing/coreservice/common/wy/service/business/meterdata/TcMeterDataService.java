package com.everwing.coreservice.common.wy.service.business.meterdata;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;

import java.util.List;
import java.util.Map;

public interface TcMeterDataService {

	BaseDto listPageDatas(String companyId, TcMeterData tcMeterData);

	BaseDto batchModify(String companyId, List<TcMeterData> datas,String operationType);

	BaseDto listPageDatasToAudit(String companyId, TcMeterData data);

	BaseDto batchAudit(String companyId, List<TcMeterData> datas,Integer auditStatus);

	BaseDto listPageReadingRecords(String companyId, TcMeterData tcMeterData);

	BaseDto modifyReading(String companyId, TcMeterData data);
	
	BaseDto modifyReadingNew(String companyId, TcMeterData data);
	
	BaseDto listPageHistories(WyBusinessContext ctx,TcMeterData tcMeterData);

	BaseDto findByBuildingCode(String companyId, String code);
	
	BaseDto selectAbnormalData(String companyId,TcMeterData meterData);
	
	BaseDto getReadingInfoByYear(String companyId,Map<String, String> paramMap);
	
	//处理所有业务的，因为需要分页，故单独一个方法
	BaseDto listPageReadingInfoByYear(String companyId,TcMeterData tcMeterData,int serviceType);
	//根据taskId以及buildingCode获取抄表数据以及对应的buildingFullName
	BaseDto findByTaskIdAndBuildingCode(String companyId, String code,String taskId);
	
	/**
	 * 电表异常抄表查询
	 * @param companyId
	 * @param meterData
	 * @return
	 */
	public BaseDto selectAbnormalElectData(String companyId,TcMeterData meterData);



	List<TcMeterData> queryByTaskIds(String companyId,List<String> taskIds,int meterType);

    List<TcMeterData> queryByTaskId(String companyId,String taskId,int meterType);

    List<TcMeterData> queryByDescription(String companyId,String description,int meterType);

    List<TcMeterData> queryYearData(String companyId,String meterCode,int meterType,String year);

    int modifyWMeterData(String companyId ,String taskId,String accountId,String meterCode,String totalReading,String fileId);

    int modifyEMeterData(String companyId,String taskId,String accountId,String meterCode,String totalReading,
                         String peakrReading,String vallyReading,String commonReading, String fileId);

	void push2Siebel(String companyId, List<String> ids, Integer frequence);
	/**
	 * 核对表数据，如果水表本月用量高于上月15方或者电表本月用量多于上月50%，添加异常
	 * @param companyId
	 * @param projectId
	 * @param meterType
	 * @return
	 */
	BaseDto checkMeterData(String companyId,TcMeterData t);
	/**
	 * 修改表 的读数
	 * @param companyId
	 * @param data
	 * @return
	 */
	public BaseDto modifyTotalReadingById(String companyId, TcMeterData data);

	BaseDto getLastTaskMterData(String companyId, TcMeterData data);
	/**
	 * 根据年份和建筑编号查找相应的元数据信息
	 */
	List<TcMeterData>queryMeterByYear(String companyId,String meterCode,int meterType, String year);
	
	BaseDto modifyMeterDataStatus(String companyId,TcMeterData tcMeterData); 
	
}
