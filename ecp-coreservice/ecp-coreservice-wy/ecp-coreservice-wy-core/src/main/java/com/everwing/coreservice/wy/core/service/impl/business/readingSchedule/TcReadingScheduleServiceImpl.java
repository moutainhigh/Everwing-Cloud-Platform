package com.everwing.coreservice.wy.core.service.impl.business.readingSchedule;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.MeterEnum;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.meterrelation.TcMeterRelation;
import com.everwing.coreservice.common.wy.entity.business.readingschedule.TcReadingSchedule;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import com.everwing.coreservice.common.wy.entity.order.TcOrderComplete;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import com.everwing.coreservice.common.wy.service.business.readingSchedule.TcReadingScheduleService;
import com.everwing.coreservice.wy.dao.mapper.business.electmeter.TcElectMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterrelation.TcMeterRelationMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingschedule.TcReadingScheduleMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingtask.TcReadingTaskMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargingSchemeMapper;
import com.everwing.coreservice.wy.dao.mapper.order.complete.TcOrderCompleteMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service("tcReadingScheduleService")
public class TcReadingScheduleServiceImpl implements TcReadingScheduleService{
	
	@Autowired
	private TBsChargingSchemeMapper tBsChargingSchemeMapper;

	@Autowired
	private TcReadingScheduleMapper tcReadingScheduleMapper;
	
	@Autowired
	private TcReadingTaskMapper tcReadingTaskMapper;

	@Autowired
	private TcWaterMeterMapper tcWaterMeterMapper;
	
	@Autowired
	private TSysUserMapper tSysUserMapper;
	
	@Autowired
	private TcMeterRelationMapper tcMeterRelationMapper;
	
	@Autowired
	private TcMeterDataMapper tcMeterDataMapper;
	
	@Autowired
	private TcElectMeterMapper tcElectMeterMapper;
	
	@Autowired
	private TcOrderCompleteMapper tcOrderCompleteMapper;
	
	private static final String NOBODY_STR = "暂无";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPage(String companyId,TcReadingSchedule tcReadingSchedule) {
		List<TcReadingSchedule> list = this.tcReadingScheduleMapper.listPage(tcReadingSchedule);
		if(!CollectionUtils.isEmpty(list)){
			for(TcReadingSchedule t : list){

				TSysUserList tSysUserList = null;
				TSysUserSearch tSysUserSearch = new TSysUserSearch();
				tSysUserSearch.setUserId(t.getCreateId());
				List<TSysUserList> userLists = tSysUserMapper.findByCondition(tSysUserSearch);
				if(org.apache.commons.collections.CollectionUtils.isNotEmpty(userLists)){
					tSysUserList = userLists.get(0);
				}
				t.setCreateId((null == tSysUserList) ? NOBODY_STR:tSysUserList.getLoginName());
				t.setTasks(this.tcReadingTaskMapper.getTasksByScheduleId(t.getId()));
			}
		}
		return new BaseDto(list, tcReadingSchedule.getPage());
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto insert(String companyId, TcReadingSchedule tcReadingSchedule,Integer flagCount) {
		if(CommonUtils.isEmpty(tcReadingSchedule)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"参数为空,插入失败"));
		}
		
