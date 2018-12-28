package com.everwing.coreservice.wy.core.resourceDI;

import com.everwing.coreservice.common.wy.service.annex.AnnexService;
import com.everwing.coreservice.common.wy.service.business.meterdata.TcMeterDataService;
import com.everwing.coreservice.common.wy.service.configuration.bc.collection.TBcCollectionService;
import com.everwing.coreservice.common.wy.service.configuration.bill.TBsBillHistoryService;
import com.everwing.coreservice.common.wy.service.configuration.bill.TBsBillTotalService;
import com.everwing.coreservice.common.wy.service.configuration.billmgr.BillMgrService;
import com.everwing.coreservice.common.wy.service.configuration.cmac.CmacBillingService;
import com.everwing.coreservice.common.wy.service.configuration.latefee.LateFeeService;
import com.everwing.coreservice.common.wy.service.configuration.project.TBsProjectService;
import com.everwing.coreservice.common.wy.service.configuration.task.*;
import com.everwing.coreservice.common.wy.service.quartz.business.MeterDataTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @TODO 提供service层的统一依赖注入
 *
 */
@Component
public class ServiceRrcs {

	
	/** ---------------------------------------------- 收费计费  ---------------------------------------------- **/
	
	@Autowired
	protected WyBillingTaskService wyBillingTaskService;
	
	@Autowired
	protected TBsProjectTaskService tBsProjectTaskService;
	

	@Autowired 
	protected WaterElectShareTaskService waterElectShareTaskService;

	@Autowired
	protected WaterAndElectBillingTaskService waterAndElectBillingTaskService;
	@Autowired
	protected TBsBillTotalService tBsBillTotalService;
	
	@Autowired
	protected TBsBillHistoryService tBsBillHistoryService;
	
	@Autowired
	protected CmacBillingService cmacBillingService;
	
	@Autowired
	protected TBsProjectService tBsProjectService;
	
	@Autowired
	protected LateFeeService LateFeeService;
	
	@Autowired
	protected WyShareTaskService wyShareTaskService;
	
	@Autowired
	protected BillMgrService billMgrService;
	
	@Autowired
	protected WaterElectRebillingService waterElectRebillingService;
	
	@Autowired
	protected TBcCollectionService tBcCollectionService;
	
	@Autowired
	protected MeterDataTaskService meterDataTaskService;
	
	@Autowired
	protected TcMeterDataService tcMeterDataService;
	
	@Autowired
	protected AnnexService annexService;
}
