package com.everwing.coreservice.wy.core.service.impl.order;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.MeterDataEnum;
import com.everwing.coreservice.common.wy.common.enums.TcOrderComplaintAndCompleteEnum;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.meterrelation.TcMeterRelation;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.order.TcOrderChangeAssetComplaint;
import com.everwing.coreservice.common.wy.entity.order.TcOrderComplaint;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.service.order.TcOrderComplaintService;
import com.everwing.coreservice.wy.dao.mapper.business.electmeter.TcElectMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterrelation.TcMeterRelationMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingtask.TcReadingTaskMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.order.complaint.TcOrderComplaintMapper;
import com.everwing.coreservice.wy.dao.mapper.order.complete.TcOrderCompleteMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("tcOrderComplaintServiceImpl")
public class TcOrderComplaintServiceImpl implements TcOrderComplaintService{
	
	@Autowired
	private TcOrderComplaintMapper tcOrderComplaintMapper;
	
	@Autowired
	private TcMeterDataMapper tcMeterDataMapper;
	
	@Autowired
	private TcReadingTaskMapper tcReadingTaskMapper;
	
	@Autowired
	private TcMeterRelationMapper tcMeterRelationMapper;
	
	@Autowired
	private TcWaterMeterMapper tcWaterMeterMapper;
	
	@Autowired
	private TcElectMeterMapper tcElectMeterMapper;
	
	@Autowired
	private TcBuildingMapper tcBuildingMapper;
	
	@Autowired
	private TcOrderCompleteMapper tcOrderCompleteMapper;
	
