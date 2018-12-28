package com.everwing.coreservice.wy.core.service.impl.order;

import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.utils.WyAppPushUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.MeterDataEnum;
import com.everwing.coreservice.common.wy.common.enums.TcOrderComplaintAndCompleteEnum;
import com.everwing.coreservice.common.wy.dto.MyWorkOrderDTO;
import com.everwing.coreservice.common.wy.dto.OrderSearchDto;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.readingtask.TcReadingTask;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.configuration.bill.FeeItemDetail;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TempCurrentFee;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargeType;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsChargingScheme;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.entity.order.TcOrder;
import com.everwing.coreservice.common.wy.entity.order.TcOrderComplaint;
import com.everwing.coreservice.common.wy.entity.order.TcOrderComplete;
import com.everwing.coreservice.common.wy.entity.order.TcOrderExample;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUser;
import com.everwing.coreservice.common.wy.service.order.TcOrderComplaintService;
import com.everwing.coreservice.common.wy.service.order.TcOrderCompleteService;
import com.everwing.coreservice.common.wy.utils.SysConfig;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.platform.api.IncreasedIdApi;
import com.everwing.coreservice.wy.dao.mapper.annex.AnnexMapper;
import com.everwing.coreservice.wy.dao.mapper.business.electmeter.TcElectMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.business.readingtask.TcReadingTaskMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.TBsProjectMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillHistoryMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillTotalMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargeTypeMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.project.TBsChargingSchemeMapper;
import com.everwing.coreservice.wy.dao.mapper.order.TcOrderExtraMapper;
import com.everwing.coreservice.wy.dao.mapper.order.TcOrderMapper;
import com.everwing.coreservice.wy.dao.mapper.order.TcOrderTypeExtraMapper;
import com.everwing.coreservice.wy.dao.mapper.order.complaint.TcOrderComplaintMapper;
import com.everwing.coreservice.wy.dao.mapper.order.complete.TcOrderCompleteMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysUserMapper;
import com.everwing.utils.FormulaCalculationUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.number.NumberFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

@Service("tcOrderCompleteServiceImpl")
public class TcOrderCompleteServiceImpl implements TcOrderCompleteService{
	
	private static final Logger log = LogManager.getLogger(TcOrderCompleteServiceImpl.class);
	
	@Value("${queue.task2Wy.bill.gen.key}")
	private String route_key_gen_bill;
	
	@Value("${queue.task2Wy.project.auto.flush.key}")
	private String route_key_sum_project_by_company_id;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Autowired
	private TcOrderCompleteMapper tcOrderCompleteMapper;
	
	@Autowired
	private TcOrderComplaintMapper tcOrderComplaintMapper;
	
	@Autowired
	private TcMeterDataMapper tcMeterDataMapper;
	
	@Autowired
	private TcReadingTaskMapper tcReadingTaskMapper;
	
	@Autowired
	private TcElectMeterMapper tcElectMeterMapper;
	
	@Autowired
	private TcWaterMeterMapper tcWaterMeterMapper;
	
	@Autowired
	private TcOrderTypeExtraMapper tcOrderTypeExtraMapper;
	
	@Autowired
	private TcOrderMapper tcOrderMapper;

	@Autowired
	private TSysUserMapper tSysUserMapper;
	
	@Autowired
	private TcOrderExtraMapper tcOrderExtraMapper;
	
	@Autowired
	private TcBuildingMapper tcBuildingMapper;
	
	@Autowired
	private IncreasedIdApi increasedIdApi;
	
	@Autowired
	private FastDFSApi fastDFSApi;
	
	@Autowired
	private TBsChargeBillTotalMapper tBsChargeBillTotalMapper;
	
	@Autowired
	private TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;
	
	@Autowired
	private AnnexMapper annexMapper;
	
	@Autowired
	private SysConfig sysConfig;
	
	@Autowired
	private TBsProjectMapper tBsProjectMapper;
	
	@Autowired
	private TBsChargeTypeMapper tBsChargeTypeMapper;
	
	@Autowired
	private TBsChargingSchemeMapper tBsChargingSchemeMapper;

	@Autowired
	private WyAppPushUtils wyAppPushUtils;

	@Override
	public BaseDto listPageDatas(String companyId, OrderSearchDto orderSearchDto) {
		List<Map> listPageDatas = tcOrderExtraMapper.listPageDatas(orderSearchDto);
		BaseDto baseDto = new BaseDto();
		baseDto.setLstDto(listPageDatas);
		baseDto.setPage(orderSearchDto.getPage());
		baseDto.setMessageMap(new MessageMap(null, "请求成功."));
		return baseDto;
	}
	
	@Override
	public BaseDto updateOrder(String companyId,TcOrder order) {
		tcOrderExtraMapper.updatePrinciplePersonByOrderCode(order);
		TcOrderExample tcOrderExample=new TcOrderExample();
		tcOrderExample.createCriteria().andOrderCodeEqualTo(order.getOrderCode());
		TcOrder tcOrder=tcOrderMapper.selectByExample(tcOrderExample).get(0);
		String newUserId=order.getPrincipalSysUserId();
		if(newUserId!=null){
			if(tcOrder.getPrincipalSysUserId().equals(newUserId)){//未改变负责人不需要推送
			}else {
				TSysUser sysUser=tSysUserMapper.selectByPrimaryKey(newUserId);
				wyAppPushUtils.pushToOrderResponsiblePerson(sysUser.getMobileTelephone());
			}
		}
		return new BaseDto();
	}
	
	@Override
	public BaseDto newOrders(String companyId,List<TcOrder> newOrders,String userId) {
		if (newOrders != null) {
			for(TcOrder order : newOrders){
				order.setOrderId(UUID.randomUUID().toString());
				order.setCreateTime(new Date());
				order.setUpdateTime(new Date());
				order.setStatus(1);
				order.setCreateBy(userId);
				order.setOrderCode(generateOrderCodeByBuildingCode(order.getBuildingCode()));
				tcOrderMapper.insert(order);
				String principalSysUserId=order.getPrincipalSysUserId();
				TSysUser sysUser=tSysUserMapper.selectByPrimaryKey(principalSysUserId);
				wyAppPushUtils.pushToOrderResponsiblePerson(sysUser.getMobileTelephone());
			}
		}
		return new BaseDto();
	}
	
	/**
	 * @description 工单号生成逻辑
	 */
	private String generateOrderCodeByBuildingCode(String buildingCode){
		String projectId = tcBuildingMapper.loadBuildingByBuildingCode(buildingCode).getProjectId();
		Integer increasedId = increasedIdApi.generateIncreasedId(1).getModel();
		String dateStr = new DateFormatter("yyMMdd").print(new Date(), Locale.CHINA);
		String numberStr = new NumberFormatter("000000").print(increasedId, Locale.CHINA);
		String orderCode = "WO" + projectId + dateStr + numberStr;
		return orderCode;
	}
	
