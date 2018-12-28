package com.everwing.autotask.core.service.impl.bill;

import com.everwing.autotask.core.dao.TBsChargeBillHistoryMapper;
import com.everwing.autotask.core.dao.TBsChargingSchemeMapper;
import com.everwing.autotask.core.dao.TBsProjectMapper;
import com.everwing.autotask.core.service.bill.TBsProjectService;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author DELL shiny
 * @create 2018/6/4
 */
@Log4j2
@Service
public class TBsProjectServiceImpl implements TBsProjectService{

    @Autowired
    private TBsProjectMapper tBsProjectMapper;

    @Autowired
    private TBsChargingSchemeMapper tBsChargingSchemeMapper;

    @Autowired
    private TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;

    @Override
    public void genNextProject(String companyId) {
        log.info("定时任务: [ 扫描生成下月计费计划 ]启动");
        List<TBsProject> needGenProjects = tBsProjectMapper.findNeedGenProjects();
        if(CommonUtils.isEmpty(needGenProjects)){
            log.info("定时任务: [ 扫描生成下月计费计划 ]  完成  : 未找到可复制的本月计划");
            return ;
        }
        List<TBsProject> nextProjects = new ArrayList<TBsProject>();
        for(TBsProject p : needGenProjects){
            if(null == p){
                continue;
            }
            TBsProject nextProject = new TBsProject();
            nextProject.setId(CommonUtils.getUUID());
            nextProject.setBillingTime(CommonUtils.addMonth(p.getBillingTime(), 1));
            nextProject.setCreateId("system");
            nextProject.setCreateTime(new Date());
            nextProject.setModifyId("system");
            nextProject.setModifyTime(new Date());

            //status需要判断上月的状态 如果为0 则是未启动 , 如果为2/1 就是启动计费, 设置为1即可
            nextProject.setWaterStatus( (0 == p.getWaterStatus()) ? 0 : 1);
            nextProject.setBtStatus( (0 == p.getBtStatus()) ? 0 : 1);
            nextProject.setWyStatus( (0 == p.getWyStatus()) ? 0 : 1);
            nextProject.setElectStatus( (0 == p.getElectStatus()) ? 0 : 1);

            nextProject.setIsGenBill(BillingEnum.bill_is_gen_no.getIntV());
            nextProject.setProjectId(p.getProjectId());
            nextProject.setProjectName(p.getProjectName());
            nextProject.setBtOrder(p.getBtOrder());
            nextProject.setCommonStatus(p.getCommonStatus());
            nextProject.setCurrentFee(0.0);
            nextProject.setElectOrder(p.getElectOrder());
            nextProject.setLastOwedFee(0.0);
            nextProject.setStatus(p.getStatus());
            nextProject.setTotalFee(0.0);
            nextProject.setWaterOrder(p.getWaterOrder());
            nextProject.setWyOrder(p.getWyOrder());
            nextProjects.add(nextProject);
        }
        if(!nextProjects.isEmpty()) {
            tBsProjectMapper.batchInsert(nextProjects);
        }
        log.info("定时任务 : [ 扫描生成下月计费计划 ] 完成");
    }

    @Override
    public void autoFlush(String companyId) {
        //自动将过期的scheme置为无效
        tBsChargingSchemeMapper.autoStopScheme();

        //找出该公司下所有本月的计费项目
        TBsProject paramObj = new TBsProject();
        paramObj.setBillingTime(new Date());
        paramObj.setStatus(BillingEnum.IS_USED_USING.getIntV());;
        List<TBsProject> projects = this.tBsProjectMapper.findAllByObj(paramObj);

//		TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
        if(CommonUtils.isNotEmpty(projects)){
            for(TBsProject p : projects){

                //对有欠费记录的分单进行违约金聚合 , 记录在新的账单里面, billing_time为null
                if(null != p.getProjectId()){
                    tBsChargeBillHistoryMapper.updateLateFeeByProjectId(p.getProjectId());
                }

                p.setCurrentFee(0.0);
                p.setLastOwedFee(0.0);
                p.setTotalFee(0.0);

                //直接聚合t_bs_project, 计费完成的,已经产生下个月的任务的
                tBsProjectMapper.flushItems(p.getProjectId());


                //判断四个费种是否都已经计费完成
				/*paramTotal.setProjectId(p.getProjectId());
				paramTotal.setBillingTime(p.getBillingTime());*/
				/*if(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV() == p.getWyStatus()){
					paramTotal.setType(BillingEnum.ACCOUNT_TYPE_WY.getIntV());
					sumTotal(paramTotal, p);
				}
				if(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV() == p.getBtStatus()){
					paramTotal.setType(BillingEnum.ACCOUNT_TYPE_BT.getIntV());
					sumTotal(paramTotal, p);
				}
				if(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV() == p.getWaterStatus()){
					paramTotal.setType(BillingEnum.ACCOUNT_TYPE_WATER.getIntV());
					sumTotal(paramTotal, p);
				}
				if(BillingEnum.PROJECT_BILLING_STATUS_COMPLETE.getIntV() == p.getElectStatus()){
					paramTotal.setType(BillingEnum.ACCOUNT_TYPE_ELECT.getIntV());
					sumTotal(paramTotal, p);
				}

				this.tBsProjectMapper.update(p);*/
            }
        }
        log.info("项目自动刷新: 刷新完成");
    }
}
