package com.everwing.autotask.core.jobs.bill.project;

import com.everwing.autotask.core.service.CompanyService;
import com.everwing.autotask.core.service.bill.TBsProjectService;
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
 * @create 2018/6/4
 */
@JobHandler(value = "tBsProjectJobHandler")
@Component
public class TBsProjectJobHandler extends IJobHandler{

    @Autowired
    private CompanyService companyService;

    @Autowired
    private TBsProjectService tBsProjectService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        List<Company> companyList = companyService.queryAllCompany();
        XxlJobLogger.log("开始自动扫描所有可复制的计费项目");
        for(Company company:companyList){
            String companyId=company.getCompanyId();
            XxlJobLogger.log("开始自动扫描所有可复制的计费项目公司：{0}",companyId);
            tBsProjectService.genNextProject(companyId);
            XxlJobLogger.log("自动扫描所有可复制的计费项目完成公司：{0}",companyId);
        }
        XxlJobLogger.log("自动扫描所有可复制的计费项目完成");
        return ReturnT.SUCCESS;
    }
}
