package com.everwing.autotask.core.jobs.finance;

import com.everwing.autotask.core.service.CompanyService;
import com.everwing.autotask.core.service.PushPayInfoToFinanceService;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 物业自动计费
 *
 * @author DELL shiny
 * @create 2018/6/5
 */
@JobHandler("pushFinanceJobHandler")
@Component
public class PushFinanceJobHandler extends IJobHandler{

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PushPayInfoToFinanceService pushPayInfoToFinanceService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        List<Company> companyList=companyService.queryAllCompany();
        XxlJobLogger.log("自动任务 [财务推送] : 自动扫描开始.");
        for(Company company:companyList){
            String companyId=company.getCompanyId();
            XxlJobLogger.log("自动任务 [财务推送]{0}公司",companyId);
            pushPayInfoToFinanceService.doPushPayInfo(companyId);
            XxlJobLogger.log("自动任务 [财务推送]{0}公司结束",companyId);
        }
        XxlJobLogger.log("自动任务 [财务推送] : 结束.");
        return ReturnT.SUCCESS;
    }
}