		//判断当前新建的计划: 若为启用,查看是否还有其他正在启用的计划,如果有正在启用的任务,则提示无法创建,提示需要修改该计划为停用状态,在停用其他计划的时候,才能使用该计划
		if(tcReadingSchedule.getStatus() == 1 && CommonUtils.isNotEmpty(this.tcReadingScheduleMapper.findUsingSchedule(tcReadingSchedule))){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"当前已有执行中计划,请修改当前计划为停用后,再进行保存."));
		}
		
		boolean flag = (1 == flagCount) ? false:true;	//当flagCount为2时,为重抄任务,除了插入之外,还需要将原抄表数据置为无效
		String id = CommonUtils.getUUID();
		tcReadingSchedule.setId(id);
		tcReadingSchedule.setCreateTime(new Date());
		tcReadingSchedule.setModifyTime(new Date());
		tcReadingSchedule.setScheduleCode(getNextScheduleCode(5));
		int totalCount = 0 ;
		Integer meterType= tcReadingSchedule.getMeterType(); //在这里区分水/电表
		// 插入任务表 , 第一次插入任务表,需要对该任务中关联位置下的所有水表数进行聚合,然后放入任务中
		if(CommonUtils.isNotEmpty(tcReadingSchedule.getTasks())){
			Map<String,Object> codeMap = this.tcReadingTaskMapper.getMaxTaskCode();
			Integer taskCode = (CommonUtils.isEmpty(codeMap) ? 0:CommonUtils.null2Int(codeMap.get("taskCode")));
			for(TcReadingTask task : tcReadingSchedule.getTasks()){
				String taskId = CommonUtils.getUUID();
				task.setId(taskId);
				int count = 0;
				if(CommonUtils.isNotEmpty(task.getReadingPosition())){
					List<String> positions = CommonUtils.str2List(task.getReadingPosition(), Constants.STR_COMMA);
					List<TcMeterRelation> relations = new ArrayList<TcMeterRelation>();
					if(CommonUtils.isNotEmpty(positions)){
						for(String position : positions){
							//批量插入到关联关系表
							TcMeterRelation obj = new TcMeterRelation();
							obj.setBuildingCode(position);
							obj.setRelationId(taskId);
							obj.setType(3);
							relations.add(obj);
						}
						this.tcMeterRelationMapper.batchAdd(relations);
						//还要批量生成任务绑定的抄表初始数据
						count = genAndInsertMeterData(task,meterType,tcReadingSchedule,flag);
					}
				}
				
				task.setReadingPosition("");
				task.setTaskCode("TASK".concat(CommonUtils.complete(String.valueOf(++taskCode), '0', 5)));
				task.setScheduleId(id);
				task.setScheduleCode(tcReadingSchedule.getScheduleCode());
				task.setAuditStartTime(tcReadingSchedule.getAuditStartTime());
				task.setAuditEndTime(CommonUtils.changeDays(tcReadingSchedule.getAuditStartTime(), tcReadingSchedule.getAuditDays()));
				task.setStatus(tcReadingSchedule.getStatus());  
				task.setAuditStatus(0);
				task.setStatus(1);
				task.setMeterType(tcReadingSchedule.getMeterType());
				task.setStartTime(tcReadingSchedule.getReadingStartTime()); //开始时间
				task.setEndTime(CommonUtils.changeDays(tcReadingSchedule.getReadingStartTime(), tcReadingSchedule.getReadingDays())); //任务结束时间
				task.setScheduleContent(tcReadingSchedule.getScheduleName());
				task.setTotalMeterCount(count);   // 这里引入查水表的接口, 将该任务查询位置  building_code下的所有水表数查出,并放入此处
				task.setCompleteMeterCount(0);
				task.setRemainMeterCount(count);  // 第一次插入,则此处应该为总水表数
				task.setCreateTime(new Date());
				task.setModifyTime(new Date());
				totalCount += count;
			}
			this.tcReadingTaskMapper.batchAdd(tcReadingSchedule.getTasks());
		}
		tcReadingSchedule.setTotalMeterCount(totalCount);
		this.tcReadingScheduleMapper.add(tcReadingSchedule);
		return new BaseDto(new MessageMap(null, "插入成功"));
	}
	
	private int genAndInsertMeterData(TcReadingTask task ,Integer meterType,TcReadingSchedule tcReadingSchedule, boolean flag){
		if(CommonUtils.isEmpty(task)){
			return 0;
		}
		int count=0;
		TSysUserList tSysUserList = null;
		TSysUserSearch tSysUserSearch = new TSysUserSearch();
		tSysUserSearch.setUserId(task.getReadingPersonId());
		List<TSysUserList> userLists = tSysUserMapper.findByCondition(tSysUserSearch);
		if(org.apache.commons.collections.CollectionUtils.isNotEmpty(userLists)){
			tSysUserList = userLists.get(0);
		}

		if(meterType.equals(MeterEnum.RECORD_METERTYPE_WARTER.getIntValue())){
			List<TcMeterData> datas = new ArrayList<TcMeterData>();
			List<TcWaterMeter> meters = this.tcWaterMeterMapper.findMetersByPositions(CommonUtils.str2List(task.getReadingPosition(), Constants.STR_COMMA));
			 count = meters.size();
			if(CommonUtils.isNotEmpty(meters)){
				
				for(TcWaterMeter meter : meters){
					TcMeterData data = new TcMeterData();
					data.setAuditStatus(0);
					data.setMeterCode(meter.getCode());
					data.setMeterType(MeterEnum.RECORD_METERTYPE_WARTER.getIntValue()); //水表
					data.setReadingPersonId(task.getReadingPersonId());
					data.setReadingPersonName(tSysUserList.getStaffName());
//					data.setReadingTime(task.getStartTime());
					TcOrderComplete tcOrderComplete = this.tcOrderCompleteMapper.findByBuildCodeAndProjectId(meter.getRelationBuilding(), meter.getProjectId(), 0);
					if(CommonUtils.isEmpty(tcOrderComplete)){
						data.setLastTotalReading(meter.getInitAmount()); //这里先要去检查是否有做过产权变更的情况，如果做了产权变更，这里需要填写产权变更时的读数，如果没有做产权变更这里电表需要将初始化读数放入到本次抄表数据的上次读数
					}else{
						data.setLastTotalReading(CommonUtils.null2Double(tcOrderComplete.getCompleteContent()));
					}
					data.setTaskId(task.getId());
					data.setIsUsed(0);
					data.setProjectId(tcReadingSchedule.getProjectId());
					data.setProjectName(tcReadingSchedule.getProjectName());
					data.setBeforeTaskId((flag) ? task.getId() : ""); //将新数据关联上之前的任务id
					data.setCreateId(task.getCreateId());
					data.setModifyId(task.getModifyId());
					data.setBeforeTaskId(task.getLastTaskId());
					data.setCreateTime(new Date());
					data.setModifyTime(new Date());
					datas.add(data);
				}
				if(!datas.isEmpty()){
					this.tcMeterDataMapper.batchAdd(datas); //插入新数据
					if(flag){
						//此处将该任务下的单条抄表数据置为无效
						TcMeterData data = new TcMeterData();
						data.setTaskId(task.getId());
						data.setMeterCode(datas.get(0).getMeterCode());
						data.setIsUsed(1);
						this.tcMeterDataMapper.inValidDataByTaskIdAndMeterCode(data);
					}
				}
			}
		}
		if(meterType.equals(MeterEnum.RECORD_METERTYPE_ELECT.getIntValue())){
			List<TcMeterData> datas = new ArrayList<TcMeterData>();
			List<ElectMeter> meters = this.tcElectMeterMapper.findMetersByPositions(CommonUtils.str2List(task.getReadingPosition(), Constants.STR_COMMA));
			count = meters.size();
			 if(CommonUtils.isNotEmpty(meters)){
					for(ElectMeter meter : meters){
						TcMeterData data = new TcMeterData();
						data.setAuditStatus(0);
						data.setMeterCode(meter.getCode());
						data.setMeterType(MeterEnum.RECORD_METERTYPE_ELECT.getIntValue()); //电表
						data.setReadingPersonId(task.getReadingPersonId());
						data.setReadingPersonName(tSysUserList.getStaffName());
						data.setReadingTime(task.getStartTime());
						TcOrderComplete tcOrderComplete = this.tcOrderCompleteMapper.findByBuildCodeAndProjectId(meter.getRelationbuilding(), meter.getProjectId(), 1);
						if(CommonUtils.isEmpty(tcOrderComplete)){
							data.setLastTotalReading(meter.getInitamount()); //这里先要去检查是否有做过产权变更的情况，如果做了产权变更，这里需要填写产权变更时的读数，如果没有做产权变更这里电表需要将初始化读数放入到本次抄表数据的上次读数
						}else{
							data.setLastTotalReading(CommonUtils.null2Double(tcOrderComplete.getCompleteContent()));
							data.setLastCommonReading(CommonUtils.null2Double(tcOrderComplete.getCompleteCommonReading()));
							data.setLastPeakReading(CommonUtils.null2Double(tcOrderComplete.getCompletePeakReading()));
							data.setLastVallyReading(CommonUtils.null2Double(tcOrderComplete.getCompleteVallyReading()));
						}
						data.setTaskId(task.getId());
						data.setIsUsed(0);
						data.setProjectId(tcReadingSchedule.getProjectId());
						data.setProjectName(tcReadingSchedule.getProjectName());
						data.setBeforeTaskId((flag) ? task.getId() : ""); //将新数据关联上之前的任务id
						data.setCreateId(task.getCreateId());
						data.setModifyId(task.getModifyId());
						data.setBeforeTaskId(task.getLastTaskId());
						data.setCreateTime(new Date());
						data.setModifyTime(new Date());
						datas.add(data);
					}
					if(!datas.isEmpty()){
						this.tcMeterDataMapper.batchAdd(datas); //插入新数据
						if(flag){
							//此处将该任务下的单条抄表数据置为无效
							TcMeterData data = new TcMeterData();
							data.setTaskId(task.getId());
							data.setMeterCode(datas.get(0).getMeterCode());
							data.setIsUsed(1);
							this.tcMeterDataMapper.inValidDataByTaskIdAndMeterCode(data);
						}
					}
				}
		}
		return count;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto modify(String companyId, TcReadingSchedule tcReadingSchedule) {
		if(CommonUtils.isEmpty(tcReadingSchedule)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"参数为空,修改失败"));
		}
		
		//判断当前修改的计划: 若为启用,查看是否还有其他正在启用的计划,如果有正在启用的任务,则提示无法创建,提示需要修改该计划为停用状态,在停用其他计划的时候,才能使用该计划
		if(tcReadingSchedule.getStatus() == 1 && CommonUtils.isNotEmpty(this.tcReadingScheduleMapper.findUsingSchedule(tcReadingSchedule))){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"当前已有执行中计划,请修改当前计划为停用后,再进行保存."));
		}
		tcReadingSchedule.setModifyTime(new Date());
		
		//插入新的原始抄表数据
		Integer meterType = tcReadingSchedule.getMeterType();

		//判断本计划的执行时间与水电表的抄表scheme时间是否冲突
		TBsChargingScheme paramScheme = new TBsChargingScheme();
		paramScheme.setSchemeType((meterType == 0) ? BillingEnum.SCHEME_TYPE_WATER.getIntV() : BillingEnum.SCHEME_TYPE_ELECT.getIntV());
		paramScheme.setIsUsed(BillingEnum.IS_USED_USING.getIntV());
		paramScheme.setProjectId(tcReadingSchedule.getProjectId());
		TBsChargingScheme schema = this.tBsChargingSchemeMapper.findUsingScheme(paramScheme);
		int startDay = tcReadingSchedule.getReadingStartTime().getDate();
		int endDay = startDay + tcReadingSchedule.getReadingDays();
		int auditStartDay = tcReadingSchedule.getAuditStartTime().getDate();
		int auditEndDay = auditStartDay + tcReadingSchedule.getAuditDays();
		if(null != schema){  //检查时间
			if(schema.getChargeData() >= startDay && schema.getChargeData() <= endDay){
				
				return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,((meterType == 0) ? "水费":"电表")) + "计费时间为["+schema.getChargeData() + "日], 当前[抄表时间]与其冲突, 请酌情修改抄表时间. ");
			
			}else if(schema.getChargeData() >= auditStartDay && schema.getChargeData() <= auditEndDay){
				
				return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,((meterType == 0) ? "水费":"电表")) + "计费时间为["+schema.getChargeData() + "日], 当前[审核时间]与其冲突, 请酌情修改抄表时间. ");
			
			}
		}
		
		int totalCount = 0;
		List<TcReadingTask> editedTasks = tcReadingSchedule.getTasks();
		if(CommonUtils.isNotEmpty(editedTasks)){
			//编辑任务后, 产生下个周期的新任务,本周期内的任务不再发生变化
			List<TcMeterRelation> newRelations = new ArrayList<TcMeterRelation>();
			for(TcReadingTask task : editedTasks){
				int count = 0;
				newRelations.clear();
				List<String> positions = CommonUtils.str2List(task.getReadingPosition(), Constants.STR_COMMA);
				String position = task.getReadingPosition();
				
				//任务已完成,不再做处理
				if(task.getStatus() == 2)
					continue;

//				TcReadingTask usingTask = this.tcReadingTaskMapper.findUsingTaskByScheduleId(tcReadingSchedule.getId());
				if(CommonUtils.isEmpty(task.getId())){
					//任务为新增 , 下个周期生效 , 判断当前是否含有执行中的任务,若含有,则该任务推至执行中的任务的下周期
					task.setId(CommonUtils.getUUID());
					
					count = genAndInsertMeterData(task, meterType, tcReadingSchedule, false);
					
					if(CommonUtils.isNotEmpty(positions)){
						for(String p : position.split(",")){
							TcMeterRelation relation = new TcMeterRelation();
							relation.setBuildingCode(p);
							relation.setRelationId(task.getId());
							relation.setType(3);
							newRelations.add(relation);
						}
						this.tcMeterRelationMapper.batchAdd(newRelations);
					}
					
					task.setCreateTime(new Date());
					task.setModifyTime(new Date());
					task.setTotalMeterCount(count);
					task.setRemainMeterCount(count);
					task.setCompleteMeterCount(0);
					task.setReadingPosition(null);
					task.setStatus(0);   //该任务为新建
					task.setAuditStatus(0);
					task.setScheduleId(tcReadingSchedule.getId());
					task.setScheduleCode(tcReadingSchedule.getScheduleCode());
					task.setMeterType(tcReadingSchedule.getMeterType());
					task.setScheduleContent(tcReadingSchedule.getScheduleName());
					
					//TODO 计划编辑时 ,下个月的任务生效.
//					if(null == usingTask){
					task.setAuditStartTime(CommonUtils.changeMonths(CommonUtils.setCurrentDate(tcReadingSchedule.getAuditStartTime()), tcReadingSchedule.getExecFreq())); //下个月生效
					task.setStartTime(CommonUtils.changeMonths(CommonUtils.setCurrentDate(tcReadingSchedule.getReadingStartTime()),tcReadingSchedule.getExecFreq())); //开始时间 下个月生效
					/*}else{
						task.setAuditStartTime(CommonUtils.changeMonths(usingTask.getAuditStartTime(), tcReadingSchedule.getExecFreq()));
						task.setStartTime(CommonUtils.changeMonths(usingTask.getStartTime(), tcReadingSchedule.getExecFreq()));
					}*/
					task.setAuditEndTime(CommonUtils.changeDays(task.getAuditStartTime(), tcReadingSchedule.getAuditDays()));
					task.setEndTime(CommonUtils.changeDays(task.getStartTime(), tcReadingSchedule.getReadingDays())); //任务结束时间
					this.tcReadingTaskMapper.insert(task);
					
					
					
				}else{
					//任务为修改  ,id存在 , 这个任务不做修改 , 产生下个月的新任务
					
					if(task.getStatus() == 0){
						//该任务为新建状态 , 将该任务下的抄表数据以及relation删除后重新插入,任务进行更新
						this.tcMeterDataMapper.delByTaskId(task.getId());
						this.tcMeterRelationMapper.delByRelationId(task.getId());
						
						count = genAndInsertMeterData(task, meterType, tcReadingSchedule, false);
						if(CommonUtils.isNotEmpty(positions)){
							for(String p : position.split(",")){
								TcMeterRelation relation = new TcMeterRelation();
								relation.setBuildingCode(p);
								relation.setRelationId(task.getId());
								relation.setType(3);
								newRelations.add(relation);
							}
							this.tcMeterRelationMapper.batchAdd(newRelations);
						}
						task.setStatus(0);
						task.setAuditStatus(0);
						task.setCompleteMeterCount(0);
						task.setRemainMeterCount(count);
						task.setTotalMeterCount(count);
						task.setReadingPosition(null);
						task.setAuditStartTime(tcReadingSchedule.getAuditStartTime()); //修改任务审核时间
						task.setStartTime(tcReadingSchedule.getReadingStartTime()); //开始时间 下个月生效
						task.setAuditEndTime(CommonUtils.changeDays(task.getAuditStartTime(), tcReadingSchedule.getAuditDays()));
						task.setEndTime(CommonUtils.changeDays(task.getStartTime(), tcReadingSchedule.getReadingDays())); //任务结束时间
						this.tcReadingTaskMapper.update(task);
					}else{
						//任务为进行中状态
						//若本月已修改一次,即已经生成了下个月的任务, 再次修改的话,需要将已生成的下个月的任务进行删除
						TcReadingTask nextTask = this.tcReadingTaskMapper.getNextTaskByCurrentId(task.getId());
						if(null != nextTask){
							//删除新生成的下月任务
							this.tcMeterDataMapper.delByTaskId(nextTask.getId());
							this.tcMeterRelationMapper.delByRelationId(nextTask.getId());
							this.tcReadingTaskMapper.delByTaskId(nextTask.getId());
						}
						nextTask = new TcReadingTask();
						nextTask.setId(CommonUtils.getUUID());
						nextTask.setReadingPosition(task.getReadingPosition());
						nextTask.setCreateId(task.getCreateId());
						nextTask.setModifyId(task.getCreateId());
						count = genAndInsertMeterData(nextTask, meterType,tcReadingSchedule, false);  //生成并插入tc_meter_data
						if(CommonUtils.isNotEmpty(positions)){
							for(String p : position.split(",")){
								TcMeterRelation relation = new TcMeterRelation();
								relation.setBuildingCode(p);
								relation.setRelationId(task.getId());
								relation.setType(3);
								newRelations.add(relation);
							}
							this.tcMeterRelationMapper.batchAdd(newRelations);
						}
						
						nextTask.setLastTaskId(task.getId());
						nextTask.setCreateTime(new Date());
						nextTask.setModifyTime(new Date());
						nextTask.setTotalMeterCount(count);
						nextTask.setRemainMeterCount(count);
						nextTask.setCompleteMeterCount(0);
						nextTask.setReadingPosition(null);
						nextTask.setStatus(0);   //该任务为新建
						nextTask.setAuditStatus(0);
						nextTask.setScheduleId(tcReadingSchedule.getId());
						nextTask.setScheduleCode(tcReadingSchedule.getScheduleCode());
						nextTask.setMeterType(tcReadingSchedule.getMeterType());
						nextTask.setScheduleContent(tcReadingSchedule.getScheduleName());
//						if(null == usingTask){
						//TODO 计划修改时间时, 新任务时间由修改后的计划时间产生
						nextTask.setAuditStartTime(CommonUtils.changeMonths(CommonUtils.setCurrentDate(tcReadingSchedule.getAuditStartTime()), tcReadingSchedule.getExecFreq())); //下个月生效
						nextTask.setStartTime(CommonUtils.changeMonths(CommonUtils.setCurrentDate(tcReadingSchedule.getReadingStartTime()),tcReadingSchedule.getExecFreq())); //开始时间 下个月生效
						/*}else{
							nextTask.setAuditStartTime(CommonUtils.changeMonths(usingTask.getAuditStartTime(), tcReadingSchedule.getExecFreq()));
							nextTask.setStartTime(CommonUtils.changeMonths(usingTask.getStartTime(), tcReadingSchedule.getExecFreq()));
						}*/
						nextTask.setAuditEndTime(CommonUtils.changeDays(nextTask.getAuditStartTime(), tcReadingSchedule.getAuditDays()));
						nextTask.setEndTime(CommonUtils.changeDays(nextTask.getStartTime(), tcReadingSchedule.getReadingDays())); //任务结束时间
						this.tcReadingTaskMapper.insert(nextTask);
						
						
						//修改本计划的时间
						task.setReadingPosition(null);
						task.setAuditStartTime(tcReadingSchedule.getAuditStartTime()); //修改任务审核时间
						task.setStartTime(tcReadingSchedule.getReadingStartTime()); //开始时间 下个月生效
						task.setAuditEndTime(CommonUtils.changeDays(task.getAuditStartTime(), tcReadingSchedule.getAuditDays()));
						task.setEndTime(CommonUtils.changeDays(task.getStartTime(), tcReadingSchedule.getReadingDays())); //任务结束时间
						this.tcReadingTaskMapper.update(task);
					}
				}
				this.tcReadingTaskMapper.updateTaskCount(task.getId());
			}
		}
		this.tcReadingScheduleMapper.modify(tcReadingSchedule);
		return new BaseDto(new MessageMap(null, "修改成功"));
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto del(String companyId, String ids) {
		MessageMap msgMap = null;
		if(CommonUtils.isEmpty(ids)){
			msgMap = new MessageMap(MessageMap.INFOR_ERROR,"参数为空,删除失败");
		}
		else{
			List<String> idList = CommonUtils.str2List(ids, Constants.STR_COMMA);
			//先删除任务/建筑关联关系表中的数据
			this.tcMeterRelationMapper.batchDelByscheduleIds(idList);

			//删除抄表记录
			this.tcMeterDataMapper.delByScheduleIds(idList);
			
			//删除任务
			this.tcReadingTaskMapper.delByScheduleIds(idList);
			
			this.tcReadingScheduleMapper.del(idList);
			
			msgMap = new MessageMap(null,"删除成功");
		}
		return new BaseDto(msgMap);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto getInfoById(String companyId, String id) {
		if(CommonUtils.isEmpty(id)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "传入参数为空"));
		}
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(this.tcReadingScheduleMapper.getScheduleById(id));
		returnDto.setMessageMap(new MessageMap(null, "请求完成"));
		return returnDto;
	}

	/**
	 * 修改状态
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto batchModify(String companyId,TcReadingSchedule tcReadingSchedule) {

		MessageMap msgMap = new MessageMap();
		
		if(CommonUtils.isEmpty(tcReadingSchedule.getId()) || CommonUtils.isEmpty(tcReadingSchedule.getStatus())){
			msgMap.setFlag(MessageMap.INFOR_WARNING);
			msgMap.setMessage("传入参数为空.");
		}else{
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("ids", CommonUtils.str2List(tcReadingSchedule.getId(), Constants.STR_COMMA));
			params.put("status", tcReadingSchedule.getStatus());
			params.put("modifyTime", new Date());
			this.tcReadingTaskMapper.updateStatusByScheduleId(tcReadingSchedule);  //修改状态
			this.tcReadingScheduleMapper.batchModify(params);
			
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			msgMap.setMessage("修改成功");
		}
		return new BaseDto(msgMap);
	}
	
	private String getNextScheduleCode(int totalLength){
		Map<String,Object> codeMap = this.tcReadingScheduleMapper.getMaxScheduleCode();
		Integer scheduleCode = CommonUtils.isNotEmpty(codeMap) ? CommonUtils.null2Int(codeMap.get("scheduleCode")) : 0;
		return "SCHE".concat(CommonUtils.complete(String.valueOf(++scheduleCode), '0', totalLength));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override

	public BaseDto findUsingSchedule(String companyId,TcReadingSchedule tcReadingSchedule) {
		return new BaseDto(this.tcReadingScheduleMapper.findUsingSchedule(tcReadingSchedule), null);
	}
}
