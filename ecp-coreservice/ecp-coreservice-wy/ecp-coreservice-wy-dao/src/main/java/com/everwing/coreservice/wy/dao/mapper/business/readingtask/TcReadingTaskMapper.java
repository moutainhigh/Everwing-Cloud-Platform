package com.everwing.coreservice.wy.dao.mapper.business.readingtask;

import com.everwing.coreservice.common.wy.entity.business.readingschedule.TcReadingSchedule;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface TcReadingTaskMapper {

	int batchAdd(List<TcReadingTask> tasks) throws DataAccessException;
	
	List<TcReadingTask> getTasksByScheduleId(String scheduleId) throws DataAccessException;

	TcReadingTask getWaterTaskDetailById(String id) throws DataAccessException;
	
	Map<String,Object> getMaxTaskCode() throws DataAccessException;

	void delByScheduleIds(List<String> idList) throws DataAccessException;
	
	int updateStatusByScheduleId(TcReadingSchedule entity) throws DataAccessException;
	
	int update(TcReadingTask task) throws DataAccessException;

	List<TcReadingTask> listPageSearchResult(TcReadingTask entity) throws DataAccessException;
	
	Map<String, Long> getReadedMetersCount(String taskId) throws DataAccessException;
	
	List<Map<String,Object>> listPageTasksToAudit(TcReadingTask task) throws DataAccessException;

	int insert(TcReadingTask task) throws DataAccessException;

	Integer findCompletedTaskByBuildingCode(String code);

	Integer batchStopNotExistsTasks(Map<String, Object> paramMap);
	
	/**
	 * 根据laskTaskId查找,应用taskId是主键，则不会找到多条非重抄的数据
	 * @param projectId
	 * @param laskTaskId
	 * @return
	 */
	TcReadingTask findBylastTaskIdAndProjectId(String laskTaskId);

	
	
	/** ------------------------ 自动扫描用 ------------------------ */
	//查找所有可以完成的任务
	List<TcReadingTask> findCanCompleteTasks();
	
	//查找所有可以生成的任务
	List<TcReadingTask> findCanCopyTasks();

	TcReadingTask getNextTaskByCurrentId(String id);

	void delByTaskId(String id);

	Integer startInitTasks();

	TcReadingTask findUsingTaskByScheduleId(String id);

	/**-----------------物业app----------------*/
    List<TcReadingTask> queryCurrentByAccountId(@Param("accountId") String accountId, @Param("meterType")int meterType,@Param("limit") int limit, @Param("pageSize") int pageSize);

    List<TcReadingTask> queryHistoryByAccountId(@Param("accountId") String accountId, @Param("meterType")int meterType,@Param("limit") int limit, @Param("pageSize") int pageSize);

    void updateTaskCount(@Param("taskId")String taskId);

	List<TcReadingTask> findStartInTodayTasks();
	
	List<String> findTaskIdsByScheduleId(@Param("scheduleId")String scheduleId);
	
    String getlastTaskIdByTaskId(@Param("taskId")String taskId);
	
	String getLastTaskById(@Param("lastTackId") String lastTackId);

}
