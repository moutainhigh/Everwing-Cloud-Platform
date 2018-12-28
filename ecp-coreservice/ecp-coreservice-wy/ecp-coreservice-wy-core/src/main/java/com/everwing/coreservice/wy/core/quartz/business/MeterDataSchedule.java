package com.everwing.coreservice.wy.core.quartz.business;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.ThreadPool.ThreadPoolUtils;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.datasource.DataSourceUtil;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.meterrelation.TcMeterRelation;
import com.everwing.coreservice.common.wy.entity.business.readingschedule.TcReadingSchedule;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.platform.api.CompanyApi;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @TODO 水表用的定时任务
 * 
 */
@Component
public class MeterDataSchedule extends Resources{

	private static final String AUTO_COMPLETER = "system";
	
	@Autowired
	private CompanyApi companyApi;
	
	
	/**
	 * @TODO 全库扫描: 任务的自动生成
	 * @Hz 每分钟第40秒开始		 
	 */
	public void scanSchedule(){
		System.out.println("任务自动生成  >>>>  开始扫描." + CommonUtils.getDateStr());
		RemoteModelResult<List<Company>>  rslt = companyApi.queryAllCompany();
		if(!rslt.isSuccess() || rslt.getModel() == null || rslt.getModel().isEmpty())
			return;
		
		
		for(final Company company : rslt.getModel()){
//			if(!company.getCompanyId().equals("09841dc0-204a-41f2-a175-20a6dcee0187")) continue;
			ThreadPoolUtils.getInstance().executeThread(new Runnable() {
				@Override
				public void run() {
					System.out.println("任务自动生成: 当前company  >>>> " + company.getCompanyName() +" , 开始扫描." + CommonUtils.getDateStr());
					
					String companyStr = JSON.toJSONString(company);
					DataSourceUtil.changeDataSource(companyStr);	//在此处切换数据源
					
					TcReadingSchedule schedule = new TcReadingSchedule();
					schedule.setMeterType(0);
					List<TcReadingSchedule> schedules = tcReadingScheduleMapper.findUsingSchedule(schedule);
					if(CollectionUtils.isEmpty(schedules)){
						return;
					}
					schedule = schedules.get(0);
					//1. 找出当前库内,生效的计划下,已经完成的任务. 对完成的任务进行复制,生成新的任务 (此时已完成)
					List<TcReadingTask> tasks = tcReadingTaskMapper.findCanCopyTasks();
					
					if(CollectionUtils.isNotEmpty(tasks)){
						List<TcMeterData> newDatas = new ArrayList<TcMeterData>();
						List<TcMeterRelation> relations = new ArrayList<TcMeterRelation>();
						for(TcReadingTask task : tasks){
							//首先需要判断,该任务是否已经生成了下个周期的任务
							TcReadingTask nextTask = tcReadingTaskMapper.getNextTaskByCurrentId(task.getId());
							if(null != nextTask){
								//已经生成了下个月的任务,则此处不再生成.
								continue;
							}
							
							
							newDatas.clear();
							relations.clear();
							String taskId = CommonUtils.getUUID();
							String lastTaskId = task.getId();
							try {
								if(CommonUtils.isNotEmpty(task.getMeterDatas())){
									for(TcMeterData data : task.getMeterDatas()){
										if(data.getIsUsed() == 1) continue;
										
										TcMeterData newData = new TcMeterData();
										BeanUtils.copyProperties(newData, data);
										newData.setId(CommonUtils.getUUID());
										newData.setBeforeTaskId(lastTaskId);
										newData.setTaskId(taskId);
										newData.setLastCommonReading(data.getCommonReading());
										newData.setLastPeakReading(data.getPeakReading());
										newData.setLastTotalReading(data.getTotalReading());
										newData.setLastVallyReading(data.getVallyReading());
										newData.setCreateId(AUTO_COMPLETER);
										newData.setModifyId(AUTO_COMPLETER);
										newData.setCreateTime(new Date());
										newData.setModifyTime(new Date());
										newData.setIsReplaced(0);
										newData.setRemark(null);
										newData.setCommonCount(0.0);
										newData.setPeakCount(0.0);
										newData.setUseCount(0.0);
										newData.setVallyCount(0.0);
										newDatas.add(newData);
									}
									if(CollectionUtils.isNotEmpty(newDatas))
										tcMeterDataMapper.batchAdd(newDatas);
								}
								
								if(CommonUtils.isNotEmpty(task.getRelations())){
									for(TcMeterRelation r : task.getRelations()){
										TcMeterRelation newRelation = new TcMeterRelation();
										newRelation.setBuildingCode(r.getBuildingCode());
										newRelation.setId(CommonUtils.getUUID());
										newRelation.setRelationId(taskId);
										newRelation.setType(3);
										relations.add(newRelation);
									}
									if(CollectionUtils.isNotEmpty(relations))
										tcMeterRelationMapper.batchAdd(relations);
								}
								
								
								TcReadingTask newTask  = new TcReadingTask();
								BeanUtils.copyProperties(newTask, task);
								newTask.setId(taskId);
								newTask.setStartTime(CommonUtils.changeMonths(task.getStartTime(), schedule.getExecFreq()));
								newTask.setEndTime(CommonUtils.changeMonths(task.getEndTime(), schedule.getExecFreq()));
								newTask.setAuditStartTime(CommonUtils.changeMonths(task.getAuditStartTime(), schedule.getExecFreq()));
								newTask.setAuditEndTime(CommonUtils.changeMonths(task.getAuditEndTime(), schedule.getExecFreq()));
								newTask.setAuditStatus(0); //待审核状态
								newTask.setStatus(1); //进行中状态
								newTask.setCreateId(AUTO_COMPLETER);
								newTask.setCreateTime(new Date());
								newTask.setModifyId(AUTO_COMPLETER);
								newTask.setModifyTime(new Date());
								newTask.setLastTaskId(task.getId());
								newTask.setTotalMeterCount(newDatas.size());
								newTask.setRemainMeterCount(newDatas.size());
								newTask.setCompleteMeterCount(0);
								tcReadingTaskMapper.insert(newTask);
							} catch (IllegalAccessException | InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
					System.out.println("任务自动生成: 当前companyName  >>>> " + company.getCompanyName() +" , 结束扫描." + CommonUtils.getDateStr());
				}
			});
		}
		System.out.println("任务自动生成  >>>>  结束扫描." + CommonUtils.getDateStr());
	}
	
	/**
	 * @TODO 全库扫描: 任务的自动完成
	 * @Hz 每分钟的第10秒		 
	 */
	public void autoCompleteTask(){
		System.out.println("任务自动完成  >>>>  开始扫描." + CommonUtils.getDateStr());
		RemoteModelResult<List<Company>>  rslt = companyApi.queryAllCompany();
		if(!rslt.isSuccess() || rslt.getModel() == null || rslt.getModel().isEmpty())
			return;
		
		
		for(final Company company : rslt.getModel()){
//			if(!company.getCompanyId().equals("09841dc0-204a-41f2-a175-20a6dcee0187")) continue;
			ThreadPoolUtils.getInstance().executeThread(new Runnable() {
				@Override
				public void run() {
					System.out.println("任务自动完成: 当前companyName  >>>> " + company.getCompanyName() +" , 开始扫描." + CommonUtils.getDateStr());
					String companyStr = JSON.toJSONString(company);
					DataSourceUtil.changeDataSource(companyStr);	//在此处切换数据源
					//找出有效计划下,当前时间已超过end_time的任务 (此时正在执行中)
					List<TcReadingTask> tasks = tcReadingTaskMapper.findCanCompleteTasks();
					if(CollectionUtils.isNotEmpty(tasks)){
						//对计划下的抄表数据进行统计
						StringBuffer remarkBuffer = new StringBuffer();
						for(TcReadingTask task : tasks){
							int completeCount = 0;
							if(CollectionUtils.isNotEmpty(task.getMeterDatas())){
								for(TcMeterData data : task.getMeterDatas()){
									
									if(data.getIsUsed() == 1) continue; //该条数据无效,不计算
									
									if(data.getMeterType() == 1){
										if(CommonUtils.isEmpty(data.getPeakReading())){
											data.setPeakReading(data.getLastPeakReading()); //设置为上月读数
											remarkBuffer.append("峰值,");
										}
										data.setPeakCount(data.getPeakCount() - data.getLastPeakReading());
										
										if(CommonUtils.isEmpty(data.getVallyReading())){
											data.setVallyReading(data.getLastVallyReading());
											remarkBuffer.append("谷值,");
										}
										data.setVallyCount(data.getVallyReading() - data.getLastVallyReading());
										
										if(CommonUtils.isEmpty(data.getCommonReading())){
											data.setCommonReading(data.getLastCommonReading());
											remarkBuffer.append("平值,");
										}
										data.setCommonCount(data.getCommonReading() - data.getLastCommonReading());
									}
									
									if(CommonUtils.isEmpty(data.getTotalReading())){
										data.setTotalReading(data.getLastTotalReading());
										remarkBuffer.append("总读数,");
									}
									data.setUseCount(data.getTotalReading() - data.getLastTotalReading());
									if(remarkBuffer.length() > 0){
										data.setRemark(remarkBuffer.deleteCharAt(remarkBuffer.lastIndexOf(",")).append("为空,默认为上月读数.").toString());
										remarkBuffer.delete(0, remarkBuffer.length());
									}
									
									//查找当前完成的表  在下个周期内的任务是否产生 (针对 计划修改 , 修改保存后,会产生下个月的新任务以及抄表数据,但是数据都无last_*_reading , 需要从本月填入)
									TcMeterData nextData = tcMeterDataMapper.findNextData(data);
									if(null != nextData){
										nextData.setLastCommonReading(data.getCommonReading());
										nextData.setLastPeakReading(data.getPeakReading());
										nextData.setLastTotalReading(data.getTotalReading());
										nextData.setLastVallyReading(data.getVallyReading());
										tcMeterDataMapper.modify(nextData);
									}
									
									completeCount ++;
									data.setModifyId(AUTO_COMPLETER);
									data.setModifyTime(new Date());
									tcMeterDataMapper.modify(data);
								}
							}
							task.setCompleteMeterCount(completeCount);
							task.setRemainMeterCount(task.getTotalMeterCount() - task.getCompleteMeterCount());
							task.setStatus(2);	//任务完成
							tcReadingTaskMapper.update(task);
							
							
						}
					}
					System.out.println("任务自动完成: 当前companyName  >>>> " + company.getCompanyName() +" , 结束扫描." + CommonUtils.getDateStr());
				}
			});
		}
		System.out.println("任务自动完成  >>>>  结束扫描." + CommonUtils.getDateStr());

	}
	
}
