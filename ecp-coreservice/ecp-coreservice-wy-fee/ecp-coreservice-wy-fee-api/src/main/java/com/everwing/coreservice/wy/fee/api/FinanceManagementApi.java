package com.everwing.coreservice.wy.fee.api;


import com.alibaba.dubbo.config.annotation.Reference;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.fee.entity.IncomingParameter;
import com.everwing.coreservice.common.wy.fee.entity.ProjectAccount;
import com.everwing.coreservice.common.wy.fee.service.FinaceManagementService;
import org.springframework.stereotype.Component;

/**
 *
 * 财务管理页面
 *
 * @author
 * @create 2018/6/26
 */
@Component
public class FinanceManagementApi {
    @Reference(check = false)
    private FinaceManagementService finaceManagementService;
    public RemoteModelResult listProjectCycleDetail(IncomingParameter incomingParamete){

     return new RemoteModelResult(finaceManagementService.listProjectCycleDetail(CommonUtils.getCompanyIdByCurrRequest(),incomingParamete));
    }

    public RemoteModelResult countProjectCycleDetailAmount(IncomingParameter incomingParameter) {
        return new RemoteModelResult(finaceManagementService.countProjectCycleDetailAmount(CommonUtils.getCompanyIdByCurrRequest(),incomingParameter));
    }

    public RemoteModelResult listProjectDelayDetail(IncomingParameter incomingParameter) {
        return  new RemoteModelResult(finaceManagementService.listProjectDelayDetail(CommonUtils.getCompanyIdByCurrRequest(),incomingParameter));
    }

    public RemoteModelResult countProjectDelayDetail(IncomingParameter incomingParameter) {
        return new RemoteModelResult(finaceManagementService.countProjectDelayDetail(CommonUtils.getCompanyIdByCurrRequest(),incomingParameter));
    }

    public RemoteModelResult listProjectPrestoreDetail(IncomingParameter incomingParameter) {
        return  new RemoteModelResult(finaceManagementService.listProjectPrestoreDetail(CommonUtils.getCompanyIdByCurrRequest(),incomingParameter));
    }

    public RemoteModelResult countProjectPrestoreDetail(IncomingParameter incomingParameter) {
        return  new RemoteModelResult(finaceManagementService.countProjectPrestoreDetail(CommonUtils.getCompanyIdByCurrRequest(),incomingParameter));
    }

    public RemoteModelResult listProjectProductDetai(IncomingParameter incomingParameter) {
       return  new RemoteModelResult(finaceManagementService.listProjectProductDetail(CommonUtils.getCompanyIdByCurrRequest(),incomingParameter));
    }

    public RemoteModelResult countProjectProductDetail(IncomingParameter incomingParameter) {
       return  new RemoteModelResult(finaceManagementService.countProjectProductDetail(CommonUtils.getCompanyIdByCurrRequest(),incomingParameter));
    }

    public RemoteModelResult listProjectFineDetail(IncomingParameter incomingParameter) {
        return  new RemoteModelResult(finaceManagementService.listProjectFineDetail(CommonUtils.getCompanyIdByCurrRequest(),incomingParameter));
    }

    public RemoteModelResult countProjectFineDetail(IncomingParameter incomingParameter) {
        return  new RemoteModelResult(finaceManagementService.countProjectFineDetail(CommonUtils.getCompanyIdByCurrRequest(),incomingParameter));
    }

    public RemoteModelResult selectProject(ProjectAccount projectAccount){

        return new RemoteModelResult(finaceManagementService.selectProject(CommonUtils.getCompanyIdByCurrRequest(),projectAccount));
    }
}
