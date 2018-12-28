package com.everwing.coreservice.wy.api.configuration.billmgr;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.everwing.coreservice.common.wy.service.configuration.billmgr.BillMgrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("billMgrApi")
public class BillMgrApi {

	@Autowired
	private BillMgrService billMgrService;

	public RemoteModelResult<BaseDto> genBill(String companyId,TBsProject project) {
		return new RemoteModelResult<BaseDto>(this.billMgrService.genBill(companyId,project));
	}

	public RemoteModelResult<BaseDto> reGenBill(String companyId,TBsProject project) {
		return new RemoteModelResult<BaseDto>(this.billMgrService.reGenBill(companyId,project)); 
	}

	public RemoteModelResult<BaseDto> zipBill(String companyId,TBsProject project) {
		return new RemoteModelResult<BaseDto>(this.billMgrService.zipBill(companyId,project)); 
	}
	
}
