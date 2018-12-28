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
 * 表扫描
 *
 * @author DELL shiny
 * @create 2018/6/5
 */
@JobHandler("meterDataScanJobHandler")
@Component
public class MeterDataScanJobHandler extends IJobHandler {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MeterDataTaskService meterDataTaskService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("任务自动生成开始");
        List<Company> companyList=companyService.queryAllCompany();
        for(Company company:companyList){
            String companyId=company.getCompanyId();
            XxlJobLogger.log("任务自动生成公司:{0}开始",companyId);
            if(!"09841dc0-204a-41f2-a175-20a6dcee0187".equals(companyId)) continue;
            meterDataTaskService.scanSchedule(companyId);
            XxlJobLogger.log("任务自动生成公司:{0}结束",companyId);
        }
        XxlJobLogger.log("任务自动生成结束");
        return ReturnT.SUCCESS;
    }
}