	private static final Logger log = Logger.getLogger(TcOrderComplaintServiceImpl.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto listPage(String companyId,TcOrderComplaint entity) {
		return new BaseDto(this.tcOrderComplaintMapper.listPage(entity), entity.getPage());
	}
	
	/**
	 * 临时单个抄表任务
	 */
	@Transactional(rollbackFor=Exception.class)
	public BaseDto insertAssetsChange(WyBusinessContext ctx, TcOrderChangeAssetComplaint tcOrderChangeAssetComplaint) throws ECPBusinessException{
		MessageMap msgMap = new MessageMap();
		BaseDto baseDto = new BaseDto();
		try {
			if(CommonUtils.isEmpty(tcOrderChangeAssetComplaint)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("传入的参数为空!");
				baseDto.setMessageMap(msgMap);
				return baseDto;
			}
			//这里只纯粹的构建工单。 先查这个工单是否已经生成，一个收费对象一天一种类型只能产生一个资产变更的工单
			String createTime = new DateTime().toString("yyyy-MM-dd");
			int type = tcOrderChangeAssetComplaint.getType();
			String buildCode = tcOrderChangeAssetComplaint.getBuildCode();
			String projectId = tcOrderChangeAssetComplaint.getProjectId();
			TcOrderComplaint tcOrderComplaint = this.tcOrderComplaintMapper.findByTypeAndBuildCode(String.valueOf(type), buildCode, createTime, projectId);
			if(CommonUtils.isNotEmpty(tcOrderComplaint)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				if(type==0){
					msgMap.setMessage("该资产今天已经创建了水表抄表申请工单,不能再次创建!");
					
				}else if(type==1){
					msgMap.setMessage("该资产今天已经创建了电表抄表申请工单,不能再次创建!");
				}
			}else{//可以创建
				TcOrderComplaint newTcOrder = new TcOrderComplaint();
				newTcOrder.setCustName(tcOrderChangeAssetComplaint.getCustName());
				newTcOrder.setBuildingCode(tcOrderChangeAssetComplaint.getBuildCode());
				if(type==0){
					newTcOrder.setDescriber("产权变更水表抄表申请");
				}else if(type==1){
					newTcOrder.setDescriber("产权变更电表抄表申请");
				}
				newTcOrder.setOrderType(TcOrderComplaintAndCompleteEnum.ORDER_TYPE_CHANGE_ASSETS.getIntV());
				newTcOrder.setOrderTypeOne(type);
				newTcOrder.setOrderTypeTwo(TcOrderComplaintAndCompleteEnum.ORDER_TYPE_TWO_READING.getIntV());
				newTcOrder.setOrderSource(TcOrderComplaintAndCompleteEnum.ORDER_SOURCE_CUSTOMER_SERVICE.getIntV());
				newTcOrder.setIsVisit(TcOrderComplaintAndCompleteEnum.COMMON_NO.getIntV());
				newTcOrder.setOrderStatus(TcOrderComplaintAndCompleteEnum.ORDER_STATUS_NOACCEPT.getIntV());
				newTcOrder.setIsUrgent(TcOrderComplaintAndCompleteEnum.COMMON_YES.getIntV());
				newTcOrder.setChargePersonId(tcOrderChangeAssetComplaint.getReadingPersonId());
				newTcOrder.setCreateId(ctx.getUserId());
				newTcOrder.setCreateTime(new Date());
				newTcOrder.setProjectId(projectId);
				newTcOrder.setProjectName(tcOrderChangeAssetComplaint.getProjectName());
				
				
				//记录该资产对应表的位置
				TcMeterRelation tcMeterRelation = new TcMeterRelation();
				tcMeterRelation.setType(3);
				tcMeterRelation.setRelationId(newTcOrder.getId());
				
				if(type==0){
					List<TcWaterMeter> waterList= this.tcWaterMeterMapper.findByRelarionId(projectId, buildCode);
					if(CollectionUtils.isNotEmpty(waterList)){
						tcMeterRelation.setBuildingCode(waterList.get(0).getPosition());
					}else{
						msgMap.setFlag(MessageMap.INFOR_ERROR);
						msgMap.setMessage("资产编号["+buildCode+"]没有安装水表,请检查!");
						baseDto.setMessageMap(msgMap);
						return baseDto;
					}
				}else if(type==1){
					ElectMeter electMeter = this.tcElectMeterMapper.getElectMeterByReationId(projectId, buildCode);
					if(CommonUtils.isNotEmpty(electMeter)){
						tcMeterRelation.setBuildingCode(electMeter.getLocation());
					}else{
						msgMap.setFlag(MessageMap.INFOR_ERROR);
						msgMap.setMessage("资产编号["+buildCode+"]没有安装电表,请检查!");
						baseDto.setMessageMap(msgMap);
						return baseDto;
					}
				}
				this.tcMeterRelationMapper.add(tcMeterRelation);
				this.tcOrderComplaintMapper.insert(newTcOrder);
				msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			}
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			log.info(CommonUtils.log(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto insert(String companyId, TcOrderComplaint entity) throws ECPBusinessException{
		
		//这里加一个校验；如果一个重抄任务没有完成，该建筑不能再次建立重抄任务
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			//根据buildCode。ordertype=0,order_status状态不等于2 2表示已完成
			TcOrderComplaint  tcOrderComplaint = this.tcOrderComplaintMapper.findbyBuildCodeAndProjectId(entity.getProjectId(), entity.getBuildingCode());
			if(CommonUtils.isNotEmpty(tcOrderComplaint)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("客户["+entity.getCustName()+"]存在没有完成的重抄工单,不能再次做重抄任务!");
				baseDto.setMessageMap(msgMap);
				return baseDto;
			}
			//将原抄表数据置为无效
			TcMeterData data = this.tcMeterDataMapper.findByBuildingCode(entity.getBuildingCode());
			data.setIsUsed(1);
			this.tcMeterDataMapper.modify(data);
			
			//添加重抄任务
			TcReadingTask lastTask = this.tcReadingTaskMapper.getWaterTaskDetailById(data.getTaskId());
			TcReadingTask task = new TcReadingTask();
			task.setId(CommonUtils.getUUID());
			task.setScheduleCode(lastTask.getScheduleCode());
			task.setScheduleId(lastTask.getScheduleId());
			task.setTaskContent(entity.getDescriber());
			task.setScheduleContent(lastTask.getScheduleContent());
			task.setReadingPosition(entity.getBuildingCode());
			task.setReadingPersonId(entity.getChargePersonId());
			task.setTotalMeterCount(1);
			task.setRemainMeterCount(1);
			task.setCompleteMeterCount(0);
			task.setMeterType(0);
			task.setStartTime(new Date());
			task.setCreateId(entity.getCreateId());
			task.setCreateTime(new Date());
			task.setModifyId(entity.getModifyId());
			task.setModifyTime(new Date());
			task.setLastTaskId(lastTask.getId());
			task.setStatus(4);	//重抄任务状态为4
			this.tcReadingTaskMapper.insert(task);

			//插入新的抄表数据
			TcMeterData newData = new TcMeterData();
			newData.setLastTotalReading(data.getLastTotalReading());
			newData.setTotalReading(data.getTotalReading());
			newData.setMeterType(data.getMeterType());
			newData.setMeterCode(data.getMeterCode());
			newData.setReadingPersonName(data.getReadingPersonName());
			newData.setReadingPersonId(data.getReadingPersonId());
			newData.setTaskId(task.getId());
			newData.setBeforeTaskId(data.getTaskId());
			newData.setIsUsed(0);
			newData.setExStatus(1);
			newData.setProjectId(data.getProjectId());
			newData.setProjectName(data.getProjectName());
			this.tcMeterDataMapper.add(newData);
			
			//任务关联的抄表地址
			TcMeterRelation relation = new TcMeterRelation();
			relation.setId(CommonUtils.getUUID());
			relation.setBuildingCode(entity.getBuildingCode());
			relation.setRelationId(task.getId());
			relation.setType(3);
			this.tcMeterRelationMapper.add(relation);
			
			//插入投诉订单
			entity.setRelationId(task.getId());
			this.tcOrderComplaintMapper.insert(entity);
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			log.info(CommonUtils.log(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
		
	}

//	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto showDtail(WyBusinessContext ctx, String projectId,
			String orderCode, String buildCode, String type) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		ElectMeter  electMeter=null;
		List<TcWaterMeter> waterList =null;
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		try {
			Map<String,Object> orderComplainMap = this.tcOrderComplaintMapper.findByOrderCode(projectId, orderCode);
			if(CommonUtils.isEmpty(orderComplainMap)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("该工单不存在，可能已经被删除!");
				baseDto.setMessageMap(msgMap);
				return baseDto;
			}
			orderCode = String.valueOf(orderComplainMap.get("orderCode"));
			resultList.add(orderComplainMap);
			TcBuilding tcBuilding =this.tcBuildingMapper.findByProjectIdAndBuildCode(projectId, buildCode);
			if(CommonUtils.isEmpty(tcBuilding)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("工单号["+orderCode+"]所对应的资产已经不存在,不能查看该任务!");
				baseDto.setMessageMap(msgMap);
				return baseDto;
			}
			if(type.equals("1")){ //电表
				 electMeter = this.tcElectMeterMapper.getElectMeterByReationId(projectId, buildCode);
				if(CommonUtils.isEmpty(electMeter)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("资产["+tcBuilding.getBuildingFullName()+"]没有对应电表或者其对应的电表已经停用;不能查看该工单任务");
					baseDto.setMessageMap(msgMap);
					return baseDto;
				}
			}else if(type.equals("0")){//水表
				waterList = this.tcWaterMeterMapper.findByRelarionId(projectId, buildCode);
				if(CollectionUtils.isEmpty(waterList)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("资产["+tcBuilding.getBuildingFullName()+"]没有对应水表或者其对应的水表已经停用;不能查看该工单任务");
					baseDto.setMessageMap(msgMap);
					return baseDto;
				}
			}
			
			List<Map<String,Object>> listMap =this.tcOrderComplaintMapper.findByBuildAndMeterType(projectId, buildCode, type);
			if(CollectionUtils.isEmpty(listMap)){ //表示还没有抄过表就做产权变更
				Map<String,Object> map = new HashMap<String,Object>();
				//这里的上次等读数得去找表的初始读数
				if(type.equals("0")){//表示水表
						TcWaterMeter tcWaterMeter = waterList.get(0); //理论上这里只能查到一个，因为收费对象和水电表是一对一的关系
						map.put("meterCode", tcWaterMeter.getCode());
						map.put("lastTotalReading", tcWaterMeter.getInitAmount());
					
				}else if(type.equals("1")){ //表示电表
					map.put("meterCode", electMeter.getCode());
					map.put("lastCommonReading", electMeter.getInitaveragevalue());
					map.put("lastPeakReading", electMeter.getInitpeakvalue());
					map.put("lastTotalReading", electMeter.getInitamount());
					map.put("lastVallyReading", electMeter.getInitvalleyvalue());
				}
				resultList.add(map);
			}else{//表示做产权变更前，已经做过抄表
				if(type.equals("0")){//水表
				  Map<String,Object> resultMap =convertData(projectId,listMap,waterList.get(0).getCode(),orderCode);
				  Object error = resultMap.get("error");
				  if(CommonUtils.isNotEmpty(error)){
					  msgMap.setFlag(MessageMap.INFOR_ERROR);
					  msgMap.setMessage(error.toString());
					  baseDto.setMessageMap(msgMap);
					  return baseDto;
				  }
				  resultList.add(resultMap);
				}else if(type.equals("1")){
					//电表
				 Map<String,Object> resultMap =  convertData(projectId,listMap,electMeter.getCode(),orderCode);
				 Object error = resultMap.get("error");
				 if(CommonUtils.isNotEmpty(error)){
					 msgMap.setFlag(MessageMap.INFOR_ERROR);
					 msgMap.setMessage(error.toString());
					 baseDto.setMessageMap(msgMap);
					 return baseDto;
				  }
				 resultList.add(resultMap);
				}
			}
			//用作完成工单显示之用，这里还去查询下，完成工单的现场读数等
			String compId= String.valueOf(orderComplainMap.get("id"));
			Map<String,Object> complMap= this.tcOrderCompleteMapper.findCompById(compId);
			if(CommonUtils.isNotEmpty(complMap)){
				resultList.add(complMap);
			}
			baseDto.setMessageMap(msgMap);
			baseDto.setLstDto(resultList);
			return baseDto;
		} catch (Exception e) {
			log.info(CommonUtils.log(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 对变更前做过抄表操作的数据进行整理
	 */
	private Map<String,Object> convertData(String projectId,List<Map<String,Object>> listMap,String meterCode,String orderCode){
		Map<String,Object> map = new HashMap<String,Object>();
		String flag ="";
		//listMap最多只能查询到两条数据，查询出来的两条数据，有可能存在四种情况
		// 1、一条数据都没有（已排除，这里不用处理） 2、两条数据都是重抄数据  3、重抄数据和正常数据各一条  4、两条都是正常数据  5、一条重抄数据（这种情况不可能出现，如果是重抄这一定抄过表） 6、一条正常数据
		//不可能存在只能查询到一条数据且这条数据是重抄的数据；如果存在，则需要在重抄任务那里做限制；即如果只能查到一天数据，这一条数据只能是正常抄表数据
		if(listMap.size()==1){
			//只存在一条正常数据
			Map<String,Object> mapOne = listMap.get(0);
			Object auditStatus = mapOne.get("auditStatus");
			if(auditStatus.equals(MeterDataEnum.STATUS_AUDIT_COMPLETE.getIntValue())){
				//如果抄表数据已经审核，则表示该抄表数据不能更改，所以就是本次抄表数据
				flag="thisData";
				fillMap(map,mapOne,flag);
			}else if(auditStatus.equals(MeterDataEnum.STATUS_AUDITING.getIntValue())){
				//表示该抄表数据还未审核，做资产变更后，直接修改读数，所以这里是上次读数
				flag="lastData";
				fillMap(map,mapOne,flag);
			}
		}else{
			//对于重抄任务的，如果重抄任务完成了则只管剩下的那条正常的数据，如果重抄没有完成，作废这次重抄任务，然后做变更操作
			//重抄任务增加时应该加限制。如果一个重抄任务没结束掉，不能再次新建重抄任务
			List<Map<String,Object>> againCopyList =new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> noAgainCopy=new ArrayList<Map<String,Object>>();
			for(Map<String,Object> deMap:listMap){
				Object status = deMap.get("status");
				if(status.equals(MeterDataEnum.STATUS_AGAINING.getIntValue()) 
						|| status.equals(MeterDataEnum.STATUS_AGAINING_COMPLETE.getIntValue())){
					againCopyList.add(deMap);
				}else{
					noAgainCopy.add(deMap);
				}
			}
			Map<String,Object> mapOne = null;
			Map<String,Object> mapTwo = null;
			if(againCopyList.size()==0){
				//说明，没有重抄过表，则两条数据都是正常抄表数据，拿到最新一条数据
				mapOne = noAgainCopy.get(0);
				mapTwo = noAgainCopy.get(1);
				Map<String,Object> resMap = toCompareDate(mapOne,mapTwo);
				if(CommonUtils.isEmpty(resMap)){
					log.info(CommonUtils.log("任务编号["+mapOne.get("id")+"或者"+mapTwo.get("id")+"]的创建时间不存在,请联系管理员"));
					map.put("error", "任务编号["+mapOne.get("id")+"或者"+mapTwo.get("id")+"]的创建时间不存在,请联系管理员!");
				}else{
					Object auditStatus = resMap.get("auditStatus");
					if(auditStatus.equals(MeterDataEnum.STATUS_AUDIT_COMPLETE.getIntValue())){
						//如果是已经审核了的则，不能更改本次数据，则上次读数应该是该数据的本次读数
						flag="thisData";
						fillMap(map,resMap,flag);
					}else if(auditStatus.equals(MeterDataEnum.STATUS_AUDITING.getIntValue())){
						//表示该抄表数据还未审核，做资产变更后，直接修改读数，所以这里是上次读数
						flag="lastData";
						fillMap(map,resMap,flag);
					}
				}
			}
			
			if(againCopyList.size()==1 && noAgainCopy.size()==1){
				//表明一条数据是正常数据一条数据是重抄数据
				Map<String,Object> againCopyMap = againCopyList.get(0);
				Map<String,Object> commonMap = noAgainCopy.get(0);
				Object againStatus = againCopyMap.get("status");
				if(againStatus.equals(MeterDataEnum.STATUS_AGAINING.getIntValue())){
					//如果是重抄任务执行中的状态，则可以直接作废掉该task和meterData；然后直接读写本次变更读数
					String id =String.valueOf(againCopyMap.get("id"));
					deposeTaskAndMeterData(id,meterCode,orderCode,projectId);
				}
				//如果是非重抄任务重抄完成状态可以不用管，因为重抄完成时，会将重抄的数据回改到普通正常数据里
				Object auditStatus = commonMap.get("auditStatus");
				if(auditStatus.equals(MeterDataEnum.STATUS_AUDIT_COMPLETE.getIntValue())){
					//如果是已经审核了的则，不能更改本次数据，则上次读数应该是该数据的本次读数
					flag="thisData";
					fillMap(map,commonMap,flag);
				}else if(auditStatus.equals(MeterDataEnum.STATUS_AUDITING.getIntValue())){
					//表示该抄表数据还未审核，做资产变更后，直接修改读数，所以这里是上次读数
					flag="lastData";
					fillMap(map,commonMap,flag);
				}
			}
			
			if(againCopyList.size()==2 && noAgainCopy.size()==0){
				//两条数据都是重抄数据 、两条重抄数据都是进行中的情况不可能出现，因为一条重抄没有结束前，不能再次创建重抄任务
				//所以这里只能出现一条重抄是已经重抄完成，一条是正在进行中;或者两条都是重抄完成
				Map<String,Object> againCopyMapOne = againCopyList.get(0);
				Map<String,Object> againCopyMapTwo = againCopyList.get(1);
				
				Map<String,Object> resMap = toCompareDate(againCopyMapOne,againCopyMapTwo);
				if(CommonUtils.isEmpty(resMap)){
					map.put("error","任务编号["+againCopyMapOne.get("id")+"或者"+againCopyMapTwo.get("id")+"]的创建时间为空,不能操作!");
					log.info(CommonUtils.log("任务编号["+againCopyMapOne.get("id")+"或者"+againCopyMapTwo.get("id")+"]的创建时间为空,不能操作!"));
				}else{
					//查看该重抄单的重抄状态
					Object status = resMap.get("status");
					//对于重抄未完成的，则作废这条重抄任务和meterData;对于已经重抄完成的，则不用管
					if(status.equals(MeterDataEnum.STATUS_AGAINING.getIntValue())){
						String tashId = String.valueOf(resMap.get("id"));
						deposeTaskAndMeterData(tashId,meterCode,orderCode,projectId);
					}
					//根据lastTaskId找到重抄前的那条task;重抄任务的lastTaskId一定存在，因为只有超过表之后才能做重抄任务
					String lastTaskId = String.valueOf(resMap.get("lastTaskId"));
					TcReadingTask  beforeTask = this.tcReadingTaskMapper.getWaterTaskDetailById(lastTaskId);
					if(CommonUtils.isNotEmpty(beforeTask)){
						String beforeTaskId = beforeTask.getId();
						TcMeterData  tcMeterData = this.tcMeterDataMapper.findByTaskIdAndMeterCode(projectId,beforeTaskId,meterCode);
						if(beforeTask.getAuditStatus().equals(MeterDataEnum.STATUS_AUDIT_COMPLETE.getIntValue()) && 
								CommonUtils.isNotEmpty(tcMeterData)){
							map.put("meterCode", tcMeterData.getMeterCode());
							map.put("lastCommonReading", tcMeterData.getCommonReading()==null?0:tcMeterData.getCommonReading());
							map.put("lastPeakReading", tcMeterData.getPeakReading()==null?0:tcMeterData.getPeakReading());
							map.put("lastTotalReading", tcMeterData.getTotalReading()==null?0:tcMeterData.getTotalReading());
							map.put("lastVallyReading", tcMeterData.getVallyReading()==null?0:tcMeterData.getVallyReading());
						}else if(CommonUtils.isNotEmpty(tcMeterData) && 
								beforeTask.getAuditStatus().equals(MeterDataEnum.STATUS_AUDITING.getIntValue())){
							map.put("meterCode", tcMeterData.getMeterCode());
							map.put("lastCommonReading", tcMeterData.getLastCommonReading()==null?0:tcMeterData.getLastCommonReading());
							map.put("lastPeakReading", tcMeterData.getLastPeakReading()==null?0:tcMeterData.getLastPeakReading());
							map.put("lastTotalReading", tcMeterData.getLastTotalReading()==null?0:tcMeterData.getLastTotalReading());
							map.put("lastVallyReading", tcMeterData.getLastVallyReading()==null?0:tcMeterData.getLastVallyReading());
						}
					}
				}
			}
		}
		return map;
	}
	
	
	/**
	 * 作废为重抄完成的重抄任务和meterData表
	 */
	public void deposeTaskAndMeterData(String id,String meterCode,String orderCode,String projectId){
		
		//同时这里作废该工单
		TcOrderComplaint  tcOrderComplaint = this.tcOrderComplaintMapper.findTcOrderByOrderCode(projectId, orderCode);
		if(CommonUtils.isNotEmpty(tcOrderComplaint)){
			tcOrderComplaint.setOrderStatus(TcOrderComplaintAndCompleteEnum.ORDER_STATUS_DESPOSE.getIntV());
			this.tcOrderComplaintMapper.update(tcOrderComplaint);
		}
		//如果是重抄任务执行中的状态，则可以直接作废掉该task和meterData；然后直接读写本次变更读数
		TcReadingTask tcReadingTask =this.tcReadingTaskMapper.getWaterTaskDetailById(id);
		if(CommonUtils.isNotEmpty(tcReadingTask)){ //终止该重抄任务
			tcReadingTask.setStatus(MeterDataEnum.EX_STATUS_ERROR.getIntValue());
			this.tcReadingTaskMapper.update(tcReadingTask);
		}
		//查找对应的meterData然后作废掉
		TcMeterData  tcMeterData = this.tcMeterDataMapper.queryByMeterCodeAndTaskId(id,meterCode);
		List<TcMeterData> meterDataList = new ArrayList<TcMeterData>();
		if(CommonUtils.isNotEmpty(tcMeterData)){
			tcMeterData.setIsUsed(MeterDataEnum.IS_USE_NO.getIntValue());//设置为无效
			meterDataList.add(tcMeterData);
			this.tcMeterDataMapper.batchUpdate(meterDataList); //
		}
	}
	
	/**
	 * 往Map里填充数据
	 */
	private void  fillMap(Map<String,Object> destmap,Map<String,Object> rawDataMap,String flag){
		//本次读数
		if(flag.equals("thisData")){
			destmap.put("meterCode", rawDataMap.get("meterCode"));
			destmap.put("lastCommonReading", rawDataMap.get("commonReading")==null?0:rawDataMap.get("commonReading"));
			destmap.put("lastPeakReading", rawDataMap.get("peakReading")==null?0:rawDataMap.get("peakReading"));
			destmap.put("lastTotalReading", rawDataMap.get("totalReading")==null?0:rawDataMap.get("totalReading"));
			destmap.put("lastVallyReading", rawDataMap.get("vallyReading")==null?0:rawDataMap.get("vallyReading"));
		}else if(flag.equals("lastData")){//上次读数
			destmap.put("meterCode", rawDataMap.get("meterCode"));
			destmap.put("lastCommonReading", rawDataMap.get("lastCommonReading")==null?0:rawDataMap.get("lastCommonReading"));
			destmap.put("lastPeakReading", rawDataMap.get("lastPeakReading")==null?0:rawDataMap.get("lastPeakReading"));
			destmap.put("lastTotalReading", rawDataMap.get("lastTotalReading")==null?0:rawDataMap.get("lastTotalReading"));
			destmap.put("lastVallyReading", rawDataMap.get("lastVallyReading")==null?0:rawDataMap.get("lastVallyReading"));
		}
	}
	
	
	/**
	 * 获取create_time最新的数据
	 * @param dateOne
	 * @param dateTow
	 * @return
	 */
	public Map<String,Object> toCompareDate(Map<String,Object> mapOne,Map<String,Object> mapTwo){
		Map<String,Object> map =null;
		Object createTimeOne = mapOne.get("createTime");
		Object createTimeTwo = mapTwo.get("createTime");
		if(CommonUtils.isNotEmpty(createTimeOne) && CommonUtils.isNotEmpty(createTimeTwo)){
			map = createTimeOne.toString().compareTo(createTimeTwo.toString())>0?mapOne:mapTwo;
		}
		return map;
	}
	
	
//	@Test
//	public void testObj(){
//		
//		System.out.println("2017-08-03 15:14:05".compareTo("2017-09-03 15:14:05"));
//	}

}
