package com.everwing.coreservice.wy.core.service.impl.business.meterdata;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.BigDecimalUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.ImportExportEnum;
import com.everwing.coreservice.common.wy.common.enums.MeterDataEnum;
import com.everwing.coreservice.common.wy.common.enums.MeterEnum;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.readingschedule.TcReadingSchedule;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcHydroMeterOperationRecord;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.order.TcOrderComplaint;
import com.everwing.coreservice.common.wy.service.business.meterdata.TcMeterDataService;
import com.everwing.coreservice.wy.dao.mapper.business.electmeter.TcElectMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingschedule.TcReadingScheduleMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingtask.TcReadingTaskMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterOperRecordMapper;
import com.everwing.coreservice.wy.dao.mapper.order.complaint.TcOrderComplaintMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service("tcMeterDataService")
public class TcMeterDataServiceImpl implements TcMeterDataService{
	private static final Logger log = LogManager.getLogger(TcMeterDataServiceImpl.class);
	@Autowired
	private TcMeterDataMapper tcMeterDataMapper;
	
	@Autowired
	private TcWaterMeterOperRecordMapper tcWaterMeterOperRecordMapper;
	
	@Autowired
	private TcReadingTaskMapper tcReadingTaskMapper;
	
	@Autowired
	private TcReadingScheduleMapper tcReadingScheduleMapper;
	
	@Autowired
	private TcWaterMeterMapper tcWaterMeterMapper;
	
	@Autowired
	private TcElectMeterMapper tcElectMeterMapper;
	
