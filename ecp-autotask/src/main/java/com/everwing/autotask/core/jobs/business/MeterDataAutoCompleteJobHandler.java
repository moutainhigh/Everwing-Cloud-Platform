package com.everwing.autotask.core.jobs.business;

import com.everwing.autotask.core.service.CompanyService;
import com.everwing.autotask.core.service.business.MeterDataTaskService;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author DELL shiny
 * @create 2018/6/6
 */
@JobHandler("meterDataAutoCompleteJobHandler")
@Component
public class MeterDataAutoCompleteJobHandler extends IJobHandler{

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MeterDataTaskService meterDataTaskService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        List<Company> companyList=companyService.queryAllCompany();
        for(Company company:companyList){
            String companyId=company.getCompanyId();
            XxlJobLogger.log("任务自动完成开始，当前公司:{0}",companyId);
            meterDataTaskService.autoComplete(companyId);
            XxlJobLogger.log("任务自动完成结束，当前公司:{0}",companyId);
        }
        return ReturnT.SUCCESS;
    }
}
