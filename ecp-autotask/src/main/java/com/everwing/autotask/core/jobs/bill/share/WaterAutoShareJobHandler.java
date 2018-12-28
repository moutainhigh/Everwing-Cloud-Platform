package com.everwing.autotask.core.jobs.bill.share;

import com.everwing.autotask.core.service.CompanyService;
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
 * @author DELL shiny
 * @create 2018/6/4
 */
@JobHandler("waterAutoShareJobHandler")
@Component
public class WaterAutoShareJobHandler extends IJobHandler {

    @Autowired
    private CompanyService companyService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("水费自动分摊计费定时任务开始执行");
        List<Company> companyList=companyService.queryAllCompany();
        for(Company company:companyList){
            String companyId=company.getCompanyId();
            XxlJobLogger.log("公司:{0}水费自动分摊开始",companyId);
            //waterElectShareService.doWaterElectShareBillingByCompany(companyId, BillingEnum.SHARE_ELECT_TYPE.getIntV());
            XxlJobLogger.log("公司:{0}水费自动分摊完成",companyId);
        }
        XxlJobLogger.log("水费自动分摊计费定时任务执行完成");
        return ReturnT.SUCCESS;
    }
}
