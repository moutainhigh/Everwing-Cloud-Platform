package com.everwing.coreservice.wy.core.service.impl.configuration.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.entity.MqEntity;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.service.configuration.project.SumMmeterBillToProjectService;
import com.everwing.coreservice.wy.dao.mapper.business.electmeter.TcElectMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.business.meterdata.TcMeterDataMapper;
import com.everwing.coreservice.wy.dao.mapper.business.watermeter.TcWaterMeterMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.TBsProjectMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillHistoryMapper;
import com.everwing.coreservice.wy.dao.mapper.configuration.bill.TBsChargeBillTotalMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sumMmeterBillToProjectServiceImpl")
public class SumMmeterBillToProjectServiceImpl implements SumMmeterBillToProjectService{

	@Autowired
	private TBsProjectMapper tBsProjectMapper;
	@Autowired
	private TBsChargeBillTotalMapper tBsChargeBillTotalMapper;
	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;
	@Autowired
	private TcWaterMeterMapper tcWaterMeterMapper;
	@Autowired
	private TcElectMeterMapper tcElectMeterMapper;
	@Autowired
	private TcMeterDataMapper tcMeterDataMapper;

	private static final Logger logger = Logger.getLogger(SumMmeterBillToProjectServiceImpl.class);
	//消息队列声明处
	@Value("${queue.wy2Wy.sumbill.fictitiousMeter.key}")
	private String queue_wy2Wy_sumbill_fictitiousMeter_key;
	
