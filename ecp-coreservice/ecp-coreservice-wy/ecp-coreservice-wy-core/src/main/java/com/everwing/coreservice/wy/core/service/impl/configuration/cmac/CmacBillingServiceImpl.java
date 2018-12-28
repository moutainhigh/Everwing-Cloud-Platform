package com.everwing.coreservice.wy.core.service.impl.configuration.cmac;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.service.configuration.cmac.CmacBillingService;
import com.everwing.coreservice.common.wy.service.configuration.cmac.service.*;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("cmacBillingService")
public class CmacBillingServiceImpl extends Resources implements CmacBillingService{
	
	private static final Logger logger = LogManager.getLogger(CmacBillingServiceImpl.class);
	
	@Autowired
	protected CmacWyService cmacWyService;
	
	@Autowired 
	protected CmacBtService cmacBtService;
	
	@Autowired
	protected CmacElectService cmacElectService;
	
	@Autowired
	protected CmacWaterService cmacWaterService;
	
	
	/**
	 * @TODO 对项目进行通用账户扣取
	 */
	@Transactional(rollbackFor=Exception.class)
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDto billing(final String companyId, final TBsProject project) {
		
		logger.info("对项目进行通用账户扣取: 扣取开始, 项目数据: {}",project.toString());
		
		//从项目下获取本月未从通用账户扣取的总账单, 按顺序排序 , 建立一个List集合,将相应的服务按顺序放入
		TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
		paramTotal.setBillingTime(project.getBillingTime());
		paramTotal.setProjectId(project.getProjectId());
		List<TBsChargeBillTotal> totals = this.tBsChargeBillTotalMapper.findCmacCanbilling(paramTotal);
		if(CommonUtils.isEmpty(totals)){
			//全部都已经计费,直接返回
			logger.warn("对项目进行通用账户扣取: 当前项目下未发现待扣费的总账单, 扣费完成 . 项目数据: {}",project.toString());
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前项目下未发现待扣费的总账单, 扣费完成. "));
		}

		//判断总账单数目
		List<TBsChargeBillTotal> allTotals = this.tBsChargeBillTotalMapper.findByObj(paramTotal);
		if(CommonUtils.isEmpty(allTotals) || totals.size() < allTotals.size()){
			//有部分总账单尚未完成
			logger.warn("对项目进行通用账户扣取: 有部分总账单尚未审核完成,无法进行通用账户扣取 . 项目数据: {}",project.toString());
			return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"对项目进行通用账户扣取: 有部分总账单尚未审核完成,无法进行通用账户扣取 . "));
		}

		final List<ICmacService> list = new ArrayList<ICmacService>(4);
		for(int i = 0 ; i < 4; i++){
			list.add(i,null);
		}
		list.set(project.getWyOrder()-1, this.cmacWyService);
		list.set(project.getBtOrder()-1, this.cmacBtService);
		list.set(project.getWaterOrder()-1, this.cmacWaterService);
		list.set(project.getElectOrder()-1, this.cmacElectService);

//		logger.info("正在扣费数据:{}",JSON.toJSONString(handlers));

		final String cpId = companyId.concat(Constants.STR_UNDERLINE).concat(project.getProjectId());
		if(handlers.containsKey(cpId) && CommonUtils.isNotEmpty(handlers.get(cpId))){
			logger.info("正在扣费数据，结束");
			return new BaseDto(new MessageMap(null,"当前存在正在进行中的任务"));
		}
		
			
		handlers.put(cpId,list);
		logger.info("对项目进行通用账户扣取: 开始本项目进行扣取. ");
		//开始扣费
		handlers.get(cpId).get(0).invoke(companyId,project);
		
		return new BaseDto(new MessageMap(null,"完成"));
		
	}

}
