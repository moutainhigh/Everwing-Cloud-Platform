package com.everwing.autotask.core.jobs.bill.mgr;

import com.alibaba.fastjson.JSON;
import com.everwing.autotask.core.service.CompanyService;
import com.everwing.autotask.core.service.ProjectService;
import com.everwing.autotask.core.service.bill.BillMgrService;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自动生成账单
 *
 * @author DELL shiny
 * @create 2018/6/4
 */
@JobHandler(value = "billAutoGenJobHandler")
@Component
public class BillAutoGenJobHandler extends IJobHandler{

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private BillMgrService billMgrService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        List<Company> companyList = companyService.queryAllCompany();
        XxlJobLogger.log("自动生成账单开始");
        for(Company company:companyList){
            String companyId=company.getCompanyId();
            XxlJobLogger.log("自动生成账单:{0}公司开始",companyId);
            List<TBsProject> projects = projectService.findCanGenBillProject(companyId);
            for(TBsProject project:projects){
                XxlJobLogger.log("自动生成账单:{0}项目开始", JSON.toJSONString(project));
                billMgrService.autoGenBill(companyId,project);
                XxlJobLogger.log("自动生成账单:{0}项目结束", JSON.toJSONString(project));
            }
            XxlJobLogger.log("自动生成账单:{0}公司结束",companyId);
        }
        XxlJobLogger.log("自动生成账单结束");
        return ReturnT.SUCCESS;
    }
}
