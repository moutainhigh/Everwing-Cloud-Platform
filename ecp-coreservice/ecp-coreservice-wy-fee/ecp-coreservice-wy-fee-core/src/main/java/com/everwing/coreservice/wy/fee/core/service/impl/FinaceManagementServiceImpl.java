package com.everwing.coreservice.wy.fee.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.fee.entity.*;
import com.everwing.coreservice.common.wy.fee.service.FinaceManagementService;
import com.everwing.coreservice.wy.fee.dao.mapper.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Service(timeout = 12000)
@Component
public class FinaceManagementServiceImpl implements FinaceManagementService {
    private static final Logger logger = LogManager.getLogger(FinaceManagementServiceImpl.class);


    @Autowired
    private ProjectCycleDetailMapper projectCycleDetailMapper;

    @Autowired
    private ProjectDelayDetailMapper projectDelayDetailMapper;

    @Autowired
    private ProjectPrestoreDetailMapper projectPrestoreDetailMapper;
    @Autowired
    private ProjectProductDetailMapper projectProductDetailMapper;
    @Autowired
    private ProjectFineDetailMapper projectFineDetailMapper;
    @Autowired
    private ProjectAccountMapper projectAccountMapper;

    @Autowired
    private TSysOrganizationMapper tSysOrganizationMapper;
    @Override
    //周期性数据
    public BaseDto listProjectCycleDetail(String companyId, IncomingParameter incomingParameter) {
       if (CommonUtils.isEmpty(incomingParameter.getProjectId())){
           return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有传入项目id,请传入项目id "));
       }
       incomingParameter.setCompanyId(companyId);
        List<AccountAddCycleParameter> lists =  projectCycleDetailMapper.listPageSelectByAllDetailAndIdAll(incomingParameter);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setPage(incomingParameter.getPage());
        baseDto.setLstDto(lists);
        return baseDto ;
    }

    @Override
    // 总计周期性收费数据
    public BaseDto countProjectCycleDetailAmount(String companyId, IncomingParameter incomingParameter) {

        if (CommonUtils.isEmpty(incomingParameter.getProjectId())){
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有传入项目id,请传入项目id "));
        }
        incomingParameter.setCompanyId(companyId);
        AccountAddCycleParameter parameter =   projectCycleDetailMapper.counCycleDetailAmount(incomingParameter);
        BaseDto baseDto = new BaseDto<>();

        baseDto.setObj(parameter);
        return  baseDto;
    }

    @Override
    //违约金
    public BaseDto listProjectDelayDetail(String companyId, IncomingParameter incomingParameter) {
        if (CommonUtils.isEmpty(incomingParameter.getProjectId())){
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有传入项目id,请传入项目id "));
        }
         incomingParameter.setCompanyId(companyId);
         List<AccountAddDelayParameter>lists=projectDelayDetailMapper.listPageSelectByAllDetailAndIdAll(incomingParameter);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setPage(incomingParameter.getPage());
        baseDto.setLstDto(lists);


        return baseDto ;
    }



    @Override
    //总计违约金
    public BaseDto countProjectDelayDetail(String companyId, IncomingParameter incomingParameter) {
        if (CommonUtils.isEmpty(incomingParameter.getProjectId())){
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有传入项目id,请传入项目id "));
        }
        incomingParameter.setCompanyId(companyId);
        AccountAddDelayParameter parameter =   projectDelayDetailMapper.counCycleDetailAmount(incomingParameter);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setObj(parameter);
        return  baseDto;

    }

    @Override
    //预收账户
    public BaseDto listProjectPrestoreDetail(String companyId, IncomingParameter incomingParameter) {
        if (CommonUtils.isEmpty(incomingParameter.getProjectId())){
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有传入项目id,请传入项目id "));
        }
        incomingParameter.setCompanyId(companyId);
        List<AccountAddPrestoreParameter> lists=projectPrestoreDetailMapper.listPageSelectByAllPrestoreAndIdAll(incomingParameter);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setPage(incomingParameter.getPage());
        baseDto.setObj(lists);
        return  baseDto;

    }

    @Override
    //预收账户
    public BaseDto countProjectPrestoreDetail(String companyId, IncomingParameter incomingParameter) {
        if (CommonUtils.isEmpty(incomingParameter.getProjectId())){
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有传入项目id,请传入项目id "));
        }
         incomingParameter.setCompanyId(companyId);
        AccountAddPrestoreParameter lists = projectPrestoreDetailMapper.counPrestoreDetailAmount(incomingParameter);

        BaseDto baseDto = new BaseDto<>();
        baseDto.setObj(lists);
        return  baseDto;
    }

    @Override
    // 产品收费
    public BaseDto listProjectProductDetail(String companyId, IncomingParameter incomingParameter) {
        if (CommonUtils.isEmpty(incomingParameter.getProjectId())){
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有传入项目id,请传入项目id "));
        }
         incomingParameter.setCompanyId(companyId);
        List<AccountAddProductDetail> products=projectProductDetailMapper.listPageSelectProjectProductDetail(incomingParameter);

        BaseDto baseDto = new BaseDto<>();
        baseDto.setPage(incomingParameter.getPage());
        baseDto.setObj(products);



        return  baseDto;
    }

    @Override
    //总计产品收费
    public BaseDto countProjectProductDetail(String companyId, IncomingParameter incomingParameter) {
        if (CommonUtils.isEmpty(incomingParameter.getProjectId())){
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有传入项目id,请传入项目id "));
        }
        incomingParameter.setCompanyId(companyId);
        AccountAddProductDetail product=projectProductDetailMapper.countProjectProductDetail(incomingParameter);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setObj( product);
         return  baseDto;
    }

    @Override
    //罚金
    public BaseDto listProjectFineDetail(String companyId, IncomingParameter incomingParameter) {
        if (CommonUtils.isEmpty(incomingParameter.getProjectId())){
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有传入项目id,请传入项目id "));
        }
         incomingParameter.setCompanyId(companyId);
         List <AccountAddFineDetailParameter> fineDetail= projectFineDetailMapper.listPageSelectProjectFineDetail(incomingParameter);

        BaseDto baseDto = new BaseDto<>();
        baseDto.setPage(incomingParameter.getPage());
        baseDto.setObj( fineDetail);



        return  baseDto;
    }

    @Override
    //总计罚金
    public BaseDto countProjectFineDetail(String companyId, IncomingParameter incomingParameter) {
        if (CommonUtils.isEmpty(incomingParameter.getProjectId())){
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "您当前没有传入项目id,请传入项目id "));
        }
        incomingParameter.setCompanyId(companyId);
        AccountAddFineDetailParameter fineDetailCount=projectFineDetailMapper.countProjectFineDetail(incomingParameter);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setObj(fineDetailCount);
        return  baseDto;
    }

    @Override
    public BaseDto selectProject(String companyId, ProjectAccount projectAccount) {
             BaseDto baseDto=new BaseDto();
            if(CommonUtils.isNotEmpty(projectAccount.getStaffCode())){
                List<String> projectIdList = tSysOrganizationMapper.selectProjectId(projectAccount.getStaffCode());
                if(CommonUtils.isEmpty(projectIdList)){
                    baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_ERROR,"你没有权限"));
                    return baseDto;
                }
                List<ProjectAccount> list = projectAccountMapper.selectProject(projectIdList);
                baseDto.setLstDto(list);
                baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
                return baseDto;
            }

        List<ProjectAccount> project = projectAccountMapper.selectProjectById(projectAccount.getProjectId());
        baseDto.setLstDto(project);
        baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询成功"));
        return baseDto;
    }


}
