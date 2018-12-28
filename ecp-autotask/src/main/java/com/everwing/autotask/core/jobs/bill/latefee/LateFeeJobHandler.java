package com.everwing.autotask.core.jobs.bill.latefee;

import com.everwing.autotask.core.service.CompanyService;
import com.everwing.autotask.core.service.ProjectService;
import com.everwing.autotask.core.service.bill.LateFeeService;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 违约金定时任务
 *
 * @author DELL shiny
 * @create 2018/6/4
 */
@JobHandler(value = "lateFeeJobHandler")
@Component
public class LateFeeJobHandler extends IJobHandler{

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private LateFeeService lateFeeService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        List<Company> companyList = companyService.queryAllCompany();
        XxlJobLogger.log("按照公司分开计算违约金计算开始");
        for(Company company:companyList){
            String companyId=company.getCompanyId();
            XxlJobLogger.log("{0}公司开始计算违约金",companyId);
            TBsProject param=new TBsProject();
            param.setBillingTime(new Date());
            param.setStatus(0);
            List<TBsProject> projects=projectService.findByObj(companyId,param);
            for(TBsProject project:projects){
                XxlJobLogger.log("{0}项目开始计算违约金",project.toString());
                lateFeeService.billLateFee(companyId,project);
                XxlJobLogger.log("{0}项目计算违约金结束",project.toString());
            }
            XxlJobLogger.log("{0}公司计算违约金结束",companyId);
        }
        XxlJobLogger.log("违约金结束");
        return ReturnT.SUCCESS;
    }
}
