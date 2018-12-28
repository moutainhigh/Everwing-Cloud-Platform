package com.everwing.coreservice.wy.core.service.impl.configuration.project;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.ThreadPool.ThreadPoolUtils;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.datasource.DataSourceUtil;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsProjectService;
import com.everwing.coreservice.common.wy.service.configuration.task.WaterAndElectBillingTaskService;
import com.everwing.coreservice.common.wy.service.configuration.task.WyBillingTaskService;
import com.everwing.coreservice.platform.api.CompanyApi;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("tBsProjectService")
public class TBsProjectServiceImpl extends Resources implements TBsProjectService {

	private static final Logger logger= LogManager.getLogger(TBsProjectServiceImpl.class);

	@Autowired
	private CompanyApi companyApi;
	
	private static Map<String,String> projectIdAndCompanyIdMap = new HashMap<String,String>(); //防止多次点击手工计费

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseDto listPageProject(String companyId, TBsProject project) {
		return new BaseDto(tBsProjectMapper.listPageProject(project), project.getPage());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto findById(String companyId, String id) {
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(tBsProjectMapper.findById(id));
		return returnDto;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto update(String companyId, TBsProject project) {
		//TODO 修改物业管理费, 本体基金 , 水电费的优先级
		tBsProjectMapper.update(project); 
		return new BaseDto(new MessageMap(null, "修改成功"));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto countProject(String companyId) {
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(tBsProjectMapper.countProject());
		return returnDto;
	}

	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto manualBilling(final String companyId, List<TBsProject> projects) throws ECPBusinessException{
		MessageMap msgMap = new MessageMap();
		try {
			//校验某项目是否正在手动计费，
			for(TBsProject tbs:projects){
				if(projectIdAndCompanyIdMap.containsKey(companyId+"_"+tbs.getProjectId())){
					msgMap.setFlag(MessageMap.INFOR_WARNING);
					msgMap.setMessage("项目["+tbs.getProjectName()+"]正在手动计费,请稍后再操作!");
					return new BaseDto(msgMap);
				}
			}
			
			for(TBsProject tbs:projects){
				projectIdAndCompanyIdMap.put(companyId+"_"+tbs.getProjectId(), companyId+"_"+tbs.getProjectId());
			}
			
			ThreadPoolUtils util = ThreadPoolUtils.getInstance();
			
			//对项目进行异步线程计算, 项目内对开启计费的收费项目   起线程计算, 线程计算完成后,状态回写到项目的状态
			List<String> noStartProjectNames = new ArrayList<String>(); //记录未启动的项目名称
			final List<TBsProject> startProject = new ArrayList<TBsProject>(); //记录以及启动了的项目
			for(final TBsProject project : projects){
				if(1 == project.getStatus()){
					noStartProjectNames.add(project.getProjectName());
				}else{
					startProject.add(project);
				}
			}
			
			//当所有项目都没有开启则返回。
			if(noStartProjectNames.size() == projects.size()){
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("所传入的项目全部没有启动;不能手动计费!");
				for(TBsProject tbs:projects){
					projectIdAndCompanyIdMap.remove(companyId+"_"+tbs.getProjectId());
				}
				return new BaseDto(msgMap);
			}
			
			if(startProject.size()>0){
				RemoteModelResult<Company> rslt =  this.companyApi.queryCompany(companyId);
				if(!rslt.isSuccess() || rslt.getModel() == null){
					logger.warn("切换数据源失败。");
					return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"切换数据源失败。"));
				}
				final String companyStr = JSON.toJSONString(rslt.getModel());
				util.executeThread(new Runnable() {
					@Override
					public void run() {
						DataSourceUtil.changeDataSource(companyStr); //切换数据源
						WaterAndElectBillingTaskService  waterAndElectBillingTaskService = SpringContextHolder.getBean("electFeeBillingTaskServiceImpl");
						WyBillingTaskService wyBillingTaskService = SpringContextHolder.getBean("wyBillingTaskService");
						
						//每个项目的物业，本体，水，电都单独调用
						//业务
						for(TBsProject project:startProject){
							if(project.getWyStatus() !=BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV()){
								logger.info(CommonUtils.log("项目["+project.getProjectName()+"]的物业管理费计费状态不为'未计费状态',不能进行手动计费"));
								continue;
							}else{
								wyBillingTaskService.manualBilling(companyId, project, BillingEnum.SCHEME_TYPE_WY.getIntV());
							}
						}
						//本体
						for(TBsProject project:startProject){
							if(project.getWyStatus() !=BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV()){
								logger.info(CommonUtils.log("项目["+project.getProjectName()+"]的本体基金计费状态不为'未计费状态',不能进行手动计费"));
								continue;
							}else{
								wyBillingTaskService.manualBilling(companyId, project, BillingEnum.SCHEME_TYPE_BT.getIntV());
							}
						}
						
						//水费
						for(TBsProject project:startProject){
							if(project.getWyStatus() !=BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV()){
								logger.info(CommonUtils.log("项目["+project.getProjectName()+"]的水费状态不为'未计费状态',不能进行手动计费"));
								continue;
							}else{
								waterAndElectBillingTaskService.manualElectBilling(companyId, "water", project);
							}
						}
						
						//电费
						for(TBsProject project:startProject){
							if(project.getWyStatus() !=BillingEnum.PROJECT_BILLING_STATUS_NOT_BILLING.getIntV()){
								logger.info(CommonUtils.log("项目["+project.getProjectName()+"]的电费状态不为'未计费状态',不能进行手动计费"));
								continue;
							}else{
								waterAndElectBillingTaskService.manualElectBilling(companyId, "elect", project);
							}
						}
						
					}
				});
			}
			if(startProject.size()==projects.size()){ //所有项目都已经开启
				msgMap.setFlag(MessageMap.INFOR_SUCCESS);
				msgMap.setMessage("开始异步手动计费,请耐心等待,请不要重复点击手动计费.");
				for(TBsProject tbs:projects){
					projectIdAndCompanyIdMap.remove(companyId+"_"+tbs.getProjectId());
				}
				return new BaseDto(msgMap);
			}
			if(startProject.size()>0 && noStartProjectNames.size()>0){
				String startProjectName="";
				String noStartProject="";
				for(TBsProject tbp:startProject){
					if(StringUtils.isBlank(startProjectName)){
						startProjectName = tbp.getProjectName();
					}else{
						startProjectName=startProjectName+","+tbp.getProjectName();
					}
				}
			   for(String name:noStartProjectNames){
				   if(StringUtils.isBlank(noStartProject)){
					   noStartProject = name;
				   }else{
					   noStartProject = noStartProject+","+name;
				   }
			   }
			   msgMap.setFlag(MessageMap.INFOR_WARNING);
			   msgMap.setMessage("项目名称["+startProjectName+"]开始异步手动计费,请耐心等待,请不要重复点击手动计费;项目名称["+noStartProject+"]没有开启,不能手动计费");
			   for(TBsProject tbs:projects){
					projectIdAndCompanyIdMap.remove(companyId+"_"+tbs.getProjectId());
				}
			   return new BaseDto(msgMap);
			}
			for(TBsProject tbs:projects){
				projectIdAndCompanyIdMap.remove(companyId+"_"+tbs.getProjectId());
			}
			return new BaseDto(msgMap);
		} catch (Exception e) {
			for(TBsProject tbs:projects){
				projectIdAndCompanyIdMap.remove(companyId+"_"+tbs.getProjectId());
			}
			logger.info(CommonUtils.log(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}

	@Override
	public List<TBsProject> findCanBillingCmacProject(String companyId) {
		return this.tBsProjectMapper.findCanBillingCmacProject(); 
	}


	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BaseDto autoFlush(String companyId) {
		
		//自动将过期的scheme置为无效
		this.tBsChargingSchemeMapper.autoStopScheme(); 
		
		//找出该公司下所有本月的计费项目
		TBsProject paramObj = new TBsProject();
		paramObj.setBillingTime(new Date());
		paramObj.setStatus(BillingEnum.IS_USED_USING.getIntV());;
		List<TBsProject> projects = this.tBsProjectMapper.findAllByObj(paramObj);
		
//		TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
		if(CommonUtils.isNotEmpty(projects)){
			for(TBsProject p : projects){
				
				//对有欠费记录的分单进行违约金聚合 , 记录在新的账单里面, billing_time为null
				if(null != p.getProjectId()){
					this.tBsChargeBillHistoryMapper.updateLateFeeByProjectId(p.getProjectId());
				}
				
				p.setCurrentFee(0.0);
				p.setLastOwedFee(0.0);
				p.setTotalFee(0.0);
				
				//直接聚合t_bs_project, 计费完成的,已经产生下个月的任务的
				this.tBsProjectMapper.flushItems(p.getProjectId());
				
				
				//判断四个费种是否都已经计费完成
				/*paramTotal.setProjectId(p.getProjectId());
				paramTotal.setBillingTime(p.getBillingTime());*/
				/*if(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV() == p.getWyStatus()){
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
				
				this.tBsProjectMapper.update(p);*/
			}
		}
		return new BaseDto(new MessageMap(null,"项目自动刷新: 刷新完成"));
	}
	
	
	
	private void sumTotal(TBsChargeBillTotal paramTotal,TBsProject p){
		TBsChargeBillTotal total = this.tBsChargeBillTotalMapper.findBilledTotal(paramTotal);
		
		if(null != total){
			
			TBsChargeBillTotal nextTotal = this.tBsChargeBillTotalMapper.findNextBillTotal(total.getId());
			double allLastPayed = CommonUtils.null2Double(this.tBsChargeBillHistoryMapper.findAllPayedByTotalId(nextTotal.getId()));
			if(CommonUtils.null2Double(total.getTotalFee()) < allLastPayed){
				nextTotal.setLastOwedFee(CommonUtils.null2Double(total.getTotalFee()) - allLastPayed);
				this.tBsChargeBillTotalMapper.update(nextTotal);
			}
			
			p.setCurrentFee(CommonUtils.null2Double(p.getCurrentFee()) + CommonUtils.null2Double(total.getCurrentFee())); 
			p.setLastOwedFee(CommonUtils.null2Double(p.getLastOwedFee()) + CommonUtils.null2Double(total.getLastOwedFee()));
			p.setTotalFee(CommonUtils.null2Double(p.getTotalFee()) + CommonUtils.null2Double(total.getTotalFee()));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDto findCanGenBillProject(String companyId) {
		List<TBsProject> projects = this.tBsProjectMapper.findCanGenBillProject();
		
		List<TBsProject> returnList = new ArrayList<TBsProject>();
		if(CommonUtils.isNotEmpty(projects)){
			
			List<String> types = new ArrayList<String>();
			
			for(TBsProject p : projects){
				types.clear();
				if(p.getWyStatus() != BillingEnum.PROJECT_BILLING_STATUS_STOP.getIntV()) types.add(Constants.STR_ONE);
				if(p.getBtStatus() != BillingEnum.PROJECT_BILLING_STATUS_STOP.getIntV()) types.add(Constants.STR_TWO);
				if(p.getWaterStatus() != BillingEnum.PROJECT_BILLING_STATUS_STOP.getIntV()) types.add(Constants.STR_THREE);
				if(p.getElectStatus() != BillingEnum.PROJECT_BILLING_STATUS_STOP.getIntV()) types.add(Constants.STR_FOUR);
				
				if(CommonUtils.isEmpty(types)) continue;
				
				//寻找到该计费项目下的所有已审核总账单
				Integer count = this.tBsChargeBillTotalMapper.getAuditedCountByProjectIdAndTypes(p.getProjectId(),types);
				
				if(count == types.size()){
					returnList.add(p);
				}
			}
		}
		BaseDto dto = new BaseDto();
		dto.setLstDto(returnList);
		return dto;
	}

	@Override
	public BaseDto findCommonStatus(String companyId, String userId) {
		Integer commonStatus = this.tBsProjectMapper.findCommonStatus(userId);
		if(commonStatus == null) 
			commonStatus = 1;
		BaseDto dto = new BaseDto();
		dto.setObj(commonStatus);
		dto.setMessageMap(new MessageMap(null,"查询成功"));
		return dto;
	}

	@Override
	public BaseDto findByObj(String companyId, TBsProject project) {
		return new BaseDto(this.tBsProjectMapper.findAllByObj(project),null);
	}
	
}

