package com.everwing.coreservice.wy.fee.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.constant.Constants;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.BillingEnum;
import com.everwing.coreservice.common.wy.common.enums.StreamEnum;
import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfo;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import com.everwing.coreservice.common.wy.fee.dto.*;
import com.everwing.coreservice.common.wy.fee.entity.*;
import com.everwing.coreservice.common.wy.fee.service.*;
import com.everwing.coreservice.wy.fee.dao.mapper.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.*;

@Component
@Service(timeout = 12000)
public class FinaceAccountPageServiceImpl implements FinaceAcAccountPageService {

	Logger logger = Logger.getLogger(FinaceAccountPageServiceImpl.class);

    @Autowired
    private AcBillDetailMapper acBillDetailMapper;

    @Autowired
    private AcCurrentChargeDetailMapper currentChargeDetailMapper;

    @Autowired
    private AcLateFeeStreamMapper lateFeeStreamMapper;

    @Autowired
    private AcAccountMapper acAccountMapper;

    @Autowired
    private  AcSpecialAccountMapper acSpecialAccountMapper;

    @Autowired
    private AcDelayAccountMapper acDelayAccountMapper;

    @Autowired
    private TJgStaffGropMapper tJgStaffGropMapper;

    @Autowired
    private TJgTotalCalculationMapper tJgTotalCalculationMapper;

    @Autowired
    private AcLastBillFeeInfoMapper acLastBillFeeInfoMapper;

    @Autowired
    private TSysUserMapper tSysUserMapper;

    @Autowired
    private TBsAssetAccountMapper tBsAssetAccountMapper;

    @Autowired
    private TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;

    @Autowired
    private  TBsOwedHistoryMapper tBsOwedHistoryMapper;

    @Autowired
    private  TBsChargingSchemeMapper tBsChargingSchemeMapper;

    @Autowired
    private TcBuildingMapper tcBuildingMapper;

    @Autowired
    private TBsAssetAccountStreamMapper tBsAssetAccountStreamMapper;

    @Autowired
    private TBsPayInfoMapper tBsPayInfoMapper;

    @Autowired
    private TSysProjectMapper tSysProjectMapper;

    @Autowired
    private TSysOrganizationMapper tSysOrganizationMapper;

    @Autowired
    private AcAccountLateFeeService acAccountLateFeeService;

    @Autowired
    private AcBusinessOperaService acBusinessOperaService;

    @Autowired
    private AcOrderService acOrderService;