	@Override
	public BaseDto finishOrders(String companyId,String ids,String updateBy) {
		TcOrder order = new TcOrder();
		order.setStatus(2);
		order.setUpdateBy(updateBy);
		order.setUpdateTime(new Date());
		order.setFinishTime(new Date());
		TcOrderExample example = new TcOrderExample();
		example.createCriteria().andOrderIdIn(Arrays.asList(ids.split(",")));
		tcOrderMapper.updateByExampleSelective(order, example);
		
		return new BaseDto();
	}
	
	@Override
	public BaseDto deleteOrders(String companyId,String ids) {
		TcOrderExample example = new TcOrderExample();
		example.createCriteria().andOrderIdIn(Arrays.asList(ids.split(",")));
		tcOrderMapper.deleteByExample(example);
		
		return new BaseDto();
	}
	
	@Override
	public BaseDto subType(String companyId,String typeId) {
		return new BaseDto(tcOrderTypeExtraMapper.subType(typeId), null);
	}
	
	@Override
	public BaseDto levelOneType(String companyId) {
		return new BaseDto(tcOrderTypeExtraMapper.levelOneType(), null);
	}
	
	/**
	 * 针对产权变更的完成工单
	 */
	@Transactional(rollbackFor=Exception.class)
	public BaseDto inserChangeAssetInsert(WyBusinessContext ctx, TcOrderComplete entity, String type)throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			//1. 修改投诉工单的关联完成工单号
			TcOrderComplaint tcOrderComplaint =  null;
			Map<String,Object> mapComplaint = updateComplain(entity);
			Object ObjError = mapComplaint.get("error");
			if(CommonUtils.isNotEmpty(ObjError)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(ObjError.toString());
				baseDto.setMessageMap(msgMap);
				return baseDto;
			}
			Object objComplaint = mapComplaint.get("complaint");
			if(CommonUtils.isEmpty(objComplaint)){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("需要完成的工单已经不存在,可能已经被删除!");
				baseDto.setMessageMap(msgMap);
				return baseDto;
			}
			tcOrderComplaint =(TcOrderComplaint)objComplaint;
			String buildCode = tcOrderComplaint.getBuildingCode();
			String projectId = ctx.getProjectId();
			String id = tcOrderComplaint.getId();
			String orderCode = tcOrderComplaint.getOrderCode();
			String meterCode ="";
			String meterName="";
			if(type.equals("0")){
				List<TcWaterMeter> listTcWater= this.tcWaterMeterMapper.findByRelarionId(projectId, buildCode);
				if(CommonUtils.isEmpty(listTcWater)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("建筑编号["+buildCode+"]没有对应的水表;");
					updateComplainHanding(entity);
				}else{
					meterCode = listTcWater.get(0).getCode();
					meterName = listTcWater.get(0).getWaterMeterName();
				}
			}else if(type.equals("1")){
				ElectMeter	electMeter = this.tcElectMeterMapper.getElectMeterByReationId(projectId, buildCode);
				if(CommonUtils.isEmpty(electMeter)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("建筑编号["+buildCode+"]没有对应的电表;");
					updateComplainHanding(entity);
				}else{
					meterCode = electMeter.getCode();
					meterName = electMeter.getElectricitymetername();
				}
			}
			