	@Override
	public BaseDto findTotalBill(String companyId) {
//		Map<String,Object> paramMap = new HashMap<String,Object>();
//		paramMap.put("status", BillingEnum.STATUS_START.getIntV());								//项目启用状态
//		paramMap.put("isUsed", BillingEnum.IS_USED_USING.getIntV());							//计费方案可用状态
//		new Ti
		List<TBsProject> projects = this.tBsProjectMapper.findCurrentAllUseProjects(new DateTime().toString("yyyy-MM-dd"));	
		for(TBsProject tbsProject : projects){
			//查询总单(根据项目编号和审核状态不为审核完成的状态查询!)
			List<TBsChargeBillTotal> listTotal= this.tBsChargeBillTotalMapper.findNoAduitByProjectId(tbsProject.getProjectId());
			for(TBsChargeBillTotal total:listTotal){
				logger.info(CommonUtils.log("总单编号["+total.getId()+"]发送至mq进行M级虚拟表费用汇总"));
				sendMessage(companyId,total); //发送到Mq
			}
			
		}
		
		
		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"成功!"));
	}
	
	//发送至mq
	private void sendMessage(String companyId , TBsChargeBillTotal total){
		if(null != total){
			MqEntity e = new MqEntity(BillingEnum.data_update.getIntV(), total);
			e.setCompanyId(companyId);
			this.amqpTemplate.convertAndSend(queue_wy2Wy_sumbill_fictitiousMeter_key, e);
		}
	}

	@Override
	public BaseDto sumTotalBill(String companyId, TBsChargeBillTotal total) {
		BaseDto baseDto = new BaseDto();
		String projectId=total.getProjectId();
		MessageMap mm = new MessageMap();
		//根据总单Id查找是M级虚拟表的分单
		if(total.getType().equals(3)){//水表
			List<TBsChargeBillHistory> mList= tBsChargeBillHistoryMapper.selectMByTotalId(total.getProjectId(),total.getId(),total.getType());
			for(TBsChargeBillHistory mhistory : mList){
				//找M级总表 再找分表；然后汇总
				TcWaterMeter tcWaterMeter = tcWaterMeterMapper.findMByBuildCodeAndProjectId(mhistory.getProjectId(), mhistory.getBuildingCode());
				//汇总用量


				if(CommonUtils.isNotEmpty(tcWaterMeter)){
					double totalUseCount=tcMeterDataMapper.sumUseCountByMeterCodeAndType(projectId,tcWaterMeter.getCode(),0);
					tcMeterDataMapper.updateUseCountToM(projectId,tcWaterMeter.getCode(),totalUseCount,0);
					//查找分表
					List<String> relationIds = this.tcWaterMeterMapper.findRelationIdByparentCode(tcWaterMeter.getCode(),tcWaterMeter.getProjectId());
					if(CollectionUtils.isNotEmpty(relationIds)){
						//查找子表的分单
						List<TBsChargeBillHistory> cList = this.tBsChargeBillHistoryMapper.selectCByBuildCodeAndTotalId(relationIds,total.getId());
						if(CollectionUtils.isNotEmpty(cList)){
							for(TBsChargeBillHistory chisory:cList){
								convert(mhistory,chisory);
							}
						}
					}
				}
				this.tBsChargeBillHistoryMapper.updateBillHistory(mhistory);
			}
			total.setSunStatus(1); //已汇总状态
			tBsChargeBillTotalMapper.update(total);
		}else if(total.getType().equals(4)){//电表
			List<TBsChargeBillHistory> mList= tBsChargeBillHistoryMapper.selectMByTotalId(total.getProjectId(),total.getId(),total.getType());
			for(TBsChargeBillHistory mhistory : mList){
				//找M级总表 再找分表；然后汇总
				ElectMeter electMeter = tcElectMeterMapper.findMByBuildCodeAndProjectId(mhistory.getProjectId(), mhistory.getBuildingCode());
				//汇总用量

				if(CommonUtils.isNotEmpty(electMeter)){
					double totalUseCount=tcMeterDataMapper.sumUseCountByMeterCodeAndType(projectId,electMeter.getCode(),1);
					tcMeterDataMapper.updateUseCountToM(projectId,electMeter.getCode(),totalUseCount,1);
					//查找分表
					List<String> relationIds = this.tcElectMeterMapper.findRelationIdByparentCode(electMeter.getCode(),electMeter.getProjectId());
					if(CollectionUtils.isNotEmpty(relationIds)){
						//查找子表的分单
						List<TBsChargeBillHistory> cList = this.tBsChargeBillHistoryMapper.selectCByBuildCodeAndTotalId(relationIds,total.getId());
						if(CollectionUtils.isNotEmpty(cList)){
							for(TBsChargeBillHistory chisory:cList){
								convert(mhistory,chisory);
							}
						}
					}
				}
				this.tBsChargeBillHistoryMapper.updateBillHistory(mhistory);
			}
			total.setSunStatus(1); //已汇总状态
			tBsChargeBillTotalMapper.update(total);
		}
		return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"成功"));
	}
	
	
	private void convert(TBsChargeBillHistory mhistory,TBsChargeBillHistory chisory){
		mhistory.setAccountBalance(0.00);//M虚拟表没有账户
		mhistory.setCurrentBillFee(CommonUtils.null2Double(mhistory.getCurrentBillFee())+CommonUtils.null2Double(chisory.getCurrentBillFee()));
		mhistory.setCurrentFee(CommonUtils.null2Double(mhistory.getCurrentFee())+CommonUtils.null2Double(chisory.getCurrentFee()));
		mhistory.setLastBillFee(CommonUtils.null2Double(mhistory.getLastBillFee())+CommonUtils.null2Double(chisory.getLastBillFee()));
		mhistory.setLastPayed(CommonUtils.null2Double(mhistory.getLastPayed())+CommonUtils.null2Double(chisory.getLastPayed()));
		mhistory.setLateFee(CommonUtils.null2Double(mhistory.getLateFee())+CommonUtils.null2Double(chisory.getLateFee()));
		mhistory.setShareFee(CommonUtils.null2Double(mhistory.getShareFee())+CommonUtils.null2Double(chisory.getShareFee()));
		mhistory.setCommonDesummoney(CommonUtils.null2Double(mhistory.getCommonDesummoney())+CommonUtils.null2Double(chisory.getCommonDesummoney()));
		mhistory.setNoCommonDesummoney(CommonUtils.null2Double(mhistory.getNoCommonDesummoney())+CommonUtils.null2Double(chisory.getNoCommonDesummoney()));
		mhistory.setTax(CommonUtils.null2Double(mhistory.getTax())+CommonUtils.null2Double(chisory.getTax()));
		mhistory.setFeeItemDetail(mhistory.getFeeItemDetail()+chisory.getFeeItemDetail());
	}

}
