package com.everwing.autotask.core.jobs.bill.autobilling;

import com.everwing.autotask.core.service.CompanyService;
import com.everwing.autotask.core.service.bill.WaterElectAutoBillingService;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 电费自动计费
 *
 * @author DELL shiny
 * @create 2018/6/5
 */
@JobHandler("electAutoBillingJobHandler")
@Component
public class ElectAutoBillingJobHandler extends IJobHandler{
    @Autowired
    private CompanyService companyService;

    @Autowired
    private WaterElectAutoBillingService waterElectAutoBillingService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        List<Company> companyList=companyService.queryAllCompany();
        XxlJobLogger.log("自动任务 [电费自动计费] : 自动扫描开始.");
        for(Company company:companyList){
            String companyId=company.getCompanyId();
            XxlJobLogger.log("自动任务 [电费自动计费]{0}公司",companyId);
            waterElectAutoBillingService.autoBilling(companyId, BillingEnum.ACCOUNT_TYPE_ELECT.getIntV());
            XxlJobLogger.log("自动任务 [电费自动计费]{0}公司结束",companyId);
        }
        XxlJobLogger.log("自动任务 [电费自动计费] : 结束.");
        return ReturnT.SUCCESS;
    }
}