    @Autowired
    private AcAccountService acAccountService;
    private static final DecimalFormat df = new DecimalFormat("#.00");
    @Override
    public BaseDto listPageBillInfoByBuildingFinace(String companyId, FinaceAccount finaceAccount) {
        BaseDto baseDto=new BaseDto();
        List<String> projectIdList = tSysOrganizationMapper.selectProjectId(finaceAccount.getStaffCode());
        if(CommonUtils.isEmpty(projectIdList)){
            baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_ERROR,"你没有权限"));
            return baseDto;
        }
         finaceAccount.setProjectList(projectIdList);
        List<BuildingInfoDto> buildingInfoDtos=acAccountMapper.listPageBuildingFinace(finaceAccount);
        baseDto.setLstDto(buildingInfoDtos);
        baseDto.setPage(finaceAccount.getPage());
        return baseDto;

    }




    @Override
    public BaseDto listPageBuildingInfo(String companyId,BuildingInfoDto buildingInfoDto) {
        List<BuildingInfoDto> buildingInfoDtos=acAccountMapper.listPageBuildingInfo(buildingInfoDto);
        BaseDto baseDto=new BaseDto();
        baseDto.setLstDto(buildingInfoDtos);
        baseDto.setPage(buildingInfoDto.getPage());
        return baseDto;
    }

    @Override
    public BaseDto listPageBuildingInfoByCustId(String companyId, BuildingInfoDto buildingInfoDto) {
        List<BuildingInfoDto> buildingInfoDtos=acAccountMapper.listPageBuildingInfoByCustId(buildingInfoDto);
        BaseDto baseDto=new BaseDto();
        baseDto.setLstDto(buildingInfoDtos);
        baseDto.setPage(buildingInfoDto.getPage());
        return baseDto;
    }


    @Override
    public BaseDto downLoadBill(String companyId,String id) {
        BaseDto baseDto=new BaseDto();
        AcBillDetail acBillDetail=acBillDetailMapper.selectByPrimaryKey(id);
        if(acBillDetail!=null) {
            baseDto.setObj(acBillDetail.getBillDetail());
        }
        return baseDto;
    }

    @Override
    public BaseDto loadBillInfoByBuildingCodeAndYear(String companyId,String buildingCode,String year) {
        BaseDto baseDto=new BaseDto();
        List<AcBillDetail> billDetails=acBillDetailMapper.selectByHouseCodeNewAndYear(buildingCode,year);
        baseDto.setLstDto(billDetails);
        return baseDto;
    }

    @Override
    public BaseDto loadAccountInfoByBuildingCode(String companyId, String buildingCode) {
        BaseDto baseDto=new BaseDto();
        Map<String,Object> accountInfo=acAccountMapper.selectAccountInfoByBuildingCode(buildingCode);
        baseDto.setObj(accountInfo);
        return baseDto;
    }

    @Override
    public BaseDto listPageCurrentChargeDetail(String companyId, OrderDetailInfoDto orderDetailInfoDto) {
        BaseDto baseDto=new BaseDto();
        List<Map<String,Object>> chargeDetailList=currentChargeDetailMapper.listPageBySearchObj(orderDetailInfoDto);
        baseDto.setLstDto(chargeDetailList);
        baseDto.setPage(orderDetailInfoDto.getPage());
        return baseDto;
    }

    @Override
    public BaseDto listPageLateFee(String companyIdByCurrRequest, LateFeeInfoDto lateFeeInfoDto) {
        BaseDto baseDto=new BaseDto();
        List<Map<String,Object>> lateFeeDetailList=lateFeeStreamMapper.listPageBySearchObj(lateFeeInfoDto);
        baseDto.setLstDto(lateFeeDetailList);
        baseDto.setPage(lateFeeInfoDto.getPage());
        return baseDto;
    }

    @Override
    public BaseDto listPagePrestoreDetail(String companyId,PreStoreInfoDto preStoreInfoDto) {
        BaseDto baseDto=new BaseDto();
        List<Map<String,Object>> lateFeeDetailList=acAccountMapper.listPagePrestoreDetail(preStoreInfoDto);
        baseDto.setLstDto(lateFeeDetailList);
        baseDto.setPage(preStoreInfoDto.getPage());
        return baseDto;
    }
    @Override
    public BaseDto jmMoney(String companyId,TBsPayInfo info){

        List<AcDelayAccount> accounts = acDelayAccountMapper.selectByHouseCodeNew( info.getBuildingCode());
        if(CommonUtils.isEmpty(accounts)){
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"未找到对应的账户数据."));
        }
        for(AcDelayAccount dto:accounts){
            if(dto.getAccountType()==1 && info.getWyAmount()>0){
                double jmff = acDelayAccountMapper.selectByHouseCodeNewAndType( info.getBuildingCode(),dto.getAccountType());
                if( info.getWyAmount() > jmff){
                    return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"物业减免金额大于违约金"));
                }

            }
            if(dto.getAccountType()==2 && info.getBtAmount()>0){
                double jmff = acDelayAccountMapper.selectByHouseCodeNewAndType( info.getBuildingCode(),dto.getAccountType());
                if( info.getWyAmount() > jmff){
                    return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"本体减免金额大于违约金"));
                }

            }
            if(dto.getAccountType()==3 && info.getWaterAmount()>0){
                double jmff = acDelayAccountMapper.selectByHouseCodeNewAndType( info.getBuildingCode(),dto.getAccountType());
                if( info.getWyAmount() > jmff){
                    return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"水费减免金额大于违约金"));
                }

            }
            if(dto.getAccountType()==4 && info.getElectAmount()>0){
                double jmff = acDelayAccountMapper.selectByHouseCodeNewAndType( info.getBuildingCode(),dto.getAccountType());
                if( info.getWyAmount() > jmff){
                    return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"电费减免金额大于违约金"));
                }

            }
        }

        info.setProjectId(accounts.get(0).getProjectId());
        info.setProjectName(accounts.get(0).getProjectName());

        String batchNo = "PAY" + new DateTime().toString("yyyyMMddHHmmssSSS")
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9);


        Map<String, String> amountMap = new HashMap<>();
        amountMap.put("wyJmAmount", CommonUtils.null2String( info.getWyAmount() ));
        amountMap.put("btJmAmount", CommonUtils.null2String( info.getBtAmount() ));
        amountMap.put("waterJmAmount", CommonUtils.null2String( info.getWaterAmount() ));
        amountMap.put("electJmAmount", CommonUtils.null2String( info.getElectAmount() ));

        acAccountLateFeeService.addLateFeeInfoForReduction(companyId, amountMap, info.getBuildingCode(),info.getProjectId(),info.getProjectName(),batchNo,info.getCreateId());

        //老账户数据处理


        String buildingCodes = this.tcBuildingMapper.getBuildingCodeByHouseCode(info.getBuildingCode());

        List<TBsAssetAccount> oldAccounts = this.tBsAssetAccountMapper.getAccountsByBuildingCode(buildingCodes);
        TSysUserSearch condition = new TSysUserSearch();
        condition.setUserId(info.getCreateId());
        List<TSysUserList> users = this.tSysUserMapper.findByCondition(condition);
        String staffName = (users.isEmpty()) ? "" : users.get(0).getStaffName();
        for(TBsAssetAccount account : oldAccounts){
            double jmAmount = (account.getType() == BillingEnum.ACCOUNT_TYPE_WY.getIntV()) ? info.getWyAmount() :
                    (account.getType() == BillingEnum.ACCOUNT_TYPE_BT.getIntV()) ? info.getBtAmount() :
                            (account.getType() == BillingEnum.ACCOUNT_TYPE_WATER.getIntV()) ? info.getWaterAmount() :
                                    (account.getType() == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV()) ? info.getElectAmount() :
                                            (account.getType() == BillingEnum.ACCOUNT_TYPE_COMMON.getIntV()) ? info.getCommonAmount() : 0;

            if(CommonUtils.isEmpty(jmAmount))
                continue;

            account.setAccountBalance(CommonUtils.null2Double(account.getAccountBalance()) + CommonUtils.null2Double(jmAmount));
            account.setModifyId(info.getCreateId());
            account.setModifyTime(new Date());

            //减免违约金
            TBsChargeBillHistory history = this.tBsChargeBillHistoryMapper.selectNotBillingByObj(account.getBuildingCode(),account.getType());

            //减免, 不跳过违约金
            oprLateFee(account, jmAmount, history, Constants.STR_YES,info);

            this.tBsAssetAccountMapper.update(account);

            this.tBsAssetAccountStreamMapper.singleInsert(new TBsAssetAccountStream(CommonUtils.getUUID(),
                            account.getId(),
                            jmAmount,
                            new Date(),
                            info.getCreateId(),
                            staffName,
                            StreamEnum.purpose_jm.getV()  //减免
                    )
            );
        }
        info.setId(CommonUtils.getUUID());
        info.setCreateTime(new Date());
        info.setModifyTime(new Date());
        info.setModifyId(info.getCreateId());
        info.setBatchNo(batchNo);
        this.tBsPayInfoMapper.insert(info);
        return  new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"账户减免完成."));
    }
    @Override
    public BaseDto pay2Account(String companyId, TBsPayInfo info, String singleStr, String isNotSkipLateFee) {
        if(null == info || CommonUtils.isEmpty(singleStr)){

            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "传入参数为空,交费充值失败."));
        }
        String  houseCode = info.getBuildingCodes().get(0);
        List<AcLastBillFeeInfo> dtos = acLastBillFeeInfoMapper.selectByHouseCodeNew(houseCode);
        info.setProjectName(dtos.get(0).getProjectName());
        info.setProjectId(dtos.get(0).getProjectId());
        TSysUserSearch condition = new TSysUserSearch();
        condition.setUserId(info.getCreateId());
        List<TSysUserList> userList = this.tSysUserMapper.findByCondition(condition);
        String staffName = (CommonUtils.isEmpty(userList)) ? null : userList.get(0).getStaffName();
        String staffNumber = (CommonUtils.isEmpty(userList)) ? "" : userList.get(0).getStaffNumber();
        //校验充值的钱是否相等
        double czAmount = CommonUtils.getSum(info.getWyAmount(),info.getBtAmount(),info.getWaterAmount(),info.getElectAmount(),info.getCommonAmount());
        double scAmount = CommonUtils.getSum(info.getPayCash(),info.getPayUnion(),info.getPayWx(),info.getPayZfb(),info.getPayBank());
        if(czAmount == 0){

            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "充值金额为空,请不要做冲零操作. "));
        }
        if(scAmount == 0){

            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "实充金额为空,请不要做冲零操作. "));
        }
        if(czAmount != scAmount){

            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING, "充值金额 :[" + czAmount + "]元, 与实充金额 :[" + scAmount + "]元不相等, 充值失败. 请检查."));
        }

        String batchNo = "PAY" + new DateTime().toString("yyyyMMddHHmmssSSS")
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9);
        Map<String, String> amountMap = new HashMap<>();
        amountMap.put("wyAmount", info.getWyAmount().toString() );
        amountMap.put("btAmount", info.getBtAmount().toString());
        amountMap.put("waterAmount",info.getWaterAmount().toString());
        amountMap.put("electAmount",info.getElectAmount().toString());
        amountMap.put("commonAmount",info.getCommonAmount().toString());

        acAccountService.batchRechargeToNewAccount(companyId, info.getCreateId(),  info.getBuildingCodes(), amountMap, null, batchNo,info.getPayType());

        AcBusinessOperaDetailDto acBusinessOperaDetailDto = new AcBusinessOperaDetailDto();
        acBusinessOperaDetailDto.setOperationId(info.getCreateId());
        acBusinessOperaDetailDto.setOperationTime(new Date());
        acBusinessOperaDetailDto.setBusinessType(5);
        acBusinessOperaDetailDto.setAmount(info.getWyAmount()+ info.getBtAmount()+info.getWaterAmount()+info.getElectAmount()+info.getCommonAmount());
        acBusinessOperaDetailDto.setProjectId(info.getProjectId());
        acBusinessOperaDetailDto.setProjectName(info.getProjectName());
        acBusinessOperaDetailDto.setRemark("财务缴费");
        acBusinessOperaDetailDto.setClientType(1);

        String id = acBusinessOperaService.addOperaDetail(companyId,acBusinessOperaDetailDto);

        AcOrderDto dto = new AcOrderDto();

        dto.setId(UUID.randomUUID().toString());
        dto.setAmount(info.getWyAmount()+ info.getBtAmount()+info.getWaterAmount()+info.getElectAmount()+info.getCommonAmount());
        dto.setOrderState(1);
        dto.setPayState(4);
        dto.setOrderType(3);
        dto.setOperaId(id);
        dto.setSignature("");
        dto.setHouseCodeNew(info.getBuildingCodes().get(0));
        dto.setPaymentCahnnel(info.getPayType());
        dto.setIsRcorded(1);
        List<AcOrderCycleDetailDto> orderCycleDetailList = new ArrayList<AcOrderCycleDetailDto>();
        if(info.getWyAmount()>0){
            AcOrderCycleDetailDto acOrderCycleDetailDto = new AcOrderCycleDetailDto();
            acOrderCycleDetailDto.setBusinessType(1);
            acOrderCycleDetailDto.setDepositType(1);
            acOrderCycleDetailDto.setAccountType(1);
            acOrderCycleDetailDto.setDetailAmount(CommonUtils.isEmpty(info.getWyAmount())?0.00:info.getWyAmount());
            acOrderCycleDetailDto.setHouseCodeNew(info.getBuildingCodes().get(0));
            acOrderCycleDetailDto.setLateAmount(CommonUtils.isEmpty(info.getWyLateFee())?0.00:info.getWyLateFee());
            orderCycleDetailList.add(acOrderCycleDetailDto);
        } else if (info.getBtAmount()>0){
            AcOrderCycleDetailDto acOrderCycleDetailDto = new AcOrderCycleDetailDto();
            acOrderCycleDetailDto.setBusinessType(1);
            acOrderCycleDetailDto.setDepositType(1);
            acOrderCycleDetailDto.setAccountType(2);
            acOrderCycleDetailDto.setDetailAmount(CommonUtils.isEmpty(info.getBtAmount())?0.00:info.getBtAmount());
            acOrderCycleDetailDto.setHouseCodeNew(info.getBuildingCodes().get(0));
            acOrderCycleDetailDto.setLateAmount(CommonUtils.isEmpty(info.getBtLateFee())?0.00:info.getBtLateFee());
            orderCycleDetailList.add(acOrderCycleDetailDto);
        } else if(info.getWaterAmount()>0){
            AcOrderCycleDetailDto acOrderCycleDetailDto = new AcOrderCycleDetailDto();
            acOrderCycleDetailDto.setBusinessType(1);
            acOrderCycleDetailDto.setDepositType(1);
            acOrderCycleDetailDto.setAccountType(3);
            acOrderCycleDetailDto.setDetailAmount(CommonUtils.isEmpty(info.getWaterAmount())?0.00:info.getWaterAmount());
            acOrderCycleDetailDto.setHouseCodeNew(info.getBuildingCodes().get(0));
            acOrderCycleDetailDto.setLateAmount(CommonUtils.isEmpty(info.getWaterLateFee())?0.00:info.getWaterLateFee());
            orderCycleDetailList.add(acOrderCycleDetailDto);
        } else if(info.getElectAmount()>0){
            AcOrderCycleDetailDto acOrderCycleDetailDto = new AcOrderCycleDetailDto();
            acOrderCycleDetailDto.setBusinessType(1);
            acOrderCycleDetailDto.setDepositType(1);
            acOrderCycleDetailDto.setAccountType(4);
            acOrderCycleDetailDto.setDetailAmount(CommonUtils.isEmpty(info.getElectAmount())?0.00:info.getElectAmount());
            acOrderCycleDetailDto.setHouseCodeNew(info.getBuildingCodes().get(0));
            acOrderCycleDetailDto.setLateAmount(CommonUtils.isEmpty(info.getElectLateFee())?0.00:info.getElectLateFee());
            orderCycleDetailList.add(acOrderCycleDetailDto);
        } else {
            AcOrderCycleDetailDto acOrderCycleDetailDto = new AcOrderCycleDetailDto();
            acOrderCycleDetailDto.setBusinessType(1);
            acOrderCycleDetailDto.setDepositType(1);
            acOrderCycleDetailDto.setAccountType(5);
            acOrderCycleDetailDto.setDetailAmount(CommonUtils.isEmpty(info.getCommonAmount())?0.00:info.getCommonAmount());
            acOrderCycleDetailDto.setHouseCodeNew(info.getBuildingCodes().get(0));
            orderCycleDetailList.add(acOrderCycleDetailDto);

        }
        dto.setOrderCycleDetailList(orderCycleDetailList);
        AcBusinessOperaDetailDto operaDetailDto = new AcBusinessOperaDetailDto();
        operaDetailDto.setBusinessType(1);
        operaDetailDto.setPersonType(1);
        operaDetailDto.setClientType(1);

        acOrderService.createCycleOrderInfo( companyId,dto, operaDetailDto);
        // 操作老账户

        boolean isLastFlag = false;
        String buildingCode = info.getBuildingCode();
        String tarId = CommonUtils.getUUID();
        String projectName = null;
        String projectId = null;
        int buildingCount=1;
        List<TBsAssetAccountStream> insertSteamList = new ArrayList<TBsAssetAccountStream>();
        List<TBsPayInfo> insertInfos = new ArrayList<TBsPayInfo>();
        List<String> types = new ArrayList<String>();
        if(info.getWyAmount() > 0) types.add(Constants.STR_ONE);
        if(info.getBtAmount() > 0) types.add(Constants.STR_TWO);
        if(info.getWaterAmount() > 0) types.add(Constants.STR_THREE);
        if(info.getElectAmount() > 0) types.add(Constants.STR_FOUR);
        if(info.getCommonAmount() > 0) types.add(Constants.STR_ZERO);
        List<TBsAssetAccount> accounts = this.tBsAssetAccountMapper.findByBuildingCodeAndItems(buildingCode, types);

        TBsPayInfo insertInfo = new TBsPayInfo();
        insertInfo.setId(CommonUtils.getUUID());
        insertInfo.setBuildingCode(buildingCode);
        insertInfo.setCreateId(info.getCreateId());
        insertInfo.setCustId(info.getCustId());
        insertInfo.setCustName(info.getCustName());
        insertInfo.setProjectId(info.getProjectId());
        insertInfo.setModifyId(info.getModifyId());
        insertInfo.setCreateId(info.getCreateId());
        insertInfo.setJmRemark(info.getJmRemark());		//备注
        insertInfo.setPayType(info.getPayType());
        insertInfo.setPayerName(info.getPayerName());
        insertInfo.setStatus(info.getStatus());
        insertInfo.setRelationId(tarId);
        insertInfo.setBatchNo(batchNo);
        for(TBsAssetAccount account : accounts){
            TBsAssetAccountStream stream = accountRecharge(account, info, isLastFlag,insertInfo,buildingCount,isNotSkipLateFee);
            if(null != stream && CommonUtils.null2Double(stream.getChangMoney()) > 0){
                insertSteamList.add(stream);
            }
        }
        insertInfos.add(insertInfo);

        TSysProject project = this.tSysProjectMapper.findByCode(insertInfo.getProjectId());
        projectName = (null == project) ? null : project.getName();
        projectId = (null == project) ? null : project.getCode();


        if(!insertSteamList.isEmpty()){
            this.tBsAssetAccountStreamMapper.batchInsert(insertSteamList);
        }
        if(!insertInfos.isEmpty()){
            this.tBsPayInfoMapper.batchInsert(insertInfos);
        }

        return  new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"账户充值完成."));
    }
    @Override
    public BaseDto backMoney(String companyId, TBsPayInfo info) {

        List<AcSpecialAccount> accounts = acSpecialAccountMapper.selectByHouseCodeNew( info.getBuildingCode());
        if(CommonUtils.isEmpty(accounts)){
            return new BaseDto(new MessageMap(MessageMap.INFOR_WARNING,"未找到对应的账户数据."));
        }
        info.setProjectId(accounts.get(0).getProjectId());
        info.setProjectName(accounts.get(0).getProjectName());
        TSysUserSearch condition = new TSysUserSearch();
        condition.setUserId(info.getCreateId());
        List<TSysUserList> userList = this.tSysUserMapper.findByCondition(condition);
        String staffName = (CommonUtils.isEmpty(userList)) ? null : userList.get(0).getStaffName();
        String staffNumber = (CommonUtils.isEmpty(userList)) ? "" : userList.get(0).getStaffNumber();

        String batchNo = "PAY" + new DateTime().toString("yyyyMMddHHmmssSSS")
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9)
                + (new Random()).nextInt(9);

        Map<String, String> amountMap = new HashMap<>();
        amountMap.put("wyAmount", info.getWyAmount().toString() );
        amountMap.put("btAmount", info.getBtAmount().toString());
        amountMap.put("waterAmount",info.getWaterAmount().toString());
        amountMap.put("electAmount",info.getElectAmount().toString());
        amountMap.put("commonAmount",info.getCommonAmount().toString());
        acAccountService.depositAccountRefund(companyId, amountMap,  info.getBuildingCode(),info.getProjectId(),info.getProjectName(),batchNo,info.getCreateId());


        AcBusinessOperaDetailDto acBusinessOperaDetailDto = new AcBusinessOperaDetailDto();
        acBusinessOperaDetailDto.setOperationId(info.getCreateId());
        acBusinessOperaDetailDto.setOperationTime(new Date());
        acBusinessOperaDetailDto.setBusinessType(5);
        acBusinessOperaDetailDto.setAmount(info.getWyAmount()+ info.getBtAmount()+info.getWaterAmount()+info.getElectAmount()+info.getCommonAmount());
        acBusinessOperaDetailDto.setProjectId(info.getProjectId());
        acBusinessOperaDetailDto.setProjectName(info.getProjectName());
        acBusinessOperaDetailDto.setRemark("财务缴费");
        acBusinessOperaDetailDto.setClientType(1);

        String id = acBusinessOperaService.addOperaDetail(companyId,acBusinessOperaDetailDto);

        AcOrderDto dto = new AcOrderDto();

        dto.setId(UUID.randomUUID().toString());
        dto.setAmount(info.getWyAmount()+ info.getBtAmount()+info.getWaterAmount()+info.getElectAmount()+info.getCommonAmount());
        dto.setOrderState(1);
        dto.setPayState(4);
        dto.setOrderType(3);
        dto.setOperaId(id);
        dto.setSignature("");
        dto.setHouseCodeNew( info.getBuildingCode());
        dto.setPaymentCahnnel(info.getPayType());
        dto.setIsRcorded(1);
        List<AcOrderCycleDetailDto> orderCycleDetailList = new ArrayList<AcOrderCycleDetailDto>();
        if(info.getWyAmount()>0){
            AcOrderCycleDetailDto acOrderCycleDetailDto = new AcOrderCycleDetailDto();
            acOrderCycleDetailDto.setBusinessType(1);
            acOrderCycleDetailDto.setDepositType(1);
            acOrderCycleDetailDto.setAccountType(1);
            acOrderCycleDetailDto.setDetailAmount(CommonUtils.isEmpty(info.getWyAmount())? 0.00:info.getWyAmount());
            acOrderCycleDetailDto.setHouseCodeNew( info.getBuildingCode());
            acOrderCycleDetailDto.setLateAmount( CommonUtils.isEmpty( info.getWyLateFee())? 0.00: info.getWyLateFee());
            orderCycleDetailList.add(acOrderCycleDetailDto);
        } else if (info.getBtAmount()>0){
            AcOrderCycleDetailDto acOrderCycleDetailDto = new AcOrderCycleDetailDto();
            acOrderCycleDetailDto.setBusinessType(1);
            acOrderCycleDetailDto.setDepositType(1);
            acOrderCycleDetailDto.setAccountType(2);
            acOrderCycleDetailDto.setDetailAmount(CommonUtils.isEmpty(info.getBtAmount()) ? 0.00:info.getBtAmount());
            acOrderCycleDetailDto.setHouseCodeNew( info.getBuildingCode());
            acOrderCycleDetailDto.setLateAmount(CommonUtils.isEmpty(info.getBtLateFee())?0.00:info.getBtLateFee());
            orderCycleDetailList.add(acOrderCycleDetailDto);
        } else if(info.getWaterAmount()>0){
            AcOrderCycleDetailDto acOrderCycleDetailDto = new AcOrderCycleDetailDto();
            acOrderCycleDetailDto.setBusinessType(1);
            acOrderCycleDetailDto.setDepositType(1);
            acOrderCycleDetailDto.setAccountType(3);
            acOrderCycleDetailDto.setDetailAmount(CommonUtils.isEmpty(info.getWaterAmount())?0.00:info.getWaterAmount());
            acOrderCycleDetailDto.setHouseCodeNew( info.getBuildingCode());
            acOrderCycleDetailDto.setLateAmount(CommonUtils.isEmpty(info.getWaterAmount())?0.00:info.getWaterAmount());
            orderCycleDetailList.add(acOrderCycleDetailDto);
        } else if(info.getElectAmount()>0){
            AcOrderCycleDetailDto acOrderCycleDetailDto = new AcOrderCycleDetailDto();
            acOrderCycleDetailDto.setBusinessType(1);
            acOrderCycleDetailDto.setDepositType(1);
            acOrderCycleDetailDto.setAccountType(4);
            acOrderCycleDetailDto.setDetailAmount(CommonUtils.isEmpty(info.getElectAmount())?0.00:info.getElectAmount());
            acOrderCycleDetailDto.setHouseCodeNew( info.getBuildingCode());
            acOrderCycleDetailDto.setLateAmount(CommonUtils.isEmpty(info.getElectLateFee())?0.00:info.getElectLateFee());
            orderCycleDetailList.add(acOrderCycleDetailDto);
        } else {
            AcOrderCycleDetailDto acOrderCycleDetailDto = new AcOrderCycleDetailDto();
            acOrderCycleDetailDto.setBusinessType(1);
            acOrderCycleDetailDto.setDepositType(1);
            acOrderCycleDetailDto.setAccountType(5);
            acOrderCycleDetailDto.setDetailAmount(CommonUtils.isEmpty(info.getCommonAmount())?0.00:info.getCommonAmount());
            acOrderCycleDetailDto.setHouseCodeNew( info.getBuildingCode());
            orderCycleDetailList.add(acOrderCycleDetailDto);

        }
        dto.setOrderCycleDetailList(orderCycleDetailList);
        AcBusinessOperaDetailDto operaDetailDto = new AcBusinessOperaDetailDto();
        operaDetailDto.setBusinessType(1);
        operaDetailDto.getPersonType();
        operaDetailDto.setClientType(1);

        acOrderService.createCycleOrderInfo( companyId,dto, operaDetailDto);

        //老账户


        String buildingCodes = this.tcBuildingMapper.getBuildingCodeByHouseCode(info.getBuildingCode());
        List<TBsAssetAccount> oldAccounts = this.tBsAssetAccountMapper.getAccountsByBuildingCode(buildingCodes);
        String projectName = null;
        for(TBsAssetAccount account : oldAccounts){
            double backAmount = (account.getType() == BillingEnum.ACCOUNT_TYPE_WY.getIntV()) ? info.getWyAmount() :
                    (account.getType() == BillingEnum.ACCOUNT_TYPE_BT.getIntV()) ? info.getBtAmount() :
                            (account.getType() == BillingEnum.ACCOUNT_TYPE_WATER.getIntV()) ? info.getWaterAmount() :
                                    (account.getType() == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV()) ? info.getElectAmount() :
                                            (account.getType() == BillingEnum.ACCOUNT_TYPE_COMMON.getIntV()) ? info.getCommonAmount() : 0;

            if(CommonUtils.isEmpty(backAmount))
                continue;
            account.setAccountBalance(CommonUtils.null2Double(account.getAccountBalance()) - CommonUtils.null2Double(backAmount));
            account.setModifyId(info.getCreateId());
            account.setModifyTime(new Date());
            info.setProjectId(account.getProjectId());
            projectName = account.getProjectName();

            this.tBsAssetAccountMapper.update(account);
            //插入流水
            this.tBsAssetAccountStreamMapper.singleInsert(
                    new TBsAssetAccountStream(CommonUtils.getUUID(),
                            account.getId(),
                            -backAmount,
                            new Date(),
                            info.getCreateId(),
                            staffName,
                            StreamEnum.purpose_back.getV())  //退款
            );
        }
        info.setId(CommonUtils.getUUID());
        info.setCreateTime(new Date());
        info.setModifyTime(new Date());
        info.setBatchNo(batchNo);
        info.setModifyId(info.getCreateId());
        info.setRelationId(CommonUtils.getUUID());
        this.tBsPayInfoMapper.insert(info);

        return new BaseDto(new MessageMap(MessageMap.INFOR_SUCCESS,"账户退款完成."));

    }
    private TBsAssetAccountStream accountRecharge(TBsAssetAccount account ,
                                                  TBsPayInfo info ,
                                                  boolean isLastFlag,
                                                  TBsPayInfo insertInfo,
                                                  int buildingCount,
                                                  String isNotSkipLateFee){
        if(account == null) return null;

        double amount = 0.0;
        double accountBalance = CommonUtils.null2Double(account.getAccountBalance());
        double taxRate = 0.0;
        if(BillingEnum.ACCOUNT_TYPE_COMMON.getIntV() != account.getType()){
            Double tr = this.tBsChargingSchemeMapper.findTaxRate(account.getType(), account.getBuildingCode());
            taxRate = (null == tr) ? 0 : tr.doubleValue();
        }

        insertInfo.setProjectId(account.getProjectId());
        if(!isLastFlag && accountBalance < 0){
            insertInfo.setPayCash(CommonUtils.null2Double(insertInfo.getPayCash()));
            double payCash = CommonUtils.null2Double(info.getPayCash());
            double balanceAbs = Math.abs(accountBalance);
            if(payCash >= balanceAbs) {   //支付的现金大于账户欠费金额 , 因为前面已经判断了账户金额为负数
                insertInfo.setPayCash(CommonUtils.getSum(insertInfo.getPayCash() , balanceAbs));
                info.setPayCash(CommonUtils.getScaleNumber(payCash - balanceAbs,2));
            }else{
                insertInfo.setPayUnion(CommonUtils.null2Double(insertInfo.getPayUnion()));
                double payUnion = CommonUtils.null2Double(info.getPayUnion());
                if((payCash + payUnion) >= balanceAbs){   // 支付的现金加银联刷卡 大于账户欠费金额
                    insertInfo.setPayCash(CommonUtils.getSum(insertInfo.getPayCash(),payCash));
                    insertInfo.setPayUnion(CommonUtils.getSum(insertInfo.getPayUnion(), (balanceAbs - payCash)));
                    info.setPayCash(0.0);
                    info.setPayUnion(CommonUtils.getScaleNumber(payUnion - (balanceAbs - payCash),2));
                }else{
                    insertInfo.setPayWx(CommonUtils.null2Double(insertInfo.getPayWx()));
                    double payWx = CommonUtils.null2Double(info.getPayWx());
                    if((payCash + payUnion + payWx) >= balanceAbs){  //支付的现金, 银联刷卡 , 微信支付大于账户欠费金额
                        insertInfo.setPayCash(CommonUtils.getSum(insertInfo.getPayCash(),payCash));
                        insertInfo.setPayUnion(CommonUtils.getSum(insertInfo.getPayUnion(),payUnion));
                        insertInfo.setPayWx(CommonUtils.getSum(insertInfo.getPayWx() , (balanceAbs - payCash - payUnion)));
                        info.setPayCash(0.0);
                        info.setPayUnion(0.0);
                        info.setPayWx(CommonUtils.getScaleNumber(payWx - (balanceAbs - payCash - payUnion),2));
                    }else{

                        //加上支付宝额度扣取
                        insertInfo.setPayZfb(CommonUtils.null2Double(insertInfo.getPayZfb()));
                        double payZfb = CommonUtils.null2Double(info.getPayZfb());
                        if((payCash + payUnion + payWx + payZfb) >= balanceAbs){
                            insertInfo.setPayCash(CommonUtils.getSum(insertInfo.getPayCash(),payCash));
                            insertInfo.setPayUnion(CommonUtils.getSum(insertInfo.getPayUnion(),payUnion));
                            insertInfo.setPayWx(CommonUtils.getSum(insertInfo.getPayWx(),payWx));
                            insertInfo.setPayZfb(CommonUtils.getSum(insertInfo.getPayZfb() , (balanceAbs - payCash - payUnion - payWx)));
                            info.setPayCash(0.0);
                            info.setPayUnion(0.0);
                            info.setPayWx(0.0);
                            info.setPayZfb(CommonUtils.getScaleNumber(payZfb - (balanceAbs - payCash - payUnion - payWx),2));
                        }else{
                            //加上银行账号转款进行扣取
                            insertInfo.setPayBank(CommonUtils.null2Double(insertInfo.getPayBank()));
                            double payBank = CommonUtils.null2Double(info.getPayBank());
                            if((payCash + payUnion + payWx + payZfb + payBank) >= balanceAbs){
                                insertInfo.setPayCash(CommonUtils.getSum(insertInfo.getPayCash(),payCash));
                                insertInfo.setPayUnion(CommonUtils.getSum(insertInfo.getPayUnion(),payUnion));
                                insertInfo.setPayWx(CommonUtils.getSum(insertInfo.getPayWx(),payWx));
                                insertInfo.setPayZfb(CommonUtils.getSum(insertInfo.getPayWx(),payZfb));
                                insertInfo.setPayBank(CommonUtils.getSum(insertInfo.getPayBank(), (balanceAbs - payCash - payUnion - payWx - payZfb)));
                                info.setPayCash(0.0);
                                info.setPayUnion(0.0);
                                info.setPayWx(0.0);
                                info.setPayZfb(0.0);
                                info.setPayBank(CommonUtils.getScaleNumber(payBank - (balanceAbs - payCash - payUnion - payWx - payZfb), 2));
                            }
                        }
                    }
                }
            }
        }

        if(account.getType() == BillingEnum.ACCOUNT_TYPE_WY.getIntV() && info.getWyAmount() > 0 ){

            if(isLastFlag){ //最后一条数据, 则将所有的钱都转入到该账户
                amount = info.getWyAmount();
            }else{
                if(accountBalance > 0) return null;
                amount = ( Math.abs(accountBalance) >= info.getWyAmount() ) ? info.getWyAmount() : Math.abs(accountBalance);
            }
            account.setAccountBalance(accountBalance + amount);
            info.setWyAmount(info.getWyAmount() - amount);
            insertInfo.setWyAmount(amount);
            insertInfo.setWyTax(CommonUtils.getTax(amount, taxRate));
        }

        else if(account.getType() == BillingEnum.ACCOUNT_TYPE_BT.getIntV() && info.getBtAmount() > 0){
            if(isLastFlag){
                amount = info.getBtAmount();
            }else{
                if(accountBalance > 0) return null;
                amount = (Math.abs(accountBalance) >= info.getBtAmount() ) ? info.getBtAmount() : Math.abs(accountBalance);
            }
            account.setAccountBalance(accountBalance + amount);
            info.setBtAmount(info.getBtAmount() - amount);
            insertInfo.setBtAmount(amount);
            insertInfo.setBtTax(CommonUtils.getTax(amount, taxRate));
        }

        else if(account.getType() == BillingEnum.ACCOUNT_TYPE_WATER.getIntV() && info.getWaterAmount() > 0){
            if(isLastFlag){
                amount = info.getWaterAmount();
            }else{
                if(accountBalance > 0) return null;
                amount = (Math.abs(accountBalance) >= info.getWaterAmount() ) ? info.getWaterAmount() : Math.abs(accountBalance);
            }
            account.setAccountBalance(accountBalance + amount);
            info.setWaterAmount(info.getWaterAmount() - amount);
            insertInfo.setWaterAmount(amount);
            insertInfo.setWaterTax(CommonUtils.getTax(amount, taxRate));
        }
        else if(account.getType() == BillingEnum.ACCOUNT_TYPE_ELECT.getIntV() && info.getElectAmount() > 0){

            if(isLastFlag){
                amount = info.getElectAmount();
            }else{
                if(accountBalance > 0) return null;
                amount = (Math.abs(accountBalance) >= info.getElectAmount() ) ? info.getElectAmount() : Math.abs(accountBalance);
            }
            account.setAccountBalance(accountBalance + amount);
            info.setElectAmount(info.getElectAmount() - amount);
            insertInfo.setElectAmount(amount);
            insertInfo.setElectTax(CommonUtils.getTax(amount, taxRate));
        }
        else if(account.getType() == BillingEnum.ACCOUNT_TYPE_COMMON.getIntV() && info.getCommonAmount() > 0){
            amount = info.getCommonAmount() / buildingCount;
            account.setAccountBalance(accountBalance + amount);
            insertInfo.setCommonAmount(amount);
        }

        TBsChargeBillHistory history = this.tBsChargeBillHistoryMapper.selectNotBillingByObj(account.getBuildingCode(),account.getType());

        //违约金抵扣
        oprLateFee(account, amount, history,isNotSkipLateFee, insertInfo);

        this.tBsAssetAccountMapper.update(account);

        //下月的分账单的上期已付更新, 本月分账单的缴费额更新
        if(history != null){
            //找到上期的history 即本月账单
            TBsChargeBillHistory lastHistory = this.tBsChargeBillHistoryMapper.findById(history.getLastBillId());
            if(null != lastHistory){
                double ncd = CommonUtils.calKf(lastHistory.getCurrentBillFee(), lastHistory.getCommonDesummoney(), lastHistory.getNoCommonDesummoney(), amount);
                lastHistory.setNoCommonDesummoney(ncd);
                this.tBsChargeBillHistoryMapper.updateBillHistory(lastHistory);
            }

            double lp = CommonUtils.calKf(history.getLastBillFee(), 0.0, history.getLastPayed(), amount);
            history.setLastPayed(lp);
            this.tBsChargeBillHistoryMapper.updateBillHistory(history);
        }

        return new TBsAssetAccountStream(CommonUtils.getUUID(),
                account.getId(),
                amount,
                new Date(),
                info.getModifyId(),
                info.getModifyId(),
                StreamEnum.purpose_pay_by_person.getV()	//业主交费
        );
    }

    private void oprLateFee(TBsAssetAccount account,
                            double amount,
                            TBsChargeBillHistory history,
                            String isNotSkipLateFee,
                            TBsPayInfo info){
        //违约金抵扣
        boolean isTrue = (history != null && account.getType() != BillingEnum.ACCOUNT_TYPE_COMMON.getIntV());


        List<TBsOwedHistory> histories = this.tBsOwedHistoryMapper.findAllByAccountId(account.getId());
        if(CommonUtils.isNotEmpty(histories)){

            List<HashMap> lastOwedHistory = (isTrue) ? JSONObject.parseArray(history.getLastOwedInfo(), HashMap.class) : null;
            isTrue = CommonUtils.isNotEmpty(lastOwedHistory);

            double dkAmount = amount;

            //先抵扣违约金, 选择不跳过违约金缴纳
            if(CommonUtils.isEquals(Constants.STR_YES, isNotSkipLateFee)){
                double dkLateFee = 0.0;
                for(TBsOwedHistory oh : histories){

                    if(null == oh){
                        continue;
                    }
                    if(dkAmount <= 0 ) break;

                    double lateFee = CommonUtils.null2Double(oh.getTotalLateFee());

                    if(lateFee > dkAmount) {
                        oh.setTotalLateFee(CommonUtils.getScaleNumber(lateFee - dkAmount, 2));

                        //违约金没有抵扣完
                        if(isTrue){
                            for(HashMap m : lastOwedHistory){
                                if(CommonUtils.isEquals(CommonUtils.null2String(m.get("id")), oh.getId())){
                                    m.put("totalLateFee", oh.getTotalLateFee());
                                    m.put("dkLateFee", CommonUtils.getScaleNumber(dkAmount + CommonUtils.null2Double(m.get("dkLateFee")), 2));
                                    m.put("oprTime", new Date());
                                    m.put("isUsed", Constants.IS_USED_USING);
                                    break;
                                }
                            }
                        }
                        dkLateFee += dkAmount;
                        dkAmount = 0.0;
                    }else{
                        oh.setTotalLateFee(0.0);
                        dkAmount = CommonUtils.getScaleNumber(dkAmount - lateFee, 2);
                        //违约金抵扣完成
                        if(isTrue){
                            for(HashMap m : lastOwedHistory){
                                if(CommonUtils.isEquals(CommonUtils.null2String(m.get("id")), oh.getId())){
                                    m.put("totalLateFee", oh.getTotalLateFee());
                                    m.put("dkLateFee", CommonUtils.getScaleNumber(lateFee + CommonUtils.null2Double(m.get("dkLateFee")), 2));
                                    m.put("oprTime", new Date());
                                    m.put("owedAmount", oh.getOwedAmount());
                                    if(oh.getOwedAmount() == 0){
                                        m.put("isUsed", Constants.IS_USED_STOP);
                                        m.put("owedEndTime", new Date());

                                    }else{
                                        m.put("isUsed", Constants.IS_USED_USING);
                                    }
                                    break;
                                }
                            }
                        }
                        dkLateFee += lateFee;
                        if(oh.getOwedAmount() == 0){
                            oh.setIsUsed(BillingEnum.IS_USED_STOP.getIntV());
                            oh.setOwedEndTime(new Date());
                        }
                    }
                }

                dkLateFee = CommonUtils.getScaleNumber(dkLateFee, 2);
                switch (account.getType()) {
                    case 1: info.setWyLateFee(dkLateFee); break;
                    case 2: info.setBtLateFee(dkLateFee); break;
                    case 3: info.setWaterLateFee(dkLateFee); break;
                    case 4: info.setElectLateFee(dkLateFee); break;
                    default:
                        break;
                }

            }


            //再抵扣本金
            if(dkAmount > 0){
                for(TBsOwedHistory oh : histories){
                    if(dkAmount <= 0 ) break;

                    double owedAmount = CommonUtils.null2Double(oh.getOwedAmount());
                    if(owedAmount > dkAmount){
                        //欠费金额大于当前可抵扣金额
                        oh.setOwedAmount(CommonUtils.getScaleNumber(owedAmount - dkAmount, 2));
                        if(isTrue){
                            for(HashMap m : lastOwedHistory){
                                if(CommonUtils.isEquals(CommonUtils.null2String(m.get("id")), oh.getId())){
                                    m.put("owedAmount", oh.getOwedAmount());
                                    m.put("isUsed", Constants.IS_USED_USING);
                                    m.put("dkOwedAmount", CommonUtils.getScaleNumber(dkAmount + CommonUtils.null2Double(m.get("dkOwedAmount")),2));
                                    m.put("oprTime", new Date());
                                    break;
                                }
                            }
                        }
                        dkAmount = 0.0;
                    }else{
                        //欠费金额小于当前可抵扣金额, 即抵扣完成
                        if(isTrue){
                            for(HashMap m : lastOwedHistory){
                                if(CommonUtils.isEquals(CommonUtils.null2String(m.get("id")), oh.getId())){
                                    m.put("owedAmount", 0.0);
                                    m.put("dkOwedAmount", CommonUtils.getScaleNumber(oh.getOwedAmount() + CommonUtils.null2Double(m.get("dkOwedAmount")),2));
                                    if(CommonUtils.isEmpty(oh.getTotalLateFee()))
                                        m.put("isUsed", Constants.IS_USED_STOP);
                                    m.put("owedEndTime", new Date());
                                    m.put("oprTime", new Date());
                                    break;
                                }
                            }
                        }

                        oh.setOwedAmount(0.0);
                        dkAmount = CommonUtils.getScaleNumber(dkAmount - owedAmount, 2);
                        oh.setOwedEndTime(new Date());
                        if(CommonUtils.isEmpty(oh.getTotalLateFee())){
                            oh.setIsUsed(BillingEnum.IS_USED_STOP.getIntV());
                        }
                    }
                }
            }

            for(TBsOwedHistory oh : histories){
                this.tBsOwedHistoryMapper.update(oh);
            }

            if(isTrue){
                history.setLastOwedInfo(JSONArray.toJSONString(lastOwedHistory));
                this.tBsChargeBillHistoryMapper.updateBillHistory(history);
            }
        }
    }


}
