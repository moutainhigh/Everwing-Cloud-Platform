package com.everwing.coreservice.wy.dao.mapper.business.meterdata;

import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.MeterDateAndUseCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TcMeterDataMapper {

	
	List<TcMeterData> listPageHistories(TcMeterData entity) throws DataAccessException;
	
	int add(TcMeterData data) throws DataAccessException;
	
	int modify(TcMeterData data) throws DataAccessException;
	
	int del(String id) throws DataAccessException;
	
	int batchAdd(List<TcMeterData> datas) throws DataAccessException;
	
	int batchDel(List<String> ids) throws DataAccessException;

	List<Map<String, Object>> findResultByBuildingCode(String readingPosition) throws DataAccessException;

	List<Map<String,Object>> listPageDatas(TcMeterData tcMeterData) throws DataAccessException;

	TcMeterData getLastData(TcMeterData obj) throws DataAccessException;
	
	int delByScheduleIds(List<String> ids) throws DataAccessException;
	
	List<String> getCanAuditMeterData(TcReadingTask task) throws DataAccessException;
	
	List<String> getCanAuditMeterDataByids(Map<String,Object> paramMap) throws DataAccessException;
	
	int batchAudit(Map<String,Object> paramMap) throws DataAccessException;

	List<Map<String,Object>> listPageDatasToAudit(TcMeterData data) throws DataAccessException;
	
	List<Map<String,Object>> listPageReadingRecords(TcMeterData data) throws DataAccessException;
	
	int inValidDataByTaskIdAndMeterCode(TcMeterData data) throws DataAccessException;
	
	TcMeterData getDataById(String id) throws DataAccessException;
	
	List<TcMeterData> getMeterDataForImport(Map<String, Object> paraMap) throws DataAccessException;

	
	TcMeterData getNoOpeartRead(TcMeterData obj) throws DataAccessException;
	
	/**
	 * 批量更新
	 */
	int batchUpdate(List<TcMeterData> listdata) throws DataAccessException;

	/**
	 * 对未抄表的,且表编号为传入的表,进行替换.
	 * 未抄表 : 表示该抄表数据为最新一批数据
	 * @param updateData
	 * @param replaceBeforeCode
	 * @return
	 */
	int replaceMeter(Map<String,Object> paramMap) throws DataAccessException;

	
	List<Map<String,Object>> listPageDatasToAuditForEle(TcMeterData data) throws DataAccessException;

	Integer getAuditedDatasCountByTaskId(String id) throws DataAccessException;
	
	List<TcMeterData> findAllDatasByTaskId(String id) throws DataAccessException;
	
	/**
	 * 水表抄表异常数据的查询
	 * @param meterType 0:水表 
	 * @return TcMeterData
	 */
	List<Map<String, String>> listPageAbnormalData(TcMeterData meterData) throws DataAccessException;
	
	
	/**
	 * 电表抄表异常数据的查询
	 * @param meterType 1:水表 
	 * @return TcMeterData
	 */
	List<Map<String,String>> listPageAbnormalElectData(TcMeterData meterData) throws DataAccessException;
	
	/**
	 * 统计查询出指定用户的抄表数据统计信息
	 * @param meterType 0:水表   1：电表    (必须参数)
	 * 		dataType  查询数据类型   1 已抄表  2 未抄表  3 所有(必须参数)
	 * 		searchTime  日期  规定年月  2017-06
	 * 		readingPerson  抄表人
	 */
	Map<String, String> getReadingInfoByYear(Map<String, String> paramMap) throws DataAccessException;
	

	/**
	 * 因为分页所以单独一个方法
	 */
	List<Map<String, String>> listPageReadingInfoByYear(TcMeterData tcMeterData) throws DataAccessException;
	

	TcMeterData findByBuildingCode(String code) throws DataAccessException;

	Map<String, Object> findByTaskIdAndBuildingCode(String code, String taskId) throws DataAccessException;

	/**
	 * 根据投诉工单对应的重抄任务, 在完成该投诉工单对应的完成工单后,将本月自动生成的抄表任务的lastTotalReading改成完成工单的读数
	 * @param beforeTaskId
	 * @param meterCode
	 * @return
	 */
	TcMeterData findCurrentMonthData(String beforeTaskId, String meterCode);

	Integer batchUnUseNotExistsDatas(Map<String, Object> paramMap);
	
	TcMeterData findLastDataByBuildingCodeAndScheduleId(Map<String,Object> map);

	/**
	 * 查找是否存在下个周期内的新任务 , 针对计划修改,生成新的任务及新的抄表数据
	 * @param data
	 * @return
	 */
	TcMeterData findNextData(TcMeterData data);
	
	/**
	 * @describe 水电表计费，根绝关联建筑code查询本次计费的抄表数据
	 * @param relationBuilding 关联建筑code
	 * @param meterType 表类型：0 水表  1 电表
	 * @return
	 */
	TcMeterData getMeterDataForCharge(String relationBuilding,String meterType);

	void delByTaskId(String id);

	List<TcMeterData> findExistsData(TcMeterData data);

	/**
	 * 当有投诉情况存在时, 需要找到完成工单
	 * @param data
	 * @return
	 */
	TcMeterData findComplatintData(TcMeterData data);
	
	Double getLastMeterReadingByCode(String meterCode,String shareFrequency,String meterType);
	
	

	/**
	 * 根据项目编号和表类型(水表还是电表)查找表用量、和收费对象编号。主要用于手动计费，查找表用量
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> getCountAndFeeObjByProjct(Map<String,Object> map) throws DataAccessException;

    List<TcMeterData> queryByTaskIds(@Param("taskIds")List<String> taskIds,@Param("meterType")int meterType) throws DataAccessException;

    List<TcMeterData> queryByTaskId(@Param("taskId")String taskId,@Param("meterType")int meterType) throws DataAccessException;

    List<TcMeterData> queryByDescription(@Param("description")String description,@Param("meterType")int meterType) throws DataAccessException;

    List<TcMeterData> queryYearData(@Param("meterCode")String meterCode, @Param("meterType")int meterType, @Param("year")String year)throws DataAccessException;

    int updateWMeterData(@Param("taskId") String taskId,@Param("accountId") String accountId,@Param("meterCode")String meterCode,@Param("readingTime") Date readingTime,
                         @Param("totalReading") Double totalReading,@Param("fileId")String fileId,@Param("useCount")Double useCount)throws DataAccessException;

    int updateEMeterData(@Param("taskId")String taskId, @Param("accountId") String accountId,@Param("meterCode")String meterCode,@Param("readingTime") Date readingTime,
                         @Param("totalReading") Double totalReading, @Param("peakrReading") Double peakrReading,
                         @Param("vallyReading") Double vallyReading, @Param("commonReading") Double commonReading,
                         @Param("fileId")String fileId,@Param("useCount")Double useCount,
                         @Param("peakCount")Double peakCount,@Param("vallyCount")Double vallyCount,
                         @Param("commonCount")Double commonCount)throws DataAccessException;

    TcMeterData queryByMeterCodeAndTaskId(@Param("taskId")String taskId,@Param("meterCode")String meterCode);
    
    
    /**
	 * 根据taskId和meterCode查询
	 */
	TcMeterData findByTaskIdAndMeterCode(String projectId,String taskId,String meterCode);
	
	/**
	 * 根据buildCode和projectId查询最新的抄表数据
	 */
	Map<String,Object> findNewBybuildCodeAndProId(Map<String,Object> map);
	
	/**
	 * 根据taskId和codes查找meterData数据
	 */
	List<TcMeterData> findByTaskIdAndCodes(Map<String,Object> map);
	
	/**
	 * 根据taskIds和codes以及项目编码查询
	 */
	List<TcMeterData> findByTaskIdsAndCodes(Map<String,Object> map);
	
	/**
	 * 根据metercode和项目编号和tasks查询
	 */
	TcMeterData findMByMeterCodeAndProIdTasks(Map<String,Object> map);

	TcMeterData findLastData(TcMeterData thisData);

	List<TcMeterData> findMeterByTaskIds(List<String> ids);
	
	Double  getLastMeterCount(@Param("meterCode")String meterCode,@Param("lastTaskId")String lastTaskId);
	
	TcMeterData getMeterDataById(String id)throws DataAccessException;
	
	
	List<String> getCanCheckDatas(TcMeterData thisData)throws DataAccessException;

	int batchUpdateDatas(List<String> ids)throws DataAccessException;
    double sumUseCountByMeterCodeAndType(@Param("projectId")String projectId,@Param("code")String code,@Param("type") int type);

	int updateUseCountToM(@Param("projectId")String projectId,@Param("code") String code,@Param("useCount") double totalUseCount,@Param("type") int type);
	/**
	 * 根据年份和建筑编号查找相应的元数据信息
	 * @param meterCode
	 * @param meterType
	 * @param year
	 * @return
	 * @throws DataAccessException
	 */
	List<TcMeterData> queryMeterByYear(@Param("meterCode")String meterCode, @Param("meterType")int meterType, @Param("year")String year)throws DataAccessException;
	void  modifyMeterDataStatus(TcMeterData tcMeterData) throws DataAccessException;

    List<Map<String,Object>> getCountAndFeeObjByProject(Map<String, Object> paramMap);

    List<MeterDateAndUseCount> findAllWaterDateAndUseCount( String buildingCode);
	List<MeterDateAndUseCount>findAllEleDateAndUseCount(String buildingCode);
}
