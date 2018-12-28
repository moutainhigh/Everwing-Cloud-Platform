package com.everwing.coreservice.wy.fee.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.fee.constant.AcChargeDetailBusinessTypeEnum;
import com.everwing.coreservice.common.wy.fee.constant.AccountType;
import com.everwing.coreservice.common.wy.fee.constant.BusinessType;
import com.everwing.coreservice.common.wy.fee.dto.AbnormalChargeAcccountDto;
import com.everwing.coreservice.common.wy.fee.entity.*;
import com.everwing.coreservice.common.wy.fee.service.AcAbnormalChargeService;
import com.everwing.coreservice.common.wy.fee.service.ProjectAccountService;
import com.everwing.coreservice.wy.fee.dao.mapper.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 异常计费处理实现
 */
@Component
@Service(timeout = 12000)
public class AbnormalChargeServiceImpl implements AcAbnormalChargeService {

    private static final Logger logger = LogManager.getLogger(AbnormalChargeServiceImpl.class);

    @Autowired
    private AcAbnormalChargeMapper acAbnormalChargeMapper;

    @Autowired
    private AcAbnormalChargAccountMapper acAbnormalChargAccountMapper;


    @Autowired
    private AcCommonAccountDetailMapper acCommonAccountDetailMapper;
    @Autowired
    private AcAccountMapper acAccountMapper;

    @Autowired
    private  AcCurrentChargeDetailMapper acCurrentChargeDetailMapper;

    @Autowired
    private  TBsAssetAccountMapper tBsAssetAccountMapper;
    @Autowired
    private AcCurrentChargeMapper acCurrentChargeMapper;
    @Autowired
    private  TBsAssetAccountStreamMapper tBsAssetAccountStreamMapper;

    @Autowired
    private ProjectAccountService projectAccountService;

    @Autowired
    private TSysOrganizationMapper tSysOrganizationMapper;

