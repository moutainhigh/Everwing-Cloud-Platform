package com.everwing.coreservice.common.wy.service.configuration.billmgr;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;

public interface BillMgrService {

	BaseDto genBill(String companyId, TBsProject project);

	void genBillFirstByManaul(String companyId, TBsProject project);

	BaseDto reGenBill(String companyId, TBsProject project);

	void reGenBillByManaul(String companyId, TBsProject javaObject);

	void autoGenBill(String companyId, TBsProject project);

	BaseDto zipBill(String companyId, TBsProject project);

}
