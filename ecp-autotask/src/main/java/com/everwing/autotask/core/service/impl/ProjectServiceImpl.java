package com.everwing.autotask.core.service.impl;

import com.everwing.autotask.core.dao.TBsChargeBillTotalMapper;
import com.everwing.autotask.core.dao.TBsProjectMapper;
import com.everwing.autotask.core.service.ProjectService;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DELL shiny
 * @create 2018/5/11
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private TBsProjectMapper tBsProjectMapper;

    @Autowired
    private TBsChargeBillTotalMapper tBsChargeBillTotalMapper;

    @Override
    public List<TBsProject> findCanBillingCmacProject(String companyId) {
        return tBsProjectMapper.findCanBillingCmacProject();
    }

    @Override
    public List<TBsProject> findByObj(String companyId, TBsProject project) {
        return tBsProjectMapper.findAllByObj(project);
    }

    @Override
    public List<TBsProject> findCanGenBillProject(String companyId) {
        List<TBsProject> projects = tBsProjectMapper.findCanGenBillProject();

        List<TBsProject> returnList = new ArrayList<TBsProject>();
        if(CommonUtils.isNotEmpty(projects)){

            List<String> types = new ArrayList<String>();

            for(TBsProject p : projects){
                types.clear();
                if(p.getWyStatus() != BillingEnum.PROJECT_BILLING_STATUS_STOP.getIntV()) types.add(Constants.STR_ONE);
                if(p.getBtStatus() != BillingEnum.PROJECT_BILLING_STATUS_STOP.getIntV()) types.add(Constants.STR_TWO);
                if(p.getWaterStatus() != BillingEnum.PROJECT_BILLING_STATUS_STOP.getIntV()) types.add(Constants.STR_THREE);
                if(p.getElectStatus() != BillingEnum.PROJECT_BILLING_STATUS_STOP.getIntV()) types.add(Constants.STR_FOUR);

                if(CommonUtils.isEmpty(types)) continue;

                //寻找到该计费项目下的所有已审核总账单
                Integer count = tBsChargeBillTotalMapper.getAuditedCountByProjectIdAndTypes(p.getProjectId(),types);

                if(count == types.size()){
                    returnList.add(p);
                }
            }
        }
        return returnList;
    }

    @Override
    public TBsProject findById(String companyId, String projectId) {
        return tBsProjectMapper.findById(projectId);
    }
}
