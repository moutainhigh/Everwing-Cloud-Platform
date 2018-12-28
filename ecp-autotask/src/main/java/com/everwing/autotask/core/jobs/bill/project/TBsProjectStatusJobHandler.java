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
 * 定时更新项目状态
 *
 * @author DELL shiny
 * @create 2018/6/4
 */
@JobHandler("tBsProjectStatusJobHandler")
@Component
public class TBsProjectStatusJobHandler extends IJobHandler{

    @Autowired
    private CompanyService companyService;

    @Autowired
    private TBsProjectService tBsProjectService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("自动修改项目状态任务启动");
        List<Company> companyList=companyService.queryAllCompany();
        for(Company company:companyList){
            String companyId=company.getCompanyId();
            XxlJobLogger.log("自动修改项目状态任务公司:{0}开始",companyId);
            tBsProjectService.autoFlush(companyId);
            XxlJobLogger.log("自动修改项目状态任务公司:{0}完成",companyId);
        }
        XxlJobLogger.log("自动修改项目状态任务完成");
        return ReturnT.SUCCESS;
    }
}