			//这里要最新的task是什么状态；如果是已经审核了的状态则不用管,新建立一个materData,如果是未审核的状态，用量要累加起来,则不用新建立meterData,直接修改最新task对应meterData的数据
			List<Map<String,Object>> listMap =this.tcOrderComplaintMapper.findByBuildAndMeterType(projectId, buildCode, type);
			if(CommonUtils.isEmpty(listMap)){
				//说明未抄表过(即meterData数据都没产生过)；就做产权变更；这里需要新建一个meteData数据，然后产权变更时，会直接扣费用;
				//这里需要新创建一个meterData数据
				TcMeterData data = new TcMeterData();
				noMeterDataChangeAsset(ctx,entity,data,type,id,meterCode,meterName);
			}else{
				//表示做过抄表在做产权变更;对于抄过表任务；如果是已经审核了的；查找下一次生成的任务生成的数据；更改一下抄表任务的上次抄表读数，并且新增
				//对于未审核的，直接修改本次读数
				Map<String,Object> newTaskMap = getNewestTask(listMap,projectId,meterCode,orderCode);
				TcReadingTask tcReadingTask = (TcReadingTask)newTaskMap.get("task");
				
				Object error = newTaskMap.get("error");
				if(CommonUtils.isNotEmpty(error)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage(error.toString());
					updateComplainHanding(entity);
				}else{
					entity.setId(tcOrderComplaint.getId());
					entity.setComplaintOrderId(tcOrderComplaint.getId());
					entity.setRelationId(tcReadingTask.getId());
					entity.setCreateId(ctx.getUserId());
					entity.setCreateTime(new Date());
					entity.setExceptionReason(3);
					entity.setIsAlreadyBilling(TcOrderComplaintAndCompleteEnum.IS_BILLING_NO.getIntV());
					
					Map<String,Object> errorMap = calculationUseCount(ctx,projectId,id,buildCode,type,newTaskMap,entity,meterCode,meterName);
					if(CommonUtils.isNotEmpty(errorMap.get("error"))){
						msgMap.setFlag(MessageMap.INFOR_ERROR);
						msgMap.setMessage(errorMap.get("error").toString());
						updateComplainHanding(entity);
					}else{
						msgMap.setFlag(MessageMap.INFOR_SUCCESS);
						this.tcOrderCompleteMapper.insert(entity);
					}
					
				}
			}
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			log.info(CommonUtils.log(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
		
	}

    //如果错误，需要将工单设置成为完成状态
	private TcOrderComplaint updateComplainHanding(TcOrderComplete entity){
		TcOrderComplaint complaint=null;
		 complaint = this.tcOrderComplaintMapper.findById(entity.getComplaintOrderId());
		 if(CommonUtils.isNotEmpty(complaint)){
			 complaint.setCompleteOrderId(entity.getComplaintOrderId());  //设置工单与完成工单id一致
				complaint.setOrderStatus(1); //完成状态
				complaint.setModifyTime(new Date());
				complaint.setModifyId(entity.getCreateId());
				complaint.setId(entity.getComplaintOrderId());
				this.tcOrderComplaintMapper.update(complaint);
		 }
		return complaint;
	}
	
	/**
	 * 对于还未抄过表就做产权变更
	 */
	private void noMeterDataChangeAsset(WyBusinessContext ctx,TcOrderComplete entity,
			                            TcMeterData data,String type,String orderCode,
			                            String meterCode,String meterName){
		
		entity.setId(orderCode);
		entity.setComplaintOrderId(orderCode);
		entity.setCreateId(ctx.getUserId());
		entity.setCreateTime(new Date());
		entity.setIsAlreadyBilling(TcOrderComplaintAndCompleteEnum.IS_BILLING_NO.getIntV());
		this.tcOrderCompleteMapper.insert(entity);
		
		data.setPeakReading(entity.getCompletePeakReading());
		data.setLastPeakReading(0.00);
		data.setVallyReading(entity.getCompleteVallyReading());
		data.setLastVallyReading(0.00);
		data.setCommonReading(entity.getCompleteCommonReading());
		data.setLastCommonReading(0.00);
		data.setTotalReading(Double.parseDouble(entity.getCompleteContent()));
		data.setUseCount(Double.parseDouble(entity.getCompleteContent()));
		data.setPeakCount(entity.getCompletePeakReading());
		data.setVallyCount(entity.getCompleteVallyReading());
		data.setCommonCount(entity.getCompleteCommonReading());
		Integer meterType = type.equals("0")?0:1;
		data.setMeterType(meterType);
		data.setMeterCode(meterCode);
		data.setCircleCorrection(0.00);
		data.setPeakCircleCorrection(0.00);
		data.setValleyCircleCorrection(0.00);
		data.setAverageCircleCorrection(0.00);
		data.setCorrection(0.00);
		data.setPeakCorrection(0.00);
		data.setValleyCorrection(0.00);
		data.setAverageCorrection(0.00);
		data.setAuditStatus(1); //已经审核，对于产权变更的抄表不需要审核
		data.setReadingPersonName(ctx.getStaffName());
		data.setReadingPersonId(ctx.getUserId());
		data.setReadingTime(new Date());
		data.setTaskId(entity.getId());
		data.setIsUsed(0);
		data.setCreateId(ctx.getUserId());
		data.setCreateTime(new Date());
		data.setProjectId(ctx.getProjectId());
		data.setProjectName(ctx.getProjectName());
		data.setMeterName(meterName);
		List<TcMeterData> listData = new ArrayList<TcMeterData>();
		listData.add(data);
		this.tcMeterDataMapper.batchAdd(listData);
	}
	
	
	/**
	 * 计算用量
	 * projectId,orderCode,buildCode,type
	 */
	private Map<String,Object> calculationUseCount(WyBusinessContext ctx,String projectId,String orderCode,
			                         String buildCode,String type,Map<String,Object> map,
			                         TcOrderComplete entity,String meterCode,String meterName){
		Map<String,Object> mapError = new HashMap<String,Object>();
		Map<String,Object> lastMeterDataMap = getNewLastMeterData(ctx,projectId,orderCode,buildCode,type);
		if(CommonUtils.isNotEmpty(lastMeterDataMap.get("error"))){
			mapError.put("error", lastMeterDataMap.get("error"));
			return mapError;
		}
		String flag = String.valueOf(map.get("flag"));
		TcReadingTask tcReadingTask= (TcReadingTask)map.get("task");
		String taskId = tcReadingTask.getId();
		TcMeterData  tcMeterData =this.tcMeterDataMapper.findByTaskIdAndMeterCode(projectId,taskId,meterCode);
		
		Double userCount =(tcMeterData.getUseCount()==null?0.00:tcMeterData.getUseCount())+(Double.parseDouble(entity.getCompleteContent())-Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastTotalReading"))));
		Double peakCount=0.00;
		Double commonCount=0.00;
		Double vallyCount=0.00;
		if(type.equals("1")){
			 peakCount = (tcMeterData.getPeakCount()==null?0.00:tcMeterData.getPeakCount())+(entity.getCompletePeakReading()-Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastPeakReading"))));
			 commonCount = (tcMeterData.getCommonCount()==null?0.00:tcMeterData.getCommonCount())+(entity.getCompleteCommonReading()-Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastCommonReading"))));
			 vallyCount = (tcMeterData.getVallyCount()==null?0.00:tcMeterData.getVallyCount())+(entity.getCompleteVallyReading()-Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastVallyReading"))));
		}
		
		if(flag.equals("update")){
			//计算用量
			//检查用量是否是负数，如果用量是负数，则也不强制检验
			tcMeterData.setUseCount(userCount);
			
			tcMeterData.setPeakCount(peakCount);
			tcMeterData.setCommonCount(commonCount);
			tcMeterData.setVallyCount(vallyCount);
			tcMeterData.setAuditStatus(1); 
			//更新本次读数
			tcMeterData.setPeakReading(entity.getCompletePeakReading());
			tcMeterData.setTotalReading(Double.parseDouble(entity.getCompleteContent()));
			tcMeterData.setCommonReading(entity.getCompleteCommonReading());
			tcMeterData.setVallyReading(entity.getCompleteVallyReading());
			
			tcMeterData.setLastTotalReading(Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastTotalReading"))));
			tcMeterData.setLastCommonReading(Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastCommonReading"))));
			tcMeterData.setLastPeakReading(Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastPeakReading"))));
			tcMeterData.setLastVallyReading(Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastVallyReading"))));
			List<TcMeterData> listdata = new ArrayList<TcMeterData>();
			listdata.add(tcMeterData);
			this.tcMeterDataMapper.batchUpdate(listdata);
		}else if(flag.equals("insert")){
			//更新上次读数即可
			tcMeterData.setLastTotalReading(Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastTotalReading"))));
			tcMeterData.setLastCommonReading(Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastCommonReading"))));
			tcMeterData.setLastPeakReading(Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastPeakReading"))));
			tcMeterData.setLastVallyReading(Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastVallyReading"))));
			List<TcMeterData> listdata = new ArrayList<TcMeterData>();
			listdata.add(tcMeterData);
			this.tcMeterDataMapper.batchUpdate(listdata);
			TcMeterData data = new TcMeterData();
			converMeterData(data,lastMeterDataMap,entity);
			
			data.setUseCount(userCount);
			data.setPeakCount(peakCount);
			data.setCommonCount(commonCount);
			data.setVallyCount(vallyCount);
			
			Integer meterType = type.equals("0")?0:1; //0水表   1电表
			data.setMeterType(meterType);
			data.setMeterCode(meterCode);
			data.setCircleCorrection(0.0);
			data.setPeakCircleCorrection(0.00);
			data.setValleyCircleCorrection(0.00);
			data.setAverageCircleCorrection(0.00);
			data.setCorrection(0.00);
			data.setPeakCorrection(0.00);
			data.setValleyCorrection(0.00);
			data.setAverageCorrection(0.00);
			data.setAuditStatus(1); //对于产权变更的抄表不需要审核
			data.setReadingPersonId(ctx.getUserId());
			data.setReadingPersonName(ctx.getStaffName());
			data.setReadingTime(new Date());
			data.setRemark("产权变更抄表");
			data.setTaskId(entity.getId());
			data.setIsUsed(0);
			data.setCreateId(ctx.getUserId());
			data.setCreateTime(new Date());
			data.setProjectId(projectId);
			data.setProjectName(ctx.getProjectName());
			data.setMeterName(meterName);
			List<TcMeterData> listMeteData = new ArrayList<TcMeterData>();
			listMeteData.add(data);
			this.tcMeterDataMapper.batchAdd(listMeteData);
		}
		return mapError;
	}
	
	
	private void converMeterData(TcMeterData data,Map<String,Object> lastMeterDataMap,
			TcOrderComplete entity){
		
		data.setPeakReading(entity.getCompletePeakReading());
		data.setLastPeakReading(Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastPeakReading"))));
		data.setVallyReading(entity.getCompleteVallyReading());
		data.setLastVallyReading(Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastVallyReading"))));
		data.setCommonReading(entity.getCompleteCommonReading());
		data.setLastCommonReading(Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastCommonReading"))));
		data.setTotalReading(Double.parseDouble(entity.getCompleteContent())); //这里要保存输入进来的是数字
		data.setLastTotalReading(Double.parseDouble(String.valueOf(lastMeterDataMap.get("lastTotalReading"))));
	}
	
	
	/**
	 * 查找最新的task
	 */
	private Map<String,Object> getNewestTask(List<Map<String,Object>> listMap,String projectId,String meterCode,String orderCode){
		//listMap已经限制最多只能查询出两条数据
		//1、listMap为空(代码已经判断，这里不用做考虑) 2、一条数据(重抄数据，这里不可能出现，没有抄表过，是不能重抄的) 3、一条正常数据
		// 4、两条重抄数据  5、一条重抄一条正常      6、两条正常数据  
		//通过readTime是否为空来判别，未审核状态的task是已经超过表的；还是未抄表过的。
		//已经超过表的直接修改上次读数，本次读数和用量；未抄过表的直接修改上次读数即可
		Map<String,Object> map = new HashMap<String, Object>();
		//如果是一条正常数据，则这条任务必定是未审核状态，因为只要审核就会产生下一次抄表的数据
		String flag="";
		if(listMap.size()==1){
			//一条正常数据  未审核状态
			Map<String,Object> oneMap = listMap.get(0);
			String taskId = String.valueOf(oneMap.get("id"));
			TcReadingTask  tcReadingTask = this.tcReadingTaskMapper.getWaterTaskDetailById(taskId);
			if(CommonUtils.isEmpty(tcReadingTask)){
				log.info(CommonUtils.log("抄表任务编号:["+taskId+"]的抄表任务不存在，可能已经被删除!"));
				map.put("error", "抄表任务编号:["+taskId+"]的抄表任务不存在，可能已经被删除!");
			}else{
				Object auditStatus = oneMap.get("auditStatus");
				if(auditStatus.equals(MeterDataEnum.STATUS_AUDIT_COMPLETE.getIntValue())){
					log.info(CommonUtils.log("抄表任务["+String.valueOf(oneMap.get("id"))+"审核后还没有产生下一次抄表的数据，请检查!"));
					map.put("error", "抄表任务["+String.valueOf(oneMap.get("id"))+"还没审核,没有产生下一次抄表的任务,请先审核该抄表任务或者稍后操作!");
				}else if(auditStatus.equals(MeterDataEnum.STATUS_AUDITING.getIntValue())){
					Object readTime = oneMap.get("readingTime");
					if(CommonUtils.isEmpty(readTime)){
						//表示是还没有超过表的
						map.put("task", tcReadingTask);
						flag ="insert";
						map.put("flag", flag);
					}else{
						//表示已经抄过表的
						map.put("task", tcReadingTask);
						flag ="update";
						map.put("flag", flag);
					}
					
				}
			}
		}
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
		if(againCopyList.size()==1 && noAgainCopy.size()==1){
			//表示：一条正常数据 一条重抄数据
			Map<String,Object> againCopyMap = againCopyList.get(0);
			Map<String,Object> commonMap = noAgainCopy.get(0);
			Object againStatus = againCopyMap.get("status");
			if(againStatus.equals(MeterDataEnum.STATUS_AGAINING.getIntValue())){
				//如果是重抄任务执行中的状态，则可以直接作废掉该task和meterData；然后直接读写本次变更读数
				String id =String.valueOf(againCopyMap.get("id"));
				deposeTaskAndMeterData(id,meterCode,orderCode,projectId);
			}
			//检查正常数据的状态,如果是未审核这直接修改，如果是已经审核则创建(但是这里如果出现了一条已经审核一条重抄的，则表明，审核抄表后，还程序还没来得及产生下一次要抄表的数据，则需要等待。)
			Object auditStatus = commonMap.get("auditStatus");
			String taskCommId = String.valueOf(commonMap.get("id"));
			if(auditStatus.equals(MeterDataEnum.STATUS_AUDIT_COMPLETE.getIntValue())){
				log.info(CommonUtils.log("任务["+String.valueOf(commonMap.get("id"))+"审核后还没有产生下一次抄表的数据，请检查!"));
				map.put("error", "抄表任务["+String.valueOf(commonMap.get("id"))+"没有审核，没有产生下一次抄表任务数据,请审核该抄表任务或者稍后操作!");
			}else if(auditStatus.equals(MeterDataEnum.STATUS_AUDITING.getIntValue())){
				//
				Object readTime = commonMap.get("readingTime");
				TcReadingTask tcReadingTask	=this.tcReadingTaskMapper.getWaterTaskDetailById(taskCommId);
				if(CommonUtils.isEmpty(readTime)){
					//表示还没有抄表过
					map.put("task", tcReadingTask);
					flag ="insert";
					map.put("flag", flag);
				}else{
					map.put("task", tcReadingTask);
					flag ="update";
					map.put("flag", flag);
				}
			}
		}
		
		//两条正常数据
		if(againCopyList.size()==0 && noAgainCopy.size()==2){
			mapOne = noAgainCopy.get(0);
			mapTwo = noAgainCopy.get(1);
			Map<String,Object> mapNew = toCompareDate(mapOne,mapTwo);
			Object auditStatus = mapNew.get("auditStatus");
			String taskId = String.valueOf(mapNew.get("id"));
			if(auditStatus.equals(MeterDataEnum.STATUS_AUDIT_COMPLETE.getIntValue())){
				log.info(CommonUtils.log("任务["+String.valueOf(mapNew.get("id"))+"审核后还没有产生下一次抄表的数据，请检查!"));
				map.put("error", "抄表任务["+String.valueOf(mapNew.get("id"))+"没有审核，没有产生下一次抄表任务,请请审核该抄表任务或者稍后操作!");
			}else if(auditStatus.equals(MeterDataEnum.STATUS_AUDITING.getIntValue())){
				//这里做修改操作
				Object readTime = mapNew.get("readingTime");
				TcReadingTask tcReadingTask	=this.tcReadingTaskMapper.getWaterTaskDetailById(taskId);
				if(CommonUtils.isEmpty(readTime)){
					//表示还没有抄过表
					map.put("task", tcReadingTask);
					flag ="insert";
					map.put("flag", flag);
				}else{
					map.put("task", tcReadingTask);
					flag ="update";
					map.put("flag", flag);
				}
				
			}
		}
		
		//两条都是重抄数据
		if(againCopyList.size()==2 && noAgainCopy.size()==0){
			//两条数据都是重抄数据 、两条重抄数据都是进行中的情况不可能出现，因为一条重抄没有结束前，不能再次创建重抄任务
			//所以这里只能出现一条重抄是已经重抄完成，一条是正在进行中;或者两条都是重抄完成
			mapOne = againCopyList.get(0);
			mapTwo = againCopyList.get(1);
			Map<String,Object> newMap= toCompareDate(mapOne,mapTwo);
			Object againStatus = newMap.get("status");
			if(againStatus.equals(MeterDataEnum.STATUS_AGAINING.getIntValue())){
				//如果是重抄任务执行中的状态，则可以直接作废掉该task和meterData；然后直接读写本次变更读数
				String id =String.valueOf(newMap.get("id"));
				deposeTaskAndMeterData(id,meterCode,orderCode,projectId);
			}
		   //找到重抄之前的那个task
			//根据lastTaskId找到重抄前的那条task;重抄任务的lastTaskId一定存在，因为只有超过表之后才能做重抄任务
			String lastTaskId = String.valueOf(newMap.get("lastTaskId"));
			TcReadingTask  beforeTask = this.tcReadingTaskMapper.getWaterTaskDetailById(lastTaskId);
			String beforeTaskId = beforeTask.getId();
			if(CommonUtils.isNotEmpty(beforeTask)){
				Integer auditStatus = beforeTask.getAuditStatus();
				//如果是已经审核了的，查找生成的下一条数据
				//如果是没有审核的修改本次数据
				if(auditStatus.equals(MeterDataEnum.STATUS_AUDIT_COMPLETE.getIntValue())){
					TcReadingTask lasttcReadingTask = this.tcReadingTaskMapper.findBylastTaskIdAndProjectId(beforeTaskId);
					if(CommonUtils.isEmpty(lasttcReadingTask)){
						log.info(CommonUtils.log("任务["+beforeTaskId+"]正在审核中，还没有产生下一次抄表任务的数据，请等待一会儿后在操作!"));
						map.put("error", "抄表任务["+beforeTaskId+"]没有审核,还没有产生下一次抄表任务的数据，请等审核该抄表任务或者等待一会儿后在操作!");
					}else{
						//这里只考虑对最近产生的一次任务进行重抄；不考虑，查出的两条最新的重抄任务是针对最近的一次抄表任务；不考虑针对历史抄表任务的重抄
						//所以这里查出来的最后一次抄表任务一定是未审核状态的
						map.put("task", lasttcReadingTask);
						flag="insert";
						map.put("flag", flag);
					}
				}else if(auditStatus.equals(MeterDataEnum.STATUS_AUDITING.getIntValue())){
					//审批中的
					map.put("task", beforeTask);
					flag="update";
					map.put("flag", flag);
				}
			}else{
				map.put("error", "任务编号:["+lastTaskId+"]的任务不存在，可能被删除!,但是又做过重抄所以不能完成工单!");
			}
		}
		return map;
	}
	
	/**
	 * 查询最新的上次读数
	 */
	private Map<String,Object> getNewLastMeterData(WyBusinessContext ctx, String projectId,
			String orderCode, String buildCode, String type) throws ECPBusinessException{
		Map<String,Object> map = new HashMap<String,Object>();
		TcOrderComplaintService tcOrderComplaintService = (TcOrderComplaintService)SpringContextHolder.getBean("tcOrderComplaintServiceImpl");
		BaseDto  baseDto = tcOrderComplaintService.showDtail(ctx, projectId, orderCode, buildCode, type);
		if(baseDto.getMessageMap().getFlag()==MessageMap.INFOR_ERROR){
			map.put("error", baseDto.getMessageMap().getMessage());
			return map;
		}else{
			List<Map<String,Object>> list= baseDto.getLstDto();
			Map<String,Object> mapRes = list.get(1);
			Double lastCommonReading = mapRes.get("lastCommonReading")==null?0.00:Double.parseDouble(String.valueOf(mapRes.get("lastCommonReading")));
			Double lastPeakReading = mapRes.get("lastPeakReading")==null?0.00:Double.parseDouble(String.valueOf(mapRes.get("lastPeakReading")));
			Double lastTotalReading = mapRes.get("lastTotalReading")==null?0.00:Double.parseDouble(String.valueOf(mapRes.get("lastTotalReading")));
			Double lastVallyReading = mapRes.get("lastVallyReading")==null?0.00:Double.parseDouble(String.valueOf(mapRes.get("lastVallyReading")));
			
			map.put("lastCommonReading", lastCommonReading);
			map.put("lastPeakReading", lastPeakReading);
			map.put("lastTotalReading", lastTotalReading);
			map.put("lastVallyReading", lastVallyReading);
		}
		return map;
	}
	
	/**
	 * 作废为重抄完成的重抄任务和meterData表
	 */
	private  void deposeTaskAndMeterData(String id,String meterCode,String orderCode,String projectId){
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
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto insert(String companyId, TcOrderComplete entity) {
		//新建完成工单,需要做的事有:
		//1. 修改投诉工单的关联完成工单号
		TcOrderComplaint complaint =null;
		Map<String,Object> mapComplain =updateComplain(entity);
		Object error = mapComplain.get("error");
		if(CommonUtils.isNotEmpty(error)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,error.toString()));
		}
		Object objComplaint=mapComplain.get("complaint");
		if(CommonUtils.isEmpty(objComplaint)){
			return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"需要完成的工单已经不存在,可能已经被删除!"));
		}
		complaint = (TcOrderComplaint)objComplaint;
		
		
		//2. 修改抄表数据的读数,并计算用量 , 读取时间,读取人
		Map<String, Object> map = this.tcMeterDataMapper.findByTaskIdAndBuildingCode(complaint.getBuildingCode(), complaint.getRelationId());
		TcMeterData data = new TcMeterData();
		try {
			BeanUtils.copyProperties(data, map);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ECPBusinessException("转换失败");
		}
		
		double rate;
		if(1 == data.getMeterType()){
			ElectMeter meter = this.tcElectMeterMapper.getElectMeterByCode(data.getMeterCode(), data.getProjectId());
			rate = (null == meter) ? 1 : meter.getRate();
		}else{
			TcWaterMeter meter = this.tcWaterMeterMapper.getWaterMeterByCode(data.getMeterCode(), data.getProjectId());
			rate = (null == meter) ? 1 : meter.getRate();
		}
		data.setTotalReading(CommonUtils.null2Double(entity.getCompleteContent()));
		//判断异常原因:
		if(1 == entity.getExceptionReason()){ //读数循环,需要计算最大量程
			data.setUseCount(rate * ((Double)map.get("maxAmount") - data.getLastTotalReading() + data.getTotalReading()));
		}else if(2 == entity.getExceptionReason()){ //读数错误,直接计算用量
			data.setUseCount(rate * (data.getTotalReading() - data.getLastTotalReading()));
		}
		data.setReadingTime(new Date());
		data.setReadingPersonId(complaint.getChargePersonId());
		this.tcMeterDataMapper.modify(data);
		
		//2.1 除了重抄任务的读数需要修改之外,生成的本月抄表任务也需要修改它的lastTotalReading为本次完成工单的读数
		TcMeterData currentMonthData = this.tcMeterDataMapper.findCurrentMonthData(data.getBeforeTaskId(),data.getMeterCode());
		currentMonthData.setLastTotalReading(CommonUtils.null2Double(entity.getCompleteContent()));
		this.tcMeterDataMapper.modify(currentMonthData);
		
		//3. 后期制作账单
		//TODO  生成新的账单的逻辑   
		
		//3.1  根据buildingCode获取当前期的账单 , 只有当业主看到了账单 才会发生投诉. 
		TBsChargeBillHistory history = this.tBsChargeBillHistoryMapper.selectCurrentBillByCodeAndType(complaint.getBuildingCode(), 
																										(1 == data.getMeterType()) ? 
																												BillingEnum.SCHEME_TYPE_ELECT.getIntV() : BillingEnum.SCHEME_TYPE_WATER.getIntV());
		if(null != history){
			//3.2  获取到分单之后, 根据新的抄表数据, 以及用量, 重新计费, 并生成新的分单, 将旧的分单进行无效化
			TBsChargeBillHistory newHistory = new TBsChargeBillHistory();
			try {
				BeanUtils.copyProperties(newHistory, history);
			} catch (IllegalAccessException | InvocationTargetException e) {
				log.error("抄表投诉工单完成: 数据转换失败. 原数据:{}",history.toString());
				e.printStackTrace();
			}
			

			history.setIsUsed(BillingEnum.IS_USED_STOP.getIntV());
			
			newHistory.setId(CommonUtils.getUUID());
			newHistory.setAduitStatus(BillingEnum.AUDIT_STATUS_COMPELTE.getIntV());
			newHistory.setCreateTime(new Date());
			newHistory.setCreateId(entity.getComplaintOrderId());	//账单生成人  当前的负责人
			newHistory.setModifyId(entity.getComplaintOrderId());
			newHistory.setModifyTime(new Date());
			
			//TODO 分为水电重新计费.  先退费给相应账户, 再扣取, 注意记录流水信息
			//计算费用
			String chargeError= singleCharging(currentMonthData,newHistory);
			if(CommonUtils.isNotEmpty(chargeError)){
				log.info(CommonUtils.log(chargeError));
				return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,chargeError.toString()));
			}
			
			this.tBsChargeBillHistoryMapper.updateBillHistory(history);
			this.tBsChargeBillHistoryMapper.insertBillHistory(newHistory);
			
			
			//3.3 上传到fastDFS , 然后页面上下载pdf文档
			//3.3.1获取对应的zip包
			Annex annex = this.annexMapper.findCurrentAnnexByRleationId(history.getProjectId());
			
			//对本项目重新打包,上传
			//找到对应的项目
			TBsProject paramObj = new TBsProject();
			paramObj.setBillingTime(CommonUtils.getDate(annex.getAnnexTime(), "yyyy-MM-dd HH:mm:ss"));
			paramObj.setProjectId(history.getProjectId());
			TBsProject project = this.tBsProjectMapper.findByObj(paramObj);

			//数据聚合
			update2Project(project, history.getChargeTotalId());
			
			MqEntity e = new MqEntity();
			e.setCompanyId(companyId);
			e.setData(project);
			e.setOpr(BillingEnum.mq_gen_bill_manaul_regen.getIntV());
			this.amqpTemplate.convertAndSend(route_key_gen_bill, e); //投递至BillMgrListener
			log.info("定时任务 : 数据投递至消息队列, 账单重新生成 , routeKey : {} , 数据: {}",route_key_gen_bill,entity.toString());
		
		}
		
		
		//4. 任务状态修改, 完成, 审核通过
		TcReadingTask task = this.tcReadingTaskMapper.getWaterTaskDetailById(data.getTaskId());
		task.setStatus(5);  //重抄任务完成状态
		task.setEndTime(new Date());
		task.setAuditStatus(1);
		task.setCompleteMeterCount(task.getTotalMeterCount());
		task.setRemainMeterCount(0);
		this.tcReadingTaskMapper.update(task);
		//5. 完成工单的id为 投诉工单的id
		entity.setId(entity.getComplaintOrderId());
		entity.setCreateTime(new Date());
		entity.setModifyTime(new Date());
		entity.setRelationId(task.getId());
		this.tcOrderCompleteMapper.insert(entity);
		return new BaseDto(new MessageMap(null,"工单保存完成."));
	}
	
	private void update2Project(TBsProject p , String chargeTotalId){
		if(null == p)
			return;
		
		//对有欠费记录的分单进行违约金聚合 , 记录在新的账单里面, billing_time为null
//		if(null != p.getProjectId()){
//			this.tBsChargeBillHistoryMapper.updateLateFeeByProjectId(p.getProjectId());
//		}
		
		p.setCurrentFee(0.0);
		p.setLastOwedFee(0.0);
		p.setTotalFee(0.0);
		
		TBsChargeBillTotal paramTotal = this.tBsChargeBillTotalMapper.selectById(chargeTotalId);
		
		paramTotal.setProjectId(p.getProjectId());
		paramTotal.setBillingTime(p.getBillingTime());
		
		//判断四个费种是否都已经计费完成
		if(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV() == p.getWyStatus()){
			paramTotal.setType(BillingEnum.ACCOUNT_TYPE_WY.getIntV());
			sumTotal(paramTotal, p);
		}
		if(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV() == p.getBtStatus()){
			paramTotal.setType(BillingEnum.ACCOUNT_TYPE_BT.getIntV());
			sumTotal(paramTotal, p);
		}
		if(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV() == p.getWaterStatus()){
			paramTotal.setType(BillingEnum.ACCOUNT_TYPE_WATER.getIntV());
			sumTotal(paramTotal, p);
		}
		if(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV() == p.getElectStatus()){
			paramTotal.setType(BillingEnum.ACCOUNT_TYPE_ELECT.getIntV());
			sumTotal(paramTotal, p);
		}
		
		this.tBsProjectMapper.update(p);
	}
	
	private void sumTotal(TBsChargeBillTotal paramTotal,TBsProject p){
		TBsChargeBillTotal total = this.tBsChargeBillTotalMapper.findBilledTotal(paramTotal);
		if(null != total){
			TBsChargeBillTotal nextTotal = this.tBsChargeBillTotalMapper.findNextBillTotal(total.getId());
			double allLastPayed = this.tBsChargeBillHistoryMapper.findAllPayedByTotalId(nextTotal.getId());
			if(total.getTotalFee() < allLastPayed){
				nextTotal.setLastOwedFee(total.getTotalFee() - allLastPayed);
				this.tBsChargeBillTotalMapper.update(nextTotal);
			}
			p.setCurrentFee(p.getCurrentFee() + total.getCurrentFee()); 
			p.setLastOwedFee(p.getLastOwedFee() + total.getLastOwedFee());
			p.setTotalFee(p.getTotalFee() + total.getTotalFee());
		}
	}
	
	/**
	 * 修改投诉工单的关联完成工单号
	 */
	private Map<String,Object> updateComplain(TcOrderComplete entity){
		Map<String,Object> map = new HashMap<String,Object>();
		TcOrderComplaint complaint=null;
		 complaint = this.tcOrderComplaintMapper.findById(entity.getComplaintOrderId());
		 if(CommonUtils.isNotEmpty(complaint)){
			 if(CommonUtils.isNotEmpty(complaint.getOrderStatus()) && complaint.getOrderStatus().equals(TcOrderComplaintAndCompleteEnum.ORDER_STATUS_DESPOSE.getIntV())){
				 map.put("error", "该工单已经作废，不能完成该工单!");
			 }else{
				 complaint.setCompleteOrderId(entity.getComplaintOrderId());  //设置工单与完成工单id一致
					complaint.setOrderStatus(2); //完成状态
					complaint.setModifyTime(new Date());
					complaint.setModifyId(entity.getCreateId());
					complaint.setId(entity.getComplaintOrderId());
					this.tcOrderComplaintMapper.update(complaint);
					map.put("complaint", complaint);
			 }
		 }
		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto findById(String companyId, String id) {
		BaseDto dto = new BaseDto();
		dto.setObj(this.tcOrderCompleteMapper.findById(id));
		return dto;
	}

	/**
	 * @TODO 根据传入的完成工单主键,获取投诉,完成工单,附件信息以及抄表数据
	 */
	@Override
	public BaseDto findDetailById(String companyId, String id) {
		Map<String,Object> resultMap = this.tcOrderCompleteMapper.findDetailById(id);
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(resultMap);
		return returnDto;
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
	
	
	/**
	 * 单个收费对象重新计费
	 */
	private String singleCharging(TcMeterData currentMonthData,TBsChargeBillHistory newHistory){
		//根据newHistory查找总单上的方案，根据方案找规则，规矩规则查找收费项
		String error ="";
		List<TBsChargeType> chargeTypeList = this.tBsChargeTypeMapper.findBytoIdAndBuildCodeAndProId(newHistory.getChargeTotalId(), newHistory.getBuildingCode(), newHistory.getProjectId());
		if(CollectionUtils.isEmpty(chargeTypeList)){
			error="建筑名称为:["+newHistory.getFullName()+"]没有维护相关计费公式,不能计费;有可能是原计费方案被删除,请检查!";
			return error;
		}
		 TBsChargeBillTotal  currentTotalBill =this.tBsChargeBillTotalMapper.findTbsTotalbyId(newHistory.getChargeTotalId());
		 TBsChargingScheme scheme=  this.tBsChargingSchemeMapper.findById(currentTotalBill.getSchemeId());
		 Double currentFee = 0.0;
		 Double CountValue =0.0;
		 String feeItem="";
		 List<FeeItemDetail> list = new ArrayList<FeeItemDetail>();
		for(TBsChargeType tBsChargeType:chargeTypeList){
			//得到公式
			Double minCriticalpoint = tBsChargeType.getMinCriticalpoint();
			Double maxCriticalpoint = tBsChargeType.getMaxCriticalpoint();
			String formulaInfoValue= tBsChargeType.getFormulaInfoValue(); //一个公式里面只能有一个计费项目
			if(StringUtils.isBlank(formulaInfoValue)){
				log.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"公式为空,不能计算，请先维护公式!"));
				continue;
			}
				String[] formulaArray = formulaInfoValue.split(" ");
				Double userCount =Double.parseDouble(String.valueOf(CommonUtils.isEmpty(currentMonthData.getUseCount())?"0":currentMonthData.getUseCount()));
				Double peakCount = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(currentMonthData.getPeakCount())?"0":currentMonthData.getPeakCount()));
				Double vallyCount = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(currentMonthData.getVallyCount())?"0":currentMonthData.getVallyCount()));
				Double commonCount = Double.parseDouble(String.valueOf(CommonUtils.isEmpty(currentMonthData.getCommonCount())?"0":currentMonthData.getCommonCount()));
				//确定计费项目
				
				for(int j=0;j<formulaArray.length;j++){
					if(formulaArray[j].equals("$Count")){
						feeItem="$Count";
					}
					if(formulaArray[j].equals("$PeakCount")){
						feeItem="$PeakCount";
					}
					if(formulaArray[j].equals("$VallCount")){
						feeItem="$VallCount";
					}
					if(formulaArray[j].equals("$CommCount")){
						feeItem="$CommCount";
					}
				}
				if(feeItem.equals("$Count")){
					boolean flag = judgeCriticalpoint(minCriticalpoint,maxCriticalpoint,userCount);
					if(flag ==false) continue;
				}
				
				if(feeItem.equals("$PeakCount")){
					boolean flag = judgeCriticalpoint(minCriticalpoint,maxCriticalpoint,peakCount);
					if(flag ==false) continue;
				}
				
				if(feeItem.equals("$VallCount")){
					boolean flag = judgeCriticalpoint(minCriticalpoint,maxCriticalpoint,vallyCount);
					if(flag ==false) continue;
				}
				
				if(feeItem.equals("$CommCount")){
					boolean flag = judgeCriticalpoint(minCriticalpoint,maxCriticalpoint,commonCount);
					if(flag ==false) continue;
				}
				Object obj = FormulaCalculationUtil.waterElectCalculation(formulaInfoValue,userCount, peakCount, vallyCount, commonCount);
				if(CommonUtils.isEmpty(obj)){
					log.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"公式有误,不能做计算!"));
					continue;
				}else{
					if(obj.toString().equals("Infinity")){
						log.info(String.format("当前时间 : %s , 异常 -> %s" ,CommonUtils.getDateStr(),"公式计算有误,除数不能为0!"));
						continue;
					}else{
						CountValue = CountValue+Double.parseDouble(String.valueOf(obj));
					}
				}
				currentFee = new BigDecimal(currentFee).setScale(10,BigDecimal.ROUND_HALF_UP).doubleValue() + CountValue;
				FeeItemDetail feeItemDetail = new FeeItemDetail();
				feeItemDetail.setFeeName(tBsChargeType.getChargingName());
				feeItemDetail.setCountValue(String.valueOf(CountValue));
				list.add(feeItemDetail);
		}
		newHistory.setCurrentFee(new BigDecimal(String.valueOf(currentFee)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()); 	//本期产生金额//本期产生金额//本期产生金额
		JSONObject json = new JSONObject();
		newHistory.setFeeItemDetail(json.toJSONString(list)); //设置各个费用项组成的json字符串
		//本期总账单 计算公式: 上期总账单   - 上期已付(上期已付最多把上期抹平,剩余全部充入该种账户余额) + 本期计费 + 本期分摊 + 本期违约金
		double currBillFee = CommonUtils.null2Double(newHistory.getLastBillFee()) 
						   - CommonUtils.null2Double(newHistory.getLastPayed()) 
						   + CommonUtils.null2Double(newHistory.getShareFee()) 
						   + CommonUtils.null2Double(newHistory.getLateFee())
						   + newHistory.getCurrentFee();
		newHistory.setCurrentBillFee(currBillFee);
		String temporaryBill = newHistory.getTemporaryBill();
		if(StringUtils.isNotBlank(temporaryBill)){//表示，临时计费过，这个分单还需要计费计费和扣费
			//如果是先临时计费过，则也需要将本次的正常计费写入临时字段中
			currentTotalBill.setAuditStatus(BillingEnum.AUDIT_STATUS_WAITING.getIntV());
			List<TempCurrentFee> listTempCurrentFee =new ArrayList<TempCurrentFee>();
			 listTempCurrentFee= json.parseArray(temporaryBill, TempCurrentFee.class);
			 TempCurrentFee tempCurrentFee = new TempCurrentFee();
			 tempCurrentFee.setId(CommonUtils.getUUID());
			 tempCurrentFee.setCurrentFee(newHistory.getCurrentFee());
			 listTempCurrentFee.add(tempCurrentFee);
			 newHistory.setTemporaryBill(json.toJSONString(listTempCurrentFee)); //记录这次临时计费
		};
		
		
		newHistory.setBillingTime(new Date());
		newHistory.setTax(new BigDecimal(String.valueOf((currBillFee - CommonUtils.null2Double(newHistory.getLateFee()) ) * scheme.getTaxRate() / 100)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()); //税金计算
		this.tBsChargeBillTotalMapper.update(currentTotalBill);
		return error;
	}
	
	/**
	 * 判断临界点
	 * @param feeItem
	 * @param minCriticalpoint
	 * @param maxCriticalpoint
	 * @param dosage
	 * @return
	 */
	private Boolean judgeCriticalpoint(Double minCriticalpoint,Double maxCriticalpoint,Double dosage){
		boolean flag=true;
		if(null == minCriticalpoint || null == maxCriticalpoint){
			flag=false;
			log.info("临界点为空;不能计费!");
			return flag;
		}
		if(minCriticalpoint==0 && maxCriticalpoint==0){
			flag=true;
			return flag;
		}
		if(minCriticalpoint !=0 && maxCriticalpoint==0){
			if(dosage<minCriticalpoint){
				flag = false;
				log.info(CommonUtils.log("计费项目["+dosage+"]不在["+minCriticalpoint+","+"+∞]范围内!"));
				return flag;
			}
		}
		if(minCriticalpoint ==0 && maxCriticalpoint !=0){
			if(dosage>maxCriticalpoint){
				flag = false;
				log.info(CommonUtils.log("计费项目["+dosage+"]不在["+0+","+maxCriticalpoint+"]范围内!"));
				return flag;
			}
		}
		if(minCriticalpoint !=0 && maxCriticalpoint !=0){
			if(dosage <=minCriticalpoint || dosage>maxCriticalpoint){
				flag = false;
				log.info(CommonUtils.log("计费项目["+dosage+"]不在["+minCriticalpoint+","+maxCriticalpoint+"]范围内!"));
				return flag;
			}
		}
		return flag;
	}

    @Override
    public List<MyWorkOrderDTO> queryMyWorkOrder(String companyId, String accountId, String description, String date, int pageSize, int pageNo) {
        pageNo = pageNo < 1 ? 1 : pageNo;
        int limit = (pageNo-1)*pageSize;
        return tcOrderMapper.queryMyWorkOrder(accountId, description, date,limit,pageSize);
    }

    @Override
    public int updateProcessedWorkOrder(String companyId, String orderCode, String status, String processDate,String procesInstructions,String updateBy) {
        return tcOrderMapper.updateProcessedWorkOrder(orderCode,status,processDate,procesInstructions,updateBy);
    }

	@Override
	public TcOrder selectById(String companyId,String orderId) {
		TcOrderExample tcOrderExample = new TcOrderExample();
		tcOrderExample.createCriteria().andOrderCodeEqualTo(orderId);
		List<TcOrder> tcOrders = tcOrderMapper.selectByExample(tcOrderExample);
		if(tcOrders!=null&&tcOrders.size()>0){
			return tcOrders.get(0);
		}else {
			return null;
		}
	}

}
