package com.everwing.coreservice.wy.core.service.impl.configuration.task;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.service.configuration.task.TBsProjectTaskService;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("tBsProjectTaskService")
public class TBsProjectTaskServiceImpl extends Resources implements TBsProjectTaskService{

	private static final Logger logger = LogManager.getLogger(TBsProjectTaskServiceImpl.class);
	
	private static final String PRE_LOG = "定时任务: [ 扫描生成下月计费计划 ] ";
	
	private static final String AUTO_GENER = "system";
	
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto genNextProject(String companyId) {
		
		logger.info("{}  启动",PRE_LOG);
		MessageMap msgMap = new MessageMap();
		List<TBsProject> needGenProjects = this.tBsProjectMapper.findNeedGenProjects();
		
		if(CommonUtils.isEmpty(needGenProjects)){
			msgMap.setFlag(MessageMap.INFOR_WARNING);
			msgMap.setMessage("定时任务: [ 扫描生成下月计费计划 ]  完成  : 未找到可复制的本月计划");
			logger.info(msgMap.getMessage());
			return new BaseDto(msgMap);
		}
		
		List<TBsProject> nextProjects = new ArrayList<TBsProject>();
		for(TBsProject p : needGenProjects){
			if(null == p){
				continue;
			}
			TBsProject nextProject = new TBsProject();
			nextProject.setId(CommonUtils.getUUID());
			nextProject.setBillingTime(CommonUtils.addMonth(p.getBillingTime(), 1));
			nextProject.setCreateId(AUTO_GENER);
			nextProject.setCreateTime(new Date());
			nextProject.setModifyId(AUTO_GENER);
			nextProject.setModifyTime(new Date());
			
			//status需要判断上月的状态 如果为0 则是未启动 , 如果为2/1 就是启动计费, 设置为1即可
			nextProject.setWaterStatus( (0 == p.getWaterStatus()) ? 0 : 1);
			nextProject.setBtStatus( (0 == p.getBtStatus()) ? 0 : 1);
			nextProject.setWyStatus( (0 == p.getWyStatus()) ? 0 : 1);
			nextProject.setElectStatus( (0 == p.getElectStatus()) ? 0 : 1);
			
			nextProject.setIsGenBill(BillingEnum.bill_is_gen_no.getIntV());
			nextProject.setProjectId(p.getProjectId());
			nextProject.setProjectName(p.getProjectName());
			nextProject.setBtOrder(p.getBtOrder());
			nextProject.setCommonStatus(p.getCommonStatus());
			nextProject.setCurrentFee(0.0);
			nextProject.setElectOrder(p.getElectOrder());
			nextProject.setLastOwedFee(0.0);
			nextProject.setStatus(p.getStatus());
			nextProject.setTotalFee(0.0);
			nextProject.setWaterOrder(p.getWaterOrder());
			nextProject.setWyOrder(p.getWyOrder());
			nextProjects.add(nextProject);
		}
		
		if(!nextProjects.isEmpty())
			this.tBsProjectMapper.batchInsert(nextProjects);
		
		msgMap.setMessage("定时任务 : [ 扫描生成下月计费计划 ] 完成 ");
		return new BaseDto(msgMap);
	}

	
	
	
	
	
}