	@Autowired
	private TcOrderComplaintMapper tcOrderComplaintMapper;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPageDatas(String companyId, TcMeterData tcMeterData) {
		if(CommonUtils.isEmpty(tcMeterData)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "传入参数为空"));
		}
		BaseDto returnDto = new BaseDto();
		List<Map<String,Object>> datas = this.tcMeterDataMapper.listPageDatas(tcMeterData);
		returnDto.setLstDto(datas);
		returnDto.setPage(tcMeterData.getPage());
		returnDto.setMessageMap(new MessageMap(null, "请求成功."));
		return returnDto;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto batchModify(String companyId, List<TcMeterData> datas,String operationType) {

		MessageMap msgMap = null;
		if(CommonUtils.isEmpty(datas)){
			msgMap = new MessageMap(MessageMap.INFOR_WARNING, "传入参数为空.修改失败");
		}else{
			//除去已审核部分
			int trueCount = 0;
			TcReadingTask task = this.tcReadingTaskMapper.getWaterTaskDetailById(datas.get(0).getTaskId());	//获取此次任务对象
			TcReadingSchedule schedule = this.tcReadingScheduleMapper.getScheduleById(task.getScheduleId());
			Integer frequency = schedule.getExecFreq();
			String projectId = (null == schedule) ? "" : schedule.getProjectId();
			for(TcMeterData data : datas){
				if(CommonUtils.isEmpty(data.getTotalReading())){
					continue;
				}
				if(data.getAuditStatus() != 1 && data.getTotalReading() != 0){  //待审批状态,且读数发生改变
					trueCount ++ ;
					data.setReadingTime(new Date());
					//本次读数的表倍率
					double nowRate = getRate(data, projectId);
					
					if(data.getTotalReading() < CommonUtils.null2Double(data.getLastTotalReading())){
						data.setExStatus(MeterDataEnum.EX_STATUS_ERROR.getIntValue());
						data.setRemark("本月读数少于上月读数"+ ((null != data.getRemark()) ? ": "+data.getRemark():""));
					}else{
						//计算本次用量
						if(1 == data.getIsReplaced()){
							//若已经换过表 ,判断当前传入的数值,与数据库内的数值大小
							TcMeterData beforeData = this.tcMeterDataMapper.getDataById(data.getId()); //当前数据之前的状态
							
							//获取当时的倍率
							double rate = getRate(beforeData, projectId);
							
							//当上次读数为空,即尚未读数时, 初始化的用量即为原用量  读数需要乘以表倍率 rate
							double initUseCount = beforeData.getUseCount();
							if(0 != beforeData.getTotalReading()){
								initUseCount -= ((beforeData.getTotalReading() - beforeData.getLastTotalReading()) * rate);
							}
							/*double initUseCount = (0 == beforeData.getTotalReading()) ? 
					    					beforeData.getUseCount() : beforeData.getUseCount() - (beforeData.getTotalReading() - beforeData.getLastTotalReading());*/
							
							data.setUseCount(initUseCount + ((data.getTotalReading() - data.getLastTotalReading()) * nowRate));
							if(initUseCount > data.getUseCount()){
								continue;
							}
						}else{
							data.setUseCount((data.getTotalReading() - data.getLastTotalReading()) * nowRate);
						}
						data.setExStatus(MeterDataEnum.EX_STATUS_COMMON.getIntValue());
						//TODO 推送至Siebel
						
						
						
					}
					this.tcMeterDataMapper.modify(data);
				}
			}
			
			//修改该任务的读数
			Map<String,Long> countMap = this.tcReadingTaskMapper.getReadedMetersCount(task.getId());
			int readedCount = (CommonUtils.isEmpty(countMap)) ? 0 : countMap.get("COUNT").intValue();
			task.setCompleteMeterCount(readedCount);
			task.setRemainMeterCount(task.getTotalMeterCount() - readedCount);
			this.tcReadingTaskMapper.update(task);
			
			int count = datas.size()-trueCount;
			msgMap = new MessageMap();
			if(count<datas.size() && count>0){ //部分成功
				msgMap.setFlag(MessageMap.INFOR_WARNING);
				msgMap.setMessage("部分修改成功!成功了["+trueCount+"]条;失败了["+count+"]条");
			}
			if(count==0){ //全部成功
				msgMap.setFlag(MessageMap.INFOR_SUCCESS);
				msgMap.setMessage("全部修改成功!成功了["+trueCount+"]条!");
			}
			if(count==datas.size()){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("全部修改失败!失败了["+count+"]条!");
			}
			
		}
		return new BaseDto(msgMap);
	}
	
	private double getRate(TcMeterData data , String projectId){
		double rate = 1;
		if(1 == data.getMeterType()){
			ElectMeter meter = this.tcElectMeterMapper.getElectMeterByCode(data.getMeterCode(), projectId);
			rate = (null == meter) ? 1 : meter.getRate();
		}else{
			TcWaterMeter meter = this.tcWaterMeterMapper.getWaterMeterByCode(data.getMeterCode(), projectId);
			rate = (null == meter) ? 1 : meter.getRate();
		}
		return rate;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageDatasToAudit(String companyId,TcMeterData data) {
		List<Map<String,Object>> datas =new ArrayList<>();
		if( 1== data.getMeterType() ){
			datas = this.tcMeterDataMapper.listPageDatasToAuditForEle(data);
		}else{
			datas = this.tcMeterDataMapper.listPageDatasToAudit(data);
		}
		return new BaseDto(datas, data.getPage());
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto batchAudit(String companyId, List<TcMeterData> datas,Integer auditStatus) {
		String messageFlag = null;
		String content = null;
		if(CommonUtils.isEmpty(datas)){
			messageFlag = MessageMap.INFOR_WARNING;
			content = "参数为空,无法审核.";
		}else{
			
			long currMills = System.currentTimeMillis();
			
			//找出任务的审核时间
			TcReadingTask task = this.tcReadingTaskMapper.getWaterTaskDetailById(datas.get(0).getTaskId());
			if(currMills < task.getAuditStartTime().getTime() 
					|| currMills > task.getAuditEndTime().getTime()){
				messageFlag = MessageMap.INFOR_WARNING;
				content = "当前处于非审核时间段,无法审核.";
			}else{
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("beforeTaskId", datas.get(0).getBeforeTaskId());
				paramMap.put("datas", datas);
				List<String> canAuditIds = this.tcMeterDataMapper.getCanAuditMeterDataByids(paramMap);
				if(!canAuditIds.isEmpty()){
					paramMap.clear();
					paramMap.put("ids", canAuditIds);
					paramMap.put("auditStatus", auditStatus);
					this.tcMeterDataMapper.batchAudit(paramMap);
				}
				
				Integer auditedCount = this.tcMeterDataMapper.getAuditedDatasCountByTaskId(task.getId());
				if(auditedCount == task.getTotalMeterCount()){
					TcReadingTask t = new TcReadingTask();
					t.setId(task.getId());
					t.setAuditStatus(1);
					this.tcReadingTaskMapper.update(t);
				}
				
				messageFlag = MessageMap.INFOR_SUCCESS;
				if(canAuditIds.isEmpty() || canAuditIds.size() < datas.size()){
					content = "有部分数据未抄表,或者是抄表错误,无法审核,其他数据审核完成.";
				}else{
					content = "审核完成.";
				}
			}
		}
		return new BaseDto(new MessageMap(messageFlag, content));
	}

	



	

	/**
	 * @describe 查询水表的抄表历史记录数据信息
	 * @return BaseDto抄表历史记录 
	 * 
	 * 1.只有一次抄表记录的 --上次读数为表的initamount
	 * 2.多次抄表记录的   上次读数为  最近一次抄表的 读数
	 */ 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto<String, Object> listPageReadingRecords(String companyId, TcMeterData data){
		List<Map<String, Object>> readingDataRecords=new ArrayList<>();
		readingDataRecords=tcMeterDataMapper.listPageReadingRecords(data);
		return new BaseDto(readingDataRecords,data.getPage());
	}
	



	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto modifyReading(String companyId, TcMeterData data) {
		
		MessageMap msgMap;
		if(data == null){
			msgMap = new MessageMap(MessageMap.INFOR_WARNING, "传入参数为空");
		}else{
			TcMeterData dataObj = this.tcMeterDataMapper.getDataById(data.getId());
			if(dataObj.getAuditStatus() == 1){
				msgMap = new MessageMap(MessageMap.INFOR_WARNING,"该数据已经审核,无法修改读数");
			}else{
				String str;
				data.setExStatus(1);	//设置成正常状态,这里只要修正了就需要进行状态修改，否则会影响到后面的对异常数据的审核
				if(data.getTotalReading() >= data.getLastTotalReading()){  //TODO 后期需要对异常状态 同比环比进行判断
					str = "修改成功!";
					TcReadingTask task = this.tcReadingTaskMapper.getWaterTaskDetailById(data.getTaskId());
					task.setCompleteMeterCount(task.getCompleteMeterCount() + 1);
					task.setRemainMeterCount(task.getRemainMeterCount() - 1);
					this.tcReadingTaskMapper.update(task);
				}else{
					str = "读数修改成功,但本次读数依旧少于上月读数,处于错误状态";
				}
				this.tcMeterDataMapper.modify(data);
				msgMap = new MessageMap(null,str);
			}
		}
		return new BaseDto(msgMap);
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto modifyReadingNew(String companyId, TcMeterData data) {
		//异常列表进行修改的时候新增了这个，考虑到前面修改的方法不确定是否可以修改，这里新增一个
		MessageMap msgMap;
		if(data == null){
			msgMap = new MessageMap(MessageMap.INFOR_WARNING, "传入参数为空");
		}else{
			TcMeterData dataObj = this.tcMeterDataMapper.getDataById(data.getId());
			if(dataObj.getAuditStatus() == 1){
				msgMap = new MessageMap(MessageMap.INFOR_WARNING,"该数据已经审核,无法修改读数");
			}else{
				String str;
				data.setExStatus(1);	//设置成正常状态,这里只要修正了就需要进行状态修改，否则会影响到后面的对异常数据的审核
				if(data.getTotalReading() >= data.getLastTotalReading()){  //TODO 后期需要对异常状态 同比环比进行判断
					str = "修改成功!";
				}else{
					str = "读数修改成功,但本次读数依旧少于上月读数,处于错误状态";
				}
				this.tcMeterDataMapper.modify(data);
				msgMap = new MessageMap(null,str);
			}
		}
		return new BaseDto(msgMap);
	}


	public MessageMap electMeterReadingImport(WyBusinessContext ctx,List<TcMeterData> tcElectMeterReadinglist,String taskId){
		MessageMap messageMap = new MessageMap();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		List<TcMeterData> newUpdate = new ArrayList<TcMeterData>();
		StringBuffer error = new StringBuffer();
		try {
			paramMap.put("taskId", taskId);
			paramMap.put("meterType", MeterEnum.RECORD_METERTYPE_ELECT.getIntValue());
			if(tcElectMeterReadinglist.size()>500){
				int num=tcElectMeterReadinglist.size()/500;
				for(int i=0;i<num;i++){
					//分批查询出数据信息
					List<TcMeterData> bachList=new ArrayList<>();
					if(i==num){
						paramMap.put("list", tcElectMeterReadinglist.subList(i*500,tcElectMeterReadinglist.size() ));//一次500条
					}else{
						paramMap.put("list", tcElectMeterReadinglist.subList(i*500, (i+1)*500));//一次500条
					}
					bachList=tcMeterDataMapper.getMeterDataForImport(paramMap);//完整的抄表数据
					newUpdate.addAll(bachList);
				}
			}else{
				paramMap.put("list", tcElectMeterReadinglist);
				newUpdate=tcMeterDataMapper.getMeterDataForImport(paramMap);//完整的抄表数据
			}
			String  remark="";
			for (TcMeterData tcMeterData : newUpdate) {
				for (TcMeterData tcM : tcElectMeterReadinglist) {
					//水表编号相同的获取excel中最新的本次读数
					if(tcM.getMeterCode().equals(tcMeterData.getMeterCode())){
						remark = tcM.getRemark();
						if(CommonUtils.isNotEmpty(remark)&&remark.length()>100){ //备注只能存放
							error.append("表编号为:["+tcM.getMeterCode()+"]的备注最多只能填写100个字符");
							break;
						}
						tcMeterData.setTotalReading(tcM.getTotalReading());
						tcMeterData.setPeakReading(tcM.getPeakReading());
						tcMeterData.setVallyReading(tcM.getVallyReading());
						tcMeterData.setCommonReading(tcM.getCommonReading());
						tcMeterData.setRemark(remark);
						break;
					}
				}
			}
			int badInfo=tcElectMeterReadinglist.size()-newUpdate.size();//未能在数据库查询到的数据条数
	        BaseDto baseDto= batchModify(null,newUpdate,"1");
			if(badInfo==0){//
				messageMap.setFlag(baseDto.getMessageMap().getFlag());
				messageMap.setMessage(baseDto.getMessageMap().getMessage());
				if(baseDto.getMessageMap().getFlag().equals(MessageMap.INFOR_SUCCESS)){
					messageMap.setObj(ImportExportEnum.succeed.name());
				}
				if(baseDto.getMessageMap().getFlag().equals(MessageMap.INFOR_WARNING)){
					messageMap.setObj(ImportExportEnum.partial_success.name());
				}
				if(baseDto.getMessageMap().getFlag().equals(MessageMap.INFOR_ERROR)){
					messageMap.setObj(ImportExportEnum.failed.name());
				}
				
			}
			if(newUpdate.size()==0){ //全部失败
				messageMap.setFlag(MessageMap.INFOR_ERROR);
				messageMap.setMessage("导入失败!错误:可能所有导入的数据不存在,可能任务还没有产生;请耐心等待或者["+error.toString()+"]失败了["+tcElectMeterReadinglist.size()+"]条");
				messageMap.setObj(ImportExportEnum.failed.name());
			}
			if(newUpdate.size()>0 && newUpdate.size()<tcElectMeterReadinglist.size()){//部分成功
				messageMap.setFlag(MessageMap.INFOR_WARNING);
				messageMap.setMessage("导入部分成功;有部分数据不存在，可能已经被删除或者["+error.toString()+"]成功了["+newUpdate.size()+"]条;失败了["+(tcElectMeterReadinglist.size()-newUpdate.size())+"]条");
				
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			messageMap.setFlag(MessageMap.INFOR_ERROR);
			messageMap.setMessage("导入失败");
		}
		return messageMap;
	}
	

	
	/**
	 * 抄表结果数据的导入主要是做update操作
	 * 注意 问题：1.在未被审核前可填写和修改抄表结果   状态为：0
	 * 		
	 * @param tcWaterMeterDatalist
	 * @return
	 */
	public MessageMap importWaterMeterReading(WyBusinessContext ctx,List<TcMeterData> tcWaterMeterDatalist,String taskId){
		
		if(CommonUtils.isEmpty(tcWaterMeterDatalist)){
			return new MessageMap(MessageMap.INFOR_ERROR,"导入参数信息为空");
		}
		
		List<TcMeterData> dataList=new ArrayList<>();
		Map<String, Object> paraMap=new HashMap<>();
		paraMap.put("taskId", taskId);
		if(tcWaterMeterDatalist.size()>500){
			int num=tcWaterMeterDatalist.size()/500;
			for(int i=0;i<num;i++){
				//分批查询出数据信息
				List<TcMeterData> bachList=new ArrayList<>();
				if(i==num){
					paraMap.put("list", tcWaterMeterDatalist.subList(i*500,tcWaterMeterDatalist.size() ));//一次500条
				}else{
					paraMap.put("list", tcWaterMeterDatalist.subList(i*500, (i+1)*500));//一次500条
				}
				bachList=tcMeterDataMapper.getMeterDataForImport(paraMap);//完整的抄表数据（未审核）
				dataList.addAll(bachList);
			}
		}else{
			paraMap.put("list", tcWaterMeterDatalist);
			dataList=tcMeterDataMapper.getMeterDataForImport(paraMap);//完整的抄表数据
		}
		
		for (TcMeterData tcMeterData : dataList) {
			//单条处理
			for (TcMeterData tcM : tcWaterMeterDatalist) {
				//水表编号相同的获取excel中最新的本次读数
				if(tcM.getMeterCode().equals(tcMeterData.getMeterCode())){
					tcMeterData.setTotalReading(tcM.getTotalReading());
					break;
				}
			}
			
		}
		
		TcReadingTask task = this.tcReadingTaskMapper.getWaterTaskDetailById(taskId);	//获取此次任务对象
		//tcWaterMeterDatalist  导入excel中存在的抄表数据信息     dataList 查询出符合导入批量修改读数的数据信息
		if(CommonUtils.isEmpty(dataList)){
			return new MessageMap(MessageMap.INFOR_ERROR,"导入数据不符合要求");
		}else{
			int trueCount = 0;
			boolean flag = true;
			TcReadingSchedule schedule = this.tcReadingScheduleMapper.getScheduleById(task.getScheduleId());
			String projectId = (null == schedule) ? "" : schedule.getProjectId();
			for(TcMeterData data : dataList){
				if(CommonUtils.isEmpty(data.getTotalReading())){
					continue;
				}
				if(data.getTotalReading() != 0){  //待审批状态,且读数发生改变
					trueCount ++ ;
					data.setReadingTime(new Date());
					//本次读数的表倍率
					double nowRate = getRate(data, projectId);
					
					if(data.getTotalReading() < CommonUtils.null2Double(data.getLastTotalReading())){
						data.setExStatus(MeterDataEnum.EX_STATUS_ERROR.getIntValue());
						data.setRemark("本月读数少于上月读数"+ ((null != data.getRemark()) ? ": "+data.getRemark():""));
					}else{
						//计算本次用量
						if(1 == data.getIsReplaced()){
							//若已经换过表 ,判断当前传入的数值,与数据库内的数值大小
							TcMeterData beforeData = this.tcMeterDataMapper.getDataById(data.getId()); //当前数据之前的状态
							
							//获取当时的倍率
							double rate = getRate(beforeData, projectId);
							
							//当上次读数为空,即尚未读数时, 初始化的用量即为原用量  读数需要乘以表倍率 rate
							double initUseCount = beforeData.getUseCount();
							if(0 != beforeData.getTotalReading()){
								initUseCount -= ((beforeData.getTotalReading() - beforeData.getLastTotalReading()) * rate);
							}
							
							data.setUseCount(initUseCount + ((data.getTotalReading() - data.getLastTotalReading()) * nowRate));
							if(initUseCount > data.getUseCount()){
								continue;
							}
						}else{
							data.setUseCount((data.getTotalReading() - data.getLastTotalReading()) * nowRate);
						}
						data.setExStatus(MeterDataEnum.EX_STATUS_COMMON.getIntValue());
					}
					this.tcMeterDataMapper.modify(data);
				}
			}
		}
		
		//修改该任务的读数
		Map<String,Long> countMap = this.tcReadingTaskMapper.getReadedMetersCount(task.getId());
		int readedCount = (CommonUtils.isEmpty(countMap)) ? 0 : countMap.get("COUNT").intValue();
		task.setCompleteMeterCount(readedCount);
		task.setRemainMeterCount(task.getTotalMeterCount() - readedCount);
		this.tcReadingTaskMapper.update(task);
		
		int badInfo=tcWaterMeterDatalist.size()-dataList.size();//未能在数据库查询到的数据条数
		
//		BaseDto baseDto= batchModify(null,dataList,"0");
		
		MessageMap msg=new MessageMap();
		StringBuilder info=new StringBuilder();
		if( badInfo>0 ){
			info.append("有"+badInfo+"条导入的数据不符合要求，水表编号找不到");
		}
		msg.setMessage("倒入操作成功！"+info.toString());
		
		return msg;
	}
	
	private TcHydroMeterOperationRecord checkChange(String electCode,
			Date startTime, Date endTime) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("code", electCode);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		paramMap.put("operationType",
				MeterEnum.RECORD_OPEARTETYPE_CHANGE.getIntValue());
		paramMap.put("meterType",
				MeterEnum.RECORD_METERTYPE_ELECT.getIntValue());
		TcHydroMeterOperationRecord tcChange = tcWaterMeterOperRecordMapper
				.getInfoByCodeAndTime(paramMap);
		return tcChange;
	}

	@Override
	public BaseDto listPageHistories(WyBusinessContext ctx,
			TcMeterData tcMeterData) {
		if(CommonUtils.isEmpty(tcMeterData)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "传入参数为空"));
		}
		BaseDto returnDto = new BaseDto();
		List<TcMeterData> datas = this.tcMeterDataMapper.listPageHistories(tcMeterData);
		returnDto.setLstDto(datas);
		returnDto.setPage(tcMeterData.getPage());
		return returnDto;
	}

	@Override
	public BaseDto findByBuildingCode(String companyId, String code) {
		BaseDto returnDto = new BaseDto();
		//判断当前房屋是否已存在抄过的数据,且任务已完成
		int count = this.tcReadingTaskMapper.findCompletedTaskByBuildingCode(code);
		if(count == 0){
			returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_ERROR, "当前房屋尚未抄过表,无法投诉,请核实."));
			return returnDto;
		}
		
		
		//先判断当前建筑是否含有已经存在且未完成的投诉,若有,则直接页面进行提示
		TcOrderComplaint obj = new TcOrderComplaint();
		obj.setBuildingCode(code);
		obj.setOrderStatus(1);
		obj.setOrderType(0);		//水电抄表
		obj.setOrderTypeOne(0);		//水表
		List<TcOrderComplaint> objs = this.tcOrderComplaintMapper.findByObj(obj);
		if(!objs.isEmpty()){
			returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_ERROR,"当前房屋已存在未完成的水表抄表投诉,无法再次发起水表抄表."));
			return returnDto;
		}
		//根据buildingCode获取到建筑绑定的水表的上一次完成已抄数据.
		returnDto.setObj(this.tcMeterDataMapper.findByBuildingCode(code));
		return returnDto;
	}
	@Override
	public BaseDto selectAbnormalData(String companyId, TcMeterData meterData) {
		if(CommonUtils.isEmpty(meterData)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "表类型不可为空"));
		}
		BaseDto returnDto = new BaseDto();
		List<Map<String, String>> datas = this.tcMeterDataMapper.listPageAbnormalData(meterData);
		returnDto.setLstDto(datas);
		returnDto.setPage(meterData.getPage());
		return returnDto;
	}

   public BaseDto selectAbnormalElectData(String companyId,TcMeterData meterData){
	   BaseDto returnDto = new BaseDto();
		List<Map<String, String>> datas = this.tcMeterDataMapper.listPageAbnormalElectData(meterData);
		returnDto.setLstDto(datas);
		returnDto.setPage(meterData.getPage());
		return returnDto;
   }

    @Override
	public BaseDto getReadingInfoByYear(String companyId, Map<String, String> paramMap) {
		//查询两种数据 1 我的抄表 (包含 抄表总数量  所有已抄表，所有未抄表 )
		//2 所有抄表  （包含  所有抄表，所有已抄表，所有未抄表）
		MessageMap msg;
		if(CommonUtils.isNotEmpty(paramMap.get("readingPerson"))){
			//我的抄表 (包含 抄表总数量  所有已抄表，所有未抄表 ),结果集以map存放
			Map<String,Map<String,String>> resultMap=new HashMap<>();
			//查询抄表总数量
			paramMap.put("dataType", "3");
			Map<String, String> allData=tcMeterDataMapper.getReadingInfoByYear(paramMap);
			paramMap.put("dataType", "1");
			Map<String, String> readingData=tcMeterDataMapper.getReadingInfoByYear(paramMap);
			paramMap.put("dataType", "2");
			Map<String, String> notReading=tcMeterDataMapper.getReadingInfoByYear(paramMap);
			resultMap.put("allData", allData);
			resultMap.put("readingData", readingData);
			resultMap.put("notReading", notReading);
			return new BaseDto<>(resultMap);
			
		}else{
			msg=new MessageMap(MessageMap.INFOR_ERROR,"请求参数不符合要求");
			return new BaseDto<>(msg);
		}
	}

	@Override
	public BaseDto listPageReadingInfoByYear(String companyId, TcMeterData tcMeterData,int serviceType) {
		//查询两种数据 1 我的抄表 (包含 抄表总数量  所有已抄表，所有未抄表 )
				
				MessageMap msg;
				BaseDto baseDto=new BaseDto();
				if( CommonUtils.isNotEmpty(tcMeterData.getReadingPersonId()) &&  MeterEnum.SERVICE_TYPE_MY.getIntValue() == serviceType ){
					//我的抄表 (包含 抄表总数量  所有已抄表，所有未抄表 ),结果集以map存放
					Map<String,Map<String,String>> resultMap=new HashMap<>();
					//查询抄表总数量
					tcMeterData.setSearchCode("all");
					List<Map<String, String>> allList = tcMeterDataMapper.listPageReadingInfoByYear(tcMeterData);
					Map<String, String> allData=new HashMap();
					if(CommonUtils.isNotEmpty(allList)){
						allData=allList.get(0);
						resultMap.put("allData", allData);
					}else{
						msg=new MessageMap(MessageMap.INFOR_WARNING,"暂无数据");
						baseDto.setMessageMap(msg);
						return baseDto;
					}
					//查询已抄表量
					tcMeterData.setSearchCode("reading");
					Map<String, String> readingData=new HashMap<>();
					List<Map<String, String>> readingL=	tcMeterDataMapper.listPageReadingInfoByYear(tcMeterData);
					if(CommonUtils.isNotEmpty(readingL)){
						readingData=readingL.get(0);
						resultMap.put("readingData", readingData);
					}
					//查询未抄表量
					tcMeterData.setSearchCode("notR");
					Map<String, String> notReading=new HashMap<>();
					List<Map<String, String>> notReadingL=tcMeterDataMapper.listPageReadingInfoByYear(tcMeterData);
					if(CommonUtils.isNotEmpty(notReadingL)){
						notReading=notReadingL.get(0);
						resultMap.put("notReading", notReading);
					}
					msg=new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功");
					baseDto.setMessageMap(msg);
					baseDto.setObj(resultMap);
					return baseDto;
				}else if( MeterEnum.SERVICE_TYPE_ALL.getIntValue() == serviceType ) {
					//2 所有抄表  （包含  所有抄表，所有已抄表，所有未抄表）--一抄表员展现
//					List<Map<String, String>> resultList=new ArrayList<>();
					tcMeterData.setSearchCode("all");
					List<Map<String, String>> allList=tcMeterDataMapper.listPageReadingInfoByYear(tcMeterData);
					if(CommonUtils.isEmpty(allList)){
						//所有数据都没有查询到结果
						msg=new MessageMap(MessageMap.INFOR_WARNING,"暂无数据");
						baseDto.setMessageMap(msg);
						return baseDto;
					}
					tcMeterData.setSearchCode("reading");
					List<Map<String, String>> readingList=tcMeterDataMapper.listPageReadingInfoByYear(tcMeterData);
//					tcMeterData.setSearchCode("2");
//					List<Map<String, String>> notReading=tcMeterDataMapper.listPageReadingInfoByYear(tcMeterData);
					//分别查询出了按照抄表人统计的所有抄表信息，分为所有抄表，已抄表
					for (Map<String, String> map : allList) {
						if(CommonUtils.isNotEmpty(readingList)){
							for (Map<String, String> readingM : readingList) {
								if(map.get("readingPerson").equals(readingM.get("readingPerson"))){
									//同一个人获取其全部抄表和已抄表
									map.put("rJanuary",String.valueOf(readingM.get("January")));
									map.put("rFebruary",String.valueOf(readingM.get("February")));
									map.put("rMarch",String.valueOf(readingM.get("March")));
									map.put("rApril",String.valueOf(readingM.get("April")));
									map.put("rMay",String.valueOf(readingM.get("May")));
									map.put("rJune",String.valueOf(readingM.get("June")));
									map.put("rJuly",String.valueOf(readingM.get("July")));
									map.put("rAugust",String.valueOf(readingM.get("August")));
									map.put("rSeptember",String.valueOf(readingM.get("September")));
									map.put("rOctober",String.valueOf(readingM.get("October")));
									map.put("rNovember",String.valueOf(readingM.get("November")));
									map.put("rDecember",String.valueOf(readingM.get("December")));
									break;
								}
								//走到了这里说明所有数据非空，已抄表数据非空，但这个抄表人的已抄表数据信息没有
								map.put("rJanuary","0");
								map.put("rFebruary","0");
								map.put("rMarch","0");
								map.put("rApril","0");
								map.put("rMay","0");
								map.put("rJune","0");
								map.put("rJuly","0");
								map.put("rAugust","0");
								map.put("rSeptember","0");
								map.put("rOctober","0");
								map.put("rNovember","0");
								map.put("rDecember","0");
							}
						}else{
							//走到了这里说明所有数据空，已抄表数据非空，但这个抄表人的已抄表数据信息没有
							map.put("rJanuary","0");
							map.put("rFebruary","0");
							map.put("rMarch","0");
							map.put("rApril","0");
							map.put("rMay","0");
							map.put("rJune","0");
							map.put("rJuly","0");
							map.put("rAugust","0");
							map.put("rSeptember","0");
							map.put("rOctober","0");
							map.put("rNovember","0");
							map.put("rDecember","0");
						}
					}
					msg=new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功");
					baseDto.setMessageMap(msg);
					baseDto.setPage(tcMeterData.getPage());
					baseDto.setObj(allList);
					return baseDto;
				}else{
					msg=new MessageMap(MessageMap.INFOR_ERROR,"请求参数不符合要求");
					return new BaseDto<>(msg);
				}
	}

	/**
	 * 页面上,根据任务id以及建筑id获取对应的抄表数据以及房屋全名
	 * @param companyId
	 * @param code
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto findByTaskIdAndBuildingCode(String companyId, String code,String taskId) {
		Map<String,Object> resultMap = this.tcMeterDataMapper.findByTaskIdAndBuildingCode(code,taskId);
		BaseDto returnDto = new BaseDto();
		returnDto.setMessageMap(new MessageMap(null,"查询成功"));
		returnDto.setObj(resultMap);
		return returnDto;
	}



    @Override
    public List<TcMeterData> queryByTaskIds(String companyId, List<String> taskIds,int meterType) {
        return tcMeterDataMapper.queryByTaskIds(taskIds,meterType);
    }

    @Override
    public List<TcMeterData> queryByTaskId(String companyId, String taskId,int meterType) {
        return tcMeterDataMapper.queryByTaskId(taskId,meterType);
    }

    @Override
    public List<TcMeterData> queryByDescription(String companyId, String description, int meterType) {
        return tcMeterDataMapper.queryByDescription(description,meterType);
    }

    @Override
    public List<TcMeterData> queryYearData(String companyId, String meterCode, int meterType, String year) {
        return tcMeterDataMapper.queryYearData(meterCode,meterType,year);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public int modifyWMeterData(String companyId, String taskId, String accountId,String meterCode, String totalReading, String fileId) {
        TcReadingTask task = tcReadingTaskMapper.getWaterTaskDetailById(taskId);
        Date startTime = task.getStartTime();
        Date endTime = task.getEndTime();
        //判断上传时间是否在任务起止时间内
        int count = 0;
        if(startTime.getTime()<= System.currentTimeMillis()
                && System.currentTimeMillis() < endTime.getTime()) {
            TcMeterData tcMeterData = tcMeterDataMapper.queryByMeterCodeAndTaskId(taskId,meterCode);
            TcWaterMeter tcWaterMeter = tcWaterMeterMapper.getWaterMeterByCode(tcMeterData.getMeterCode(),tcMeterData.getProjectId());

            //水表用量 = (本次读数 - 上次读数) * 倍率
            BigDecimal bdRate =  new BigDecimal(tcWaterMeter.getRate());
            BigDecimal bdTotalReading = new BigDecimal(Double.toString(Double.parseDouble(totalReading)));
            BigDecimal bdLastTotalReading = new BigDecimal(tcMeterData.getLastTotalReading());
            double useCount = (bdTotalReading.subtract(bdLastTotalReading)).multiply(bdRate).doubleValue();

            //更新抄表数据和任务抄表数量
            tcMeterDataMapper.updateWMeterData(taskId,accountId,meterCode,tcMeterData.getReadingTime(),Double.parseDouble(totalReading),fileId,useCount);
            tcReadingTaskMapper.updateTaskCount(task.getId());
        }else {
            throw new ECPBusinessException(ReturnCode.API_WY_METER_DATE_OUT_OF_RANGE);
        }
        return count;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public int modifyEMeterData(String companyId, String taskId, String accountId, String meterCode, String totalReading, String peakReading, String vallyReading, String commonReading, String fileId) {
        TcReadingTask task = tcReadingTaskMapper.getWaterTaskDetailById(taskId);
        Date startTime = task.getStartTime();
        Date endTime = task.getEndTime();
        //判断上传时间是否在任务起止时间内
        int count = 0;
        if(startTime.getTime()<= System.currentTimeMillis()
                && System.currentTimeMillis() < endTime.getTime()) {
            TcMeterData tcMeterData = tcMeterDataMapper.queryByMeterCodeAndTaskId(taskId,meterCode);
            ElectMeter electMeter = tcElectMeterMapper.getElectMeterByCode(tcMeterData.getMeterCode(),tcMeterData.getProjectId());

            //电表用量 = (本次读数 - 上次读数) * 倍率
            //电表峰值用量 = (峰值读数 - 上次峰值读数) * 倍率
            //电表谷值用量 = (谷值读数 - 上次谷值读数) * 倍率
            //电表平值用量 = (峰值读数 - 上次平值读数) * 倍率
            BigDecimal bdRate =  new BigDecimal(electMeter.getRate());

            BigDecimal bdTotalReading = new BigDecimal(Double.toString(Double.parseDouble(totalReading)));
            BigDecimal bdLastTotalReading = new BigDecimal(tcMeterData.getLastTotalReading());
            double useCount = (bdTotalReading.subtract(bdLastTotalReading)).multiply(bdRate).doubleValue();

            BigDecimal bdPeakReadingReading = new BigDecimal(Double.toString(Double.parseDouble(peakReading)));
            BigDecimal bdLastPeakReading = new BigDecimal(tcMeterData.getLastPeakReading());
            double peakCount = (bdPeakReadingReading.subtract(bdLastPeakReading)).multiply(bdRate).doubleValue();

            BigDecimal bdVallyReadingReading = new BigDecimal(Double.toString(Double.parseDouble(vallyReading)));
            BigDecimal bdLastVallyReading = new BigDecimal(tcMeterData.getLastVallyReading());
            double vallyCount = (bdVallyReadingReading.subtract(bdLastVallyReading)).multiply(bdRate).doubleValue();

            BigDecimal bdCommonReadingReading = new BigDecimal(Double.toString(Double.parseDouble(commonReading)));
            BigDecimal bdLastCommonReading = new BigDecimal(tcMeterData.getLastCommonReading());
            double commonCount = (bdCommonReadingReading.subtract(bdLastCommonReading)).multiply(bdRate).doubleValue();

            tcMeterDataMapper.updateEMeterData(taskId,accountId,meterCode,tcMeterData.getReadingTime(),Double.parseDouble(totalReading),
                    Double.parseDouble(peakReading),Double.parseDouble(vallyReading),Double.parseDouble(commonReading),fileId,useCount,peakCount,vallyCount,commonCount);
            tcReadingTaskMapper.updateTaskCount(task.getId());
        }else {
            throw new ECPBusinessException(ReturnCode.API_WY_METER_DATE_OUT_OF_RANGE);
        }
        return count;
    }
    
    
    
    private boolean reading2Sieble(TcMeterData thisData, Integer frequency){
    	TcMeterData paramData = new TcMeterData();
    	paramData.setAuditStatus(MeterDataEnum.STATUS_AUDIT_COMPLETE.getIntValue());
    	paramData.setProjectId(thisData.getProjectId());
    	paramData.setMeterCode(thisData.getMeterCode());
    	paramData.setMeterType(thisData.getMeterType());
    	paramData.setExStatus(MeterDataEnum.EX_STATUS_COMMON.getIntValue());
    	paramData.setReadingTime(thisData.getReadingTime());
    	paramData.setFrequency(frequency);
    	paramData.setIsUsed(MeterDataEnum.IS_USED_YES.getIntValue());
    	TcMeterData lastData = this.tcMeterDataMapper.findLastData(paramData);
    	
    	String assetNo = this.tcWaterMeterMapper.findAssetNoByTypeAndMeterCode(thisData.getMeterType(), thisData.getMeterCode(), thisData.getProjectId()); 
    	if(CommonUtils.isEmpty(assetNo)){
    		return true;
    	}
    	return true;
    }

	@Override
	public void push2Siebel(String companyId, List<String> ids , Integer frequence){
		log.info("接受物业平台数据,并推送至siebel, data={}",ids);
		for(String id : ids){
			TcMeterData data = this.tcMeterDataMapper.getDataById(id);
			boolean flag = reading2Sieble(data, frequence);
			if(!flag){
				log.warn("{}数据推送至Siebel >>> 失败, 数据: {}. ", (data.getMeterType() == MeterEnum.RECORD_METERTYPE_ELECT.getIntValue()) ? "电表":"水表", data.toString());
			}else{
				log.info("{}数据推送至Siebel >>> 成功, 数据: {}. ", (data.getMeterType() == MeterEnum.RECORD_METERTYPE_ELECT.getIntValue()) ? "电表":"水表", data.toString());
			}
		}
	}
	
	
	/**
	 * 把表数据进行核对，加入异常类型
	 */
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto checkMeterData (String companyId, TcMeterData t) {
		
		MessageMap msgMap;
		if ( t.getProjectId() ==null &&  t.getMeterType() == null ) {
			
			msgMap = new MessageMap (MessageMap.INFOR_WARNING, "传入参数为空.核对失败");
			
			return new BaseDto(msgMap);
		
		}
		//找出符合条件的元数据
		List<String> ids = this.tcMeterDataMapper.getCanCheckDatas(t);
		if(CommonUtils.isEmpty(ids)){
			msgMap = new MessageMap (MessageMap.INFOR_WARNING,"没有需要核对的数据");
			return  new BaseDto(msgMap);
		}
		//批量更新，把ex_status设置为异常状态
		this.tcMeterDataMapper.batchUpdateDatas(ids);
		msgMap = new MessageMap (MessageMap.INFOR_SUCCESS,"核对成功");
		return new BaseDto(msgMap);
		
	}
	
	
	/**
	 * 修改每个表的最终读数和上个月的最终读数
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto modifyTotalReadingById(String companyId, TcMeterData data) {
		
		MessageMap messMap;
	
		if(CommonUtils.isEmpty(data.getTotalReading()) || CommonUtils.isEmpty(data.getLastTotalReading()) || CommonUtils.isEmpty(data.getRemark())){
			
			messMap =  new MessageMap(MessageMap.INFOR_WARNING,"传入的参数为空");
			
			return new BaseDto(messMap);
	
		}
		//根据id找出表数据
		TcMeterData tcMeterData = tcMeterDataMapper.getMeterDataById(data.getId());
		double totalReading = data.getTotalReading();
		
		double lastTotalReading = data.getLastTotalReading();
		//获取表的倍率
		if(tcMeterData.getMeterType() == null || CommonUtils.isEmpty(tcMeterData.getProjectId())){
			
			messMap =  new MessageMap(MessageMap.INFOR_WARNING,"传入的参数为空");
			
			return new BaseDto(messMap);
		}
		double rate = getRate(tcMeterData,tcMeterData.getProjectId());
		//由于本月读数和上月读数已被修改，需要从新计算使用量
		double subData = BigDecimalUtils.sub(totalReading,lastTotalReading);
		
		double nowUseCount = BigDecimalUtils.mul ( subData, rate);
		
		
		if ( CommonUtils.null2Double(lastTotalReading) > totalReading){
			
			messMap =  new MessageMap(MessageMap.INFOR_WARNING,"本月读数小于上月读数");
			
			return new BaseDto(messMap);
		
		}else {
		
			//double lastUseCount = getLastUseCount(data);

			data.setUseCount(nowUseCount);
			data.setExStatus(1);
			int count = this.tcMeterDataMapper.modify(data);
			if(count<=0){
				messMap = new MessageMap(MessageMap.INFOR_ERROR,"修改失败");
			}
				
			messMap = new MessageMap(MessageMap.INFOR_SUCCESS,"修改成功");
	
		}
		
		return new BaseDto(messMap);
		
		
	}
	double getLastUseCount(TcMeterData tcMeterData){
		
		String lastTaskId = tcReadingTaskMapper.getlastTaskIdByTaskId(tcMeterData.getTaskId());
		
		double lastUseCount = tcMeterDataMapper.getLastMeterCount(tcMeterData.getMeterCode(), lastTaskId);
		return lastUseCount;


	}
	
	/**
	 * 根据任务id和表的编码获取此表的在上个任务的使用量
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto getLastTaskMterData(String companyId, TcMeterData data){
		
		MessageMap messMap;
		
		BaseDto returnDto = new BaseDto();
		
		if(data.getId()==null){
			
			messMap =  new MessageMap(MessageMap.INFOR_WARNING,"传入的参数为空");
			
			return new BaseDto(messMap);
	
		}
		TcMeterData tcMeterData = tcMeterDataMapper.getMeterDataById(data.getId());
		//获取此表在上个任务中使用量的方法，需要上次的任务id和表编号
		double lastUseCount = getLastUseCount(tcMeterData);
		
		returnDto.setObj(lastUseCount);
		
		returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS, "请求成功."));
		
		return returnDto;	
	}
	/**
	 * 根据年份和建筑编号查找相应的元数据信息
	 */
	 	@Override
	    public List<TcMeterData> queryMeterByYear(String companyId,String meterCode,int meterType, String year) {
	        return tcMeterDataMapper.queryMeterByYear(meterCode,meterType,year);
	    }
	 	
	 	
	 	@Override
	    public BaseDto modifyMeterDataStatus(String companyId,TcMeterData tcMeterData) {
	 		BaseDto returnDto = new BaseDto();
	 		MessageMap messMap;
	 		
	 		 tcMeterDataMapper.modifyMeterDataStatus(tcMeterData);
	 		returnDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"成功"));
	 		return returnDto;
	    }
	
}
