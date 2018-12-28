package com.everwing.coreservice.wy.core.service.impl.business.readingtask;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.readingschedule.TcReadingSchedule;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.service.business.readingtask.TcReadingTaskService;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingschedule.TcReadingScheduleMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingtask.TcReadingTaskMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("tcReadingTaskService")
public class TcReadingTaskServiceImpl implements TcReadingTaskService{

	@Autowired
	private TcReadingTaskMapper tcReadingTaskMapper;
	
	@Autowired
	private TcMeterDataMapper tcMeterDataMapper;
	
	@Autowired
	private TcReadingScheduleMapper tcReadingScheduleMapper;
	
	@Autowired
	protected AmqpTemplate amqpTemplate;
	
	//消息队列 route_key 声明处
	@Value("${queue.wy2Wy.next.reading.task.key}")
	private String queue_wy2Wy_next_reading_task_key;
	
	@Value("${queue.wy2Siebel.meter.data.push.key}")
	private String route_key_2_siebel;

	/*public BaseDto getWaterTaskDetailById(String companyId, String id) {
		if(CommonUtils.isEmpty(id)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"传入参数为空"));
		}
		BaseDto returnDto = new BaseDto();
		TcReadingTask task = this.tcReadingTaskMapper.getWaterTaskDetailById(id);
		if(null != task){
			task.setDatas(this.tcMeterDataMapper.findResultByBuildingCode(task.getReadingPosition()));
			returnDto.setObj(task);
			returnDto.setMessageMap(new MessageMap(null,"请求完成"));
		}else{
			returnDto.setMessageMap(new MessageMap(MessageMap.EMPTY_RESULT, "未找到相应的任务"));		
		}
		return returnDto;
	}*/

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageSearchResult(String companyId, TcReadingTask entity) {
		BaseDto returnDto = new BaseDto();
		//分页查询
		List<TcReadingTask> tasks = this.tcReadingTaskMapper.listPageSearchResult(entity);
		returnDto.setLstDto(tasks);
		returnDto.setPage(entity.getPage());
		return returnDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageTasksToAudit(String companyId, TcReadingTask task) {
		List<Map<String,Object>> tasks = this.tcReadingTaskMapper.listPageTasksToAudit(task);
		return new BaseDto(tasks, task.getPage());
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto batchAudit(String companyId, List<TcReadingTask> tasks,Integer auditStatus) {
		String flag = null;
		String content = null;
		if(CommonUtils.isEmpty(tasks)){
			flag = MessageMap.INFOR_WARNING;
			content = "传入参数为空,无法审核";
		}else{
			
			//能到审核的时候
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("auditStatus", auditStatus);
			boolean flag1 = true;
			for(TcReadingTask task : tasks){
				//判断当天是否在审核日期内
				if(new Date().compareTo(task.getAuditStartTime()) < 0
						|| new Date().compareTo(task.getAuditEndTime()) > 0){
					flag1 = false;
					continue;
				}
				
				List<String> ids = this.tcMeterDataMapper.getCanAuditMeterData(task);
				if(CommonUtils.isNotEmpty(ids)){
					paramMap.put("ids", ids);
					this.tcMeterDataMapper.batchAudit(paramMap);
					
					TcReadingSchedule schedule = this.tcReadingScheduleMapper.getScheduleById(task.getScheduleId());
					
					//TODO 异步推送至Siebel   2018-01-16 不再同步到siebel
					/*MqEntity entity = new MqEntity();
					entity.setCompanyId(companyId);
					entity.setData(ids);
					entity.setOpr(schedule.getExecFreq());
					this.amqpTemplate.convertAndSend(route_key_2_siebel, entity);*/
				}
				
				//查找该任务下是否都已经审核完成
				Integer auditedCount = this.tcMeterDataMapper.getAuditedDatasCountByTaskId(task.getId());
//				//页面传过来的信息不完整，这里再查询一下数据库
//				TcReadingTask queryTask = this.tcReadingTaskMapper.getWaterTaskDetailById(task.getId());
				
				if(auditedCount == task.getTotalMeterCount()){
					TcReadingTask t = new TcReadingTask();
					t.setId(task.getId());
					t.setAuditStatus(1);
					this.tcReadingTaskMapper.update(t);
					//这里将task发送到mq，准备生成下次的抄表任务
					sendMessage(companyId,task);
				}else{
					//含有未审核通过的表,需要注意
					return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前含有无法审核通过(如错误状态)的抄表数据, 请核实, 当前任务无法整体审核通过."));
				}
			}
			
			if(flag1){
				content = "审核完成.";
			}else{
				content = "当前时间不在部分任务的审核期内,其他任务审核完成";
			}
			flag = MessageMap.INFOR_SUCCESS;
		}
		return new BaseDto(new MessageMap(flag, content));
	}
	
	private void sendMessage(String companyId , TcReadingTask task){
		if(null != task){
			MqEntity e = new MqEntity(BillingEnum.data_insert.getIntV(), task);
			e.setCompanyId(companyId);
			this.amqpTemplate.convertAndSend(queue_wy2Wy_next_reading_task_key, e);
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto batchCompelete(String companyId, List<TcReadingTask> tasks) {
		for(TcReadingTask task : tasks){
			if(task.getEndTime().getTime() > System.currentTimeMillis() || task.getStatus() == 2 || task.getAuditStatus() == 1){
				continue;	//当前任务未结束,已完成或者已审核,无法手动完成
			}
			
			List<TcMeterData> datas = this.tcMeterDataMapper.findAllDatasByTaskId(task.getId());
			
			int completeCount = 0;
			if(CommonUtils.isNotEmpty(datas)){
				StringBuffer sb = new StringBuffer();
				for(TcMeterData data : datas){
					sb.delete(0, sb.length());
					
					if(null != data.getReadingTime() && data.getTotalReading() < data.getLastTotalReading() && data.getCorrection() == 0){
						//满足条件: 1. 已抄表, 2. 本次读数少于上次读数 , 3. 无修正量. 表示该表抄的读数少于上次,且未做修正,该数据做异常处理,不计入完成状态
						continue;
					}
					
					if(CommonUtils.isEmpty(data.getReadingTime())){
						data.setReadingTime(new Date());
					}
					
					if(0 == CommonUtils.null2Double(data.getTotalReading())){
						sb.append("总读数,");
						data.setTotalReading(CommonUtils.null2Double(data.getLastTotalReading()));
					}
					if(0 == CommonUtils.null2Double(data.getVallyReading())){
						data.setVallyReading(CommonUtils.null2Double(data.getLastVallyReading()));
						sb.append("谷值读数,");
					}
					if(0 == CommonUtils.null2Double(data.getCommonReading())){
						data.setCommonReading(CommonUtils.null2Double(data.getLastCommonReading()));
						sb.append("平值读数,");
					}
					if(0 == CommonUtils.null2Double(data.getPeakReading())){
						sb.append("峰值读数,");
						data.setPeakReading(CommonUtils.null2Double(data.getLastPeakReading()));
					}
					data.setUseCount(CommonUtils.null2Double(data.getUseCount()) + data.getTotalReading() - data.getLastTotalReading());		//用量
					data.setPeakCount(CommonUtils.null2Double(data.getPeakCount()) + data.getPeakReading() - data.getLastPeakReading());		//峰值用量
					data.setVallyCount(CommonUtils.null2Double(data.getVallyCount()) + data.getVallyReading() - data.getLastVallyReading());	//谷值用量
					data.setCommonCount(CommonUtils.null2Double(data.getCommonCount()) + data.getCommonReading() - data.getLastCommonReading());	//平值用量
					if(sb.length() > 0){
						data.setRemark(sb.delete(sb.length()-1, sb.length()).append("未抄表,默认为上月读数.").toString());
					}
					this.tcMeterDataMapper.modify(data);
					completeCount ++ ;
				}
			}
			task.setStatus(2);  //任务设置为完成状态
			task.setCompleteMeterCount(completeCount);
			task.setRemainMeterCount(task.getTotalMeterCount() - completeCount);
			this.tcReadingTaskMapper.update(task);
		}
		return new BaseDto(new MessageMap(null,"任务手动完成"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto findByScheduleId(String companyId, String scheduleId) {
		return new BaseDto(this.tcReadingTaskMapper.getTasksByScheduleId(scheduleId),null);
	}

    @Override
    public List<TcReadingTask> queryCurrentByAccountId(String companyId, String accountId, int meterType,int pageNo, int pageSize) {
	    int limit = (pageNo-1)*pageSize;
        return tcReadingTaskMapper.queryCurrentByAccountId(accountId,meterType,limit,pageSize);
    }

    @Override
    public List<TcReadingTask> queryHistoryByAccountId(String companyId, String accountId, int meterType, int pageNo, int pageSize) {
        int limit = (pageNo-1)*pageSize;
        return tcReadingTaskMapper.queryHistoryByAccountId(accountId,meterType,limit,pageSize);
    }


}
