package com.everwing.autotask.core.service.impl.bill;

import com.alibaba.fastjson.JSON;
import com.everwing.autotask.core.dao.TBsChargeBillTotalMapper;
import com.everwing.autotask.core.service.bill.*;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * 通用账户逻辑
 *
 * @author DELL shiny
 * @create 2018/5/30
 */
@Log4j2
@Service
public class CmacBillingServiceImpl implements CmacBillingService {

    @Autowired
    private TBsChargeBillTotalMapper tBsChargeBillTotalMapper;

    @Autowired
    private CmacWyService cmacWyService;

    @Autowired
    private CmacBtService cmacBtService;

    @Autowired
    private CmacWaterService cmacWaterService;

    @Autowired
    private CmacElectService cmacElectService;

    @Override
    public void setNext(BaseCmacService cmacService) {
        //do nothing
    }

    @Override
    public void invoke(String companyId, TBsProject project) {
//        log.info("对项目进行通用账户扣取: 扣取开始, 项目数据: {}", JSON.toJSONString(project));
        //从项目下获取本月未从通用账户扣取的总账单, 按顺序排序 , 建立一个List集合,将相应的服务按顺序放入
        TBsChargeBillTotal paramTotal = new TBsChargeBillTotal();
        paramTotal.setBillingTime(project.getBillingTime());
        paramTotal.setProjectId(project.getProjectId());
        List<TBsChargeBillTotal> totals = tBsChargeBillTotalMapper.findCmacCanbilling(paramTotal);
        if(CommonUtils.isEmpty(totals)){
            log.info("对项目进行通用账户扣取: 当前项目下未发现待扣费的总账单, 扣费完成 . 项目数据: {}",JSON.toJSONString(project));
            return;
        }
        List<TBsChargeBillTotal> allTotals = tBsChargeBillTotalMapper.findByObj(paramTotal);
        if(CommonUtils.isEmpty(allTotals) || totals.size() < allTotals.size()){
            //有部分总账单尚未完成
            log.info("对项目进行通用账户扣取: 有部分总账单尚未审核完成,无法进行通用账户扣取 . 项目数据: {}",JSON.toJSONString(project));
            return ;
        }
        List<BaseCmacService> list = new ArrayList<>(4);
        for(int i = 0 ; i < 4; i++){
            list.add(i,null);
        }
        list.set(project.getWyOrder()-1, cmacWyService);
        list.set(project.getBtOrder()-1, cmacBtService);
        list.set(project.getWaterOrder()-1, cmacWaterService);
        list.set(project.getElectOrder()-1, cmacElectService);
        for(int i = 0 ; i < 3; i++){
            list.get(i).setNext(list.get(i+1));
        }
        log.info("对项目进行通用账户扣取: 开始本项目进行扣取. ");
        //开始扣费
        list.get(0).invoke(companyId,project);
    }
}
