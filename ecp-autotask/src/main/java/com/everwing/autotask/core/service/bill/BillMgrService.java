package com.everwing.autotask.core.service.bill;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;

/**
 * Created by DELL on 2018/6/4.
 */
public interface BillMgrService {

    void autoGenBill(String companyId, TBsProject project);
}
