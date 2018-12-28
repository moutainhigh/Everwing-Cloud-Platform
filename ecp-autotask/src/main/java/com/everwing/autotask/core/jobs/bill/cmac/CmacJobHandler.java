package com.everwing.autotask.core.jobs.bill.cmac;

import com.alibaba.fastjson.JSON;
import com.everwing.autotask.core.service.CompanyService;
import com.everwing.autotask.core.service.ProjectService;
import com.everwing.autotask.core.service.bill.CmacBillingService;
import com.everwing.coreservice.common.platform.entity.generated.Company;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.ShardingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 通用账户自动扣费任务
 *
 * @author DELL shiny
 * @create 2018/5/8
 */
@JobHandler(value="cmacJobHandler")
@Component
public class CmacJobHandler extends IJobHandler {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CmacBillingService cmacBillingService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
        XxlJobLogger.log("分片参数：当前分片序号 = {0}, 总分片数 = {1}", shardingVO.getIndex(), shardingVO.getTotal());
        List<Company> companyList = companyService.queryAllCompany();
        XxlJobLogger.log("通用扣费开始");
        for(Company company:companyList){
            if(company!=null){
                String companyId =company.getCompanyId();
                XxlJobLogger.log("{0}公司进行通用扣费开始!",companyId);
                List<TBsProject> projects=projectService.findCanBillingCmacProject(companyId);
                for(TBsProject project:projects) {
                    XxlJobLogger.log("{0}项目进行通用扣费开始!", JSON.toJSONString(project));
                    cmacBillingService.invoke(companyId,project);
                    XxlJobLogger.log("{0}项目通用扣费结束!", JSON.toJSONString(project));
                }
                XxlJobLogger.log("{0}公司通用扣费结束!",companyId);
            }
        }
        XxlJobLogger.log("通用扣费结束");
        return ReturnT.SUCCESS;
    }
}
