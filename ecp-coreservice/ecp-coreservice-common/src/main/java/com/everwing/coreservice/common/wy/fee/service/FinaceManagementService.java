package com.everwing.coreservice.common.wy.fee.service;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.wy.fee.entity.IncomingParameter;
import com.everwing.coreservice.common.wy.fee.entity.ProjectAccount;


/**
 * 周期性计费收入
 *
 */

public interface FinaceManagementService {



    BaseDto listProjectCycleDetail(String companyId, IncomingParameter incomingParameter);

    BaseDto countProjectCycleDetailAmount(String companyId, IncomingParameter incomingParameter);

    BaseDto listProjectDelayDetail(String companyId, IncomingParameter incomingParameter);

    BaseDto countProjectDelayDetail(String companyId, IncomingParameter incomingParameter);

    BaseDto listProjectPrestoreDetail(String companyId, IncomingParameter incomingParameter);

    BaseDto countProjectPrestoreDetail(String companyId, IncomingParameter incomingParameter);

    BaseDto listProjectProductDetail(String companyId, IncomingParameter incomingParameter);

    BaseDto countProjectProductDetail(String companyId, IncomingParameter incomingParameter);

    BaseDto listProjectFineDetail(String companyId, IncomingParameter incomingParameter);

    BaseDto countProjectFineDetail(String companyId, IncomingParameter incomingParameter);

    BaseDto selectProject(String companyId, ProjectAccount projectAccount);
}
