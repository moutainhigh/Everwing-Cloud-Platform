package com.everwing.coreservice.wy.core.service.impl.configuration.cmac.single;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.service.configuration.cmac.single.servie.*;
import com.everwing.coreservice.wy.core.resourceDI.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("singleCmacBillingService")
public class SingleCmacBillingServiceImpl extends Resources{

	private static final Logger logger = LogManager.getLogger(SingleCmacBillingServiceImpl.class);
	@Autowired
	protected SingleCmacWyService singleCmacWyService;
	
	@Autowired 
	protected SingleCmacBtService singleCmacBtService;
	
	@Autowired
	protected SingleCmacElectService singleCmacElectService;
	
	@Autowired
	protected SingleCmacWaterService singleCmacWaterService;
	
	/**
	 * 对项目的通用账户进行扣减
	 */
	public BaseDto billing(final String companyId, final TBsProject project,final String buildCode) {
		logger.info("对项目进行通用账户扣取: 扣取开始, 项目数据: {}",project.toString());
		//从项目下获取本月未从通用账户扣取的总账单, 按顺序排序 , 建立一个List集合,将相应的服务按顺序放入
				TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
				paramTotal.setBillingTime(project.getBillingTime());
				paramTotal.setProjectId(project.getProjectId());
				paramTotal.setAuditStatus(BillingEnum.AUDIT_STATUS_COMPELTE.getIntV());
				List<TBsChargeBillTotal> totals = this.tBsChargeBillTotalMapper.findCmacCanbilling(paramTotal);
				if(!CommonUtils.isEmpty(totals)){
					//全部都已经计费,直接返回
					logger.warn("对项目进行通用账户扣取: 当前项目下未发现待扣费的总账单, 扣费完成 . 项目数据: {}",project.toString());
					return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"当前项目下未发现待扣费的总账单, 扣费完成. "));
				}
				final List<ISingleCmacService> list = new ArrayList<ISingleCmacService>(4);
				for(int i = 0 ; i < 4; i++){
					list.add(i,null);
				}
				list.set(project.getWyOrder()-1, this.singleCmacWyService);
				list.set(project.getBtOrder()-1, this.singleCmacBtService);
				list.set(project.getWaterOrder()-1, this.singleCmacElectService);
				list.set(project.getElectOrder()-1, this.singleCmacWaterService);
				
				final String cpId = companyId.concat(Constants.STR_UNDERLINE).concat(project.getProjectId());
				if(signleHandlers.containsKey(cpId) && CommonUtils.isNotEmpty(handlers.get(cpId))){
					return new BaseDto(new MessageMap(null,"当前存在正在进行中的任务"));
				}
				
				signleHandlers.put(cpId,list);
				logger.info("对项目进行通用账户扣取: 开始本项目进行扣取. ");
				//开始扣费
				signleHandlers.get(cpId).get(0).invoke(companyId,project,buildCode);
				//聚合本项目的总账单
				return new BaseDto(new MessageMap(null,"完成"));
	}
}