    /**
     * 分页加载异常账户处理后的数据
     * @param companyId
     * @param param
     * @return
     */
    public BaseDto listAbnormalCharge(String companyId, AcAbnormalCharge param){

        if(CommonUtils.isEmpty(companyId)){
            logger.info("{}没有传入项目id");
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"没有传入公司id"));
        }
        BaseDto baseDto=new BaseDto();
        List<String> projectIdList = tSysOrganizationMapper.selectProjectId(param.getStaffCode());
        if(CommonUtils.isEmpty(projectIdList)){
            baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_ERROR,"你没有权限"));
            return baseDto;
        }
        param.setProjectList(projectIdList);
        List<AcAbnormalCharge> list =  acAbnormalChargeMapper.listPageAbnormalCharg(param);

        baseDto.setLstDto(list);
        baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询完毕"));
        baseDto.setPage(param.getPage());

        return baseDto;


    }

    /**
     * 分页加载账户列表
     * @param companyId
     * @param parm
     * @return
     */
    public BaseDto listAbnormalChargAccount(String companyId,AbnormalChargeAcccountDto parm){

        if(CommonUtils.isEmpty(companyId)){
            logger.info("{}没有传入项目id");
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"没有传入公司id"));
        }
        BaseDto baseDto=new BaseDto();
        List<String> projectIdList = tSysOrganizationMapper.selectProjectId(parm.getStaffCode());
        if(CommonUtils.isEmpty(projectIdList)){
            baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_ERROR,"你没有权限"));
            return baseDto;
        }
        parm.setProjectList(projectIdList);

        //查询出账户信息
        List<AbnormalChargeAcccountDto>  abnormalAccountList =  acAbnormalChargAccountMapper.listPageAbnormalChargAccount(parm);
        List<AbnormalChargeAcccountDto>  abnormalAccountLists =acAbnormalChargAccountMapper.listPageAbnormalCurest(parm);
        for (AbnormalChargeAcccountDto abnorDto:abnormalAccountList){
            for (AbnormalChargeAcccountDto abs:abnormalAccountLists){
                if (abnorDto.getHouseCode().equals(abs.getHouseCode())){
                    abnorDto.setOwnerName(abs.getOwnerName());
                }
            }

        }



        logger.info("{}查询出账户信息");
        if(CommonUtils.isEmpty(abnormalAccountList) || abnormalAccountList.size()<=0){
            logger.info("{}没有查到数据");
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"没有查到数据"));
        }

        baseDto.setLstDto(abnormalAccountList);
        baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"查询完毕"));
        baseDto.setPage(parm.getPage());

        return baseDto;
    }

    /**
     * 退费处理分二种情况： 1.多收退费  只需要把欠费账户加上相应的金额，资产账户加上相应的金额
     *                    2.少收补扣 操作是一样的
     * @param companyId
     * @param parm
     * @return
     */
    public BaseDto abnormalChargeAccountHandle(String companyId,AcAbnormalCharge parm){

        if(CommonUtils.isEmpty(companyId)){
            logger.info("{}没有传入项目id");
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"没有传入公司id"));
        }

        if(CommonUtils.isEmpty(parm.getAbnormalType())){
            logger.info("{}没有传入异常类型");
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"请选择异常类型"));
        }
        if (CommonUtils.isEmpty(parm.getAmount())){
            logger.info("{}没有传入修改金额");
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"请填写修改金额"));
        }
        updateOldAccountAmount(parm);
        //这里是前端未传入projectId和projectName
        AcCommonAccountDetail acCommonAccountDetail
                =acCommonAccountDetailMapper.selectHouseCodeAnd(parm.getHouseCode());
        parm.setProjectId(acCommonAccountDetail.getProjectId());
        parm.setProjectName(acCommonAccountDetail.getProjectName());
        if(parm.getAbnormalType()==1){
            logger.info("{}多收退费操作开始");
            //相应的欠费账户进行扣减
                    double last=parm.getAmount().doubleValue();

                    AcCommonAccountDetail acCommonAccount= new AcCommonAccountDetail();
                    acCommonAccount.setId(UUID.randomUUID().toString());
                    acCommonAccount.setAccountId(acCommonAccountDetail.getAccountId());
                    //parm.setBeforeAmount(acCommonAccountDetail.getAfterAmount());
                    acCommonAccount.setHouseCodeNew(acCommonAccountDetail.getHouseCodeNew());
                    acCommonAccount.setBeforeAmount(acCommonAccountDetail.getAfterAmount());
                    acCommonAccount.setAfterAmount(new BigDecimal(acCommonAccountDetail.getAfterAmount().doubleValue()+last ));
                    acCommonAccount.setChangeAmount(new BigDecimal(last));
                    acCommonAccount.setBusinessType(BusinessType.PRESTORE.getCode());
                    acCommonAccount.setDikouType(parm.getAccountType());
                    acCommonAccount.setBillDetailId(null);
                    acCommonAccount.setDescription("财务计费多收回退金额");
                    acCommonAccount.setProjectId(acCommonAccountDetail.getProjectId());
                    acCommonAccount.setProjectName(acCommonAccountDetail.getProjectName());
                    acCommonAccount.setCreateTime(new Date());
                    acCommonAccount.setCreateId(parm.getOperaId());
                    acCommonAccount.setOperaId(parm.getOperaId());
                    //对项目账户进行添加
                    createProjectAccount(parm,companyId);
                    acCommonAccountDetailMapper.insertSelective(acCommonAccount);
                    acAccountMapper.updateByLastArreasAmount(parm.getAmount().doubleValue(),parm.getHouseCode());

                    BigDecimal amount=parm.getAmount();
                    parm.setAmount(amount.multiply(new BigDecimal(-1)));
        } else if(parm.getAbnormalType()==2){
            logger.info("{}少收扣费操作开始");
            //相应的欠费账户进行扣减
            acAbnormalChargAccountMapper.updateByHouseCodeAndAccountTypeForAccount(parm);
            AcCurrentChargeDetail acCurrentChargeDetail =new AcCurrentChargeDetail();

            acCurrentChargeDetail.setId(CommonUtils.getUUID());
            acCurrentChargeDetail.setHouseCodeNew(parm.getHouseCode());
            acCurrentChargeDetail.setChargeAmount(parm.getAmount());
            acCurrentChargeDetail.setAccountId(acCurrentChargeMapper.selectAccountId(parm.getHouseCode()));
            acCurrentChargeDetail.setAccountType(parm.getAccountType());
            acCurrentChargeDetail.setCreateTime(new Date());
            acCurrentChargeDetail.setChargeTime(acCurrentChargeDetail.getCreateTime());
            acCurrentChargeDetail.setCommonDikou(new BigDecimal(0.0));
            acCurrentChargeDetail.setSpecialDikou(new BigDecimal(0.0));
            acCurrentChargeDetail.setProjectId(parm.getProjectId());
            acCurrentChargeDetail.setProjectName(parm.getProjectName());
            acCurrentChargeDetail.setCreateId("system");
            acCurrentChargeDetail.setPayableAmount(new BigDecimal(0.0));
            acCurrentChargeDetail.setAssignAmount(new BigDecimal(0.0));
            acCurrentChargeDetail.setCurrenctArreas(new BigDecimal(0.0));
            acCurrentChargeDetail.setCurrentPayment(new BigDecimal(0.0));
            acCurrentChargeDetail.setUpdateTime(acCurrentChargeDetail.getCreateTime());
            acCurrentChargeDetail.setPayableAmount(new BigDecimal(0.0));
            acCurrentChargeDetail.setPayableAmount(new BigDecimal(0));
            acCurrentChargeDetail.setOperaId(parm.getOperaId());
            acCurrentChargeDetail.setBusinessType(1);
            acCurrentChargeDetail.setBusinessTypeEnum(AcChargeDetailBusinessTypeEnum.CHARGE);
            acCurrentChargeDetail.setBusinessType(AcChargeDetailBusinessTypeEnum.CHARGE.getCode());
            acCurrentChargeDetailMapper.insertSelective(acCurrentChargeDetail);

            // 资产账户进行扣减
           acAbnormalChargAccountMapper.updateAcAccountByHouseCodeNewForAccount(parm);

        }

        //插入异常计费表流水
        logger.info("{}插入异常计费表流水开始");

        parm.setCreateTime(new Date());

        acAbnormalChargeMapper.insertAcAbnormalCharge(parm);

        BaseDto baseDto = new BaseDto();
        baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"处理成功"));

        return baseDto;
    }

    private void createProjectAccount(AcAbnormalCharge parm,String companyId) {
        projectAccountService.changePrestoreAccount(companyId, parm.getProjectId(), BusinessType.PRESTORE,AccountType.COMMON,
                null, parm.getAmount(), parm.getHouseCode(), parm.getOperaId(), CommonUtils.getUUID(), parm.getOperaId(), null);

    }

    private void updateOldAccountAmount(AcAbnormalCharge parm) {
        if(parm.getAbnormalType()==1){

            tBsAssetAccountMapper.updateAccountAndTypeAndAmount(parm.getAmount().doubleValue(),0,parm.getHouseCode());
           tBsAssetAccountStreamMapper.insertAndAmountByBuildingCode(parm.getHouseCode(),0,parm.getAmount().doubleValue(),parm.getOperaId(),"计费多收充值操作，财务操作");
        }else if (parm.getAbnormalType()==2){
            tBsAssetAccountMapper.updateAccountAndTypeAndAmount(-parm.getAmount().doubleValue(),parm.getAccountType(),parm.getHouseCode());
            tBsAssetAccountStreamMapper.insertAndAmountByBuildingCode(parm.getHouseCode(),parm.getAccountType(),-parm.getAmount().doubleValue(),parm.getOperaId(),"计费少数，财务操作");
        }

    }

}
