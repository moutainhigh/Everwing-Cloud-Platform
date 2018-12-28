package com.everwing.coreservice.wy.fee.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.exception.VersionException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.RateUtils;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.fee.constant.*;
import com.everwing.coreservice.common.wy.fee.dto.AcBusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderDto;
import com.everwing.coreservice.common.wy.fee.entity.*;
import com.everwing.coreservice.common.wy.fee.service.ProjectAccountService;
import com.everwing.coreservice.wy.fee.dao.mapper.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 项目账户serviceImpl
 *
 * @author DELL shiny
 * @create 2018/5/16
 */
@Service
@Component
public class ProjectAccountServiceImpl implements ProjectAccountService {

    private static final Logger logger = LogManager.getLogger(ProjectAccountServiceImpl.class);

    @Autowired
    private ProjectAccountMapper projectAccountMapper;

    @Autowired
    private ProjectPrestoreAccountMapper projectPrestoreAccountMapper;

    @Autowired
    private ProjectPrestoreDetailMapper projectPrestoreDetailMapper;

    @Autowired
    private ProjectCycleAccountMapper projectCycleAccountMapper;

    @Autowired
    private ProjectCycleDetailMapper projectCycleDetailMapper;

    @Autowired
    private ProjectProductDetailMapper projectProductDetailMapper;

    @Autowired
    private ProjectDelayAccountMapper projectDelayAccountMapper;
    @Autowired
    private ProjectFineDetailMapper projectFineDetailMapper;

    @Autowired
    private ProjectRefundDetailMapper projectRefundDetailMapper;

    @Autowired
    private ProjectDelayDetailMapper projectDelayDetailMapper;

    @Autowired
    private RateUtils rateUtils;

    @Override
    public boolean rollbock(String companyId, String houseCodeNew, String projectId, String operaId) {
        logger.info("{}开始查询项目账户信息");
        ProjectAccount projectAccount=projectAccountMapper.selectByCompanyIdAndProjectId(companyId, projectId);
        if (null == projectAccount){
            logger.info("{}未查询到项目账户信息,开始新建项目账户。");
            throw new ECPBusinessException("未查到项目项目账户回退失败!");
        }
        //对资产账户进行操作
        List<ProjectPrestoreDetail>projectPrestoreDetailList=projectPrestoreDetailMapper.selectByOperaIdAndType(operaId);
        for (ProjectPrestoreDetail projectPrestoreDetail:projectPrestoreDetailList){
            // 对项目账户预存流水进行删除，预存账户金额减去流水金额，项目账户总表减去流水金额
            projectPrestoreDetailMapper.deleteByPrimaryKey(projectPrestoreDetail.getId());
            ProjectPrestoreAccount prestoreAccount=projectPrestoreAccountMapper.selectByPrimaryKey(projectPrestoreDetail.getPrestoreAccount());
            prestoreAccount.setAmount(prestoreAccount.getAmount().subtract(projectPrestoreDetail.getAmount()));
            prestoreAccount.setUpdateTime(new Date());
            projectPrestoreAccountMapper.updateByPrimaryKey(prestoreAccount);
            projectAccount.setPredepositAmount(projectAccount.getPredepositAmount().subtract(projectPrestoreDetail.getAmount()));
            projectAccount.setTotalAmount(projectAccount.getTotalAmount().subtract(projectPrestoreDetail.getAmount()));
        }
        // 对项目账户滞纳金流水进行删除，滞纳金账户金额减去流水金额，项目账户总表减去流水金额
        List<ProjectDelayDetail> projectDelayDetailList=projectDelayDetailMapper.selectByOperaAndType(operaId);
        for (ProjectDelayDetail projectDelayDetail:projectDelayDetailList){
            projectDelayDetailMapper.deleteByPrimaryKey(projectDelayDetail.getId());
            ProjectDelayAccount projectDelayAccount =projectDelayAccountMapper.selectByPrimaryKey(projectDelayDetail.getDelayAccountId());
            projectDelayAccount.setAmount(projectDelayAccount.getAmount().subtract(projectDelayDetail.getAmount()));
            projectDelayAccount.setUpdateTime(new Date());
            projectDelayAccountMapper.updateByPrimaryKeySelective(projectDelayAccount);
            projectAccount.setLateAmount(projectAccount.getLateAmount().subtract(projectDelayDetail.getAmount()));
            projectAccount.setTotalAmount(projectAccount.getTotalAmount().subtract(projectDelayDetail.getAmount()));
        }
        // 对项目账户周期性收费流水进行删除，周期性账户金额减去流水金额，项目账户总表减去流水金额
        List<ProjectCycleDetail> projectCycleDetailList= projectCycleDetailMapper.selectByOperaIdAndType(operaId);
        for (ProjectCycleDetail projectCycleDetail:projectCycleDetailList){
            projectCycleDetailMapper.deleteByPrimaryKey(projectCycleDetail.getId());
            ProjectCycleAccount projectCycleAccount=projectCycleAccountMapper.selectByPrimaryKey(projectCycleDetail.getCycleId());
            projectCycleAccount.setAmount(projectCycleAccount.getAmount().subtract(projectCycleDetail.getAmount()));
            projectCycleAccount.setUpdateTime(new Date());
            projectCycleAccountMapper.updateByPrimaryKeySelective(projectCycleAccount);
            projectAccount.setCycleAmount(projectAccount.getCycleAmount().subtract(projectCycleDetail.getAmount()));
            projectAccount.setTotalAmount(projectAccount.getTotalAmount().subtract(projectCycleDetail.getAmount()));
        }

        projectAccountMapper.updateByIdAndVersion(projectAccount);
        return true;
    }

    @Override
    public boolean changeCycleAccount(String companyId, String projectId, ChargingType cycleEnum, BusinessType businessType,AccountType accountType,PayChannel payChannel, BigDecimal money, String buildingCode, String streamId, String logStream, String operator){
        logger.info("{}开始查询项目账户信息", logStream);
        Date time = new Date();
        ProjectAccount projectAccount = queryOrCreateProjectAccount(companyId, projectId,time,logStream);
        String projectAccountId = projectAccount.getId();
        logger.info("{}开始查询项目周期性账户",logStream);
        ProjectCycleAccount projectCycleAccount= queryOrCreateProjectCycleAccount(projectAccountId, time,  cycleEnum.getCode(),logStream);
        logger.info("{}开始创建项目周期性账户明细", logStream);
        String projectCycleAccountId = projectCycleAccount.getId();
        BigDecimal rate=rateUtils.getRateByCompanyIdAndProjectIdAndType(companyId,projectId, cycleEnum.getCode());
        createProjectCycleDetail(projectCycleAccountId, money, cycleEnum, businessType,accountType,payChannel, buildingCode, streamId, time, operator, rate);
        logger.info("{}项目周期性账户明细创建成功,开始汇总金额到项目周期性账户", logStream);
        projectCycleAccount.setAmount(projectCycleAccount.getAmount().add(money));
        projectAccount.setCycleAmount(projectAccount.getCycleAmount().add(money));
        int count = projectCycleAccountMapper.updateByPrimaryKeySelective(projectCycleAccount);
        if (count == 0) {
            logger.error("{}汇总金额到项目周期性账户失败!", logStream);
            throw new ECPBusinessException("汇总金额到周期性账户失败!");
        } else {
            logger.info("{}汇总金额到项目周期性账户成功！开始汇总到项目账户", logStream);
            projectAccount.setId(projectAccountId);
            projectAccount.setTotalAmount(projectAccount.getCycleAmount().add(projectAccount.getFineAmount())
                    .add(projectAccount.getLateAmount()).add(projectAccount.getPredepositAmount().add(projectAccount.getProductAmount())));
            int updateCount = projectAccountMapper.updateByIdAndVersion(projectAccount);
            if (updateCount == 0) {
                logger.error("{}汇总金额到项目账户失败!", logStream);
                try {
                    throw new VersionException("汇总到项目账户失败!");
                } catch (VersionException e) {
                    e.printStackTrace();
                }
                return false;
            } else {
                logger.info("{}汇总金额到项目账户成功!", logStream);
                return true;
            }
        }
    }

    @Override
    public boolean changeProductAccount(String companyId, String projectId, BigDecimal money, String orderId, String orderJson, int isAsset, String houseCodeNew, String logStream, String operator, BigDecimal rate){
        logger.info("{}开始查询项目账户信息", logStream);
        Date time = new Date();
        ProjectAccount projectAccount = queryOrCreateProjectAccount(companyId, projectId,time,logStream);
        logger.info("{}创建项目产品账户详情", logStream);
        createProjectProductDetail(projectAccount.getId(), money, orderId, orderJson, isAsset, houseCodeNew, rate, time, operator);
        logger.info("{}创建项目产品账户详情成功!", logStream);
        logger.info("{}开始向项目账户汇总", logStream);
        projectAccount.setProductAmount(projectAccount.getProductAmount().add(money));
        projectAccount.setTotalAmount(projectAccount.getCycleAmount().add(projectAccount.getFineAmount())
                .add(projectAccount.getLateAmount()).add(projectAccount.getPredepositAmount().add(projectAccount.getProductAmount())).subtract(projectAccount.getRefundAmount()));
        projectAccount.setUpdateTime(time);
        int updateCount = projectAccountMapper.updateByIdAndVersion(projectAccount);
        if (updateCount == 0) {
            logger.error("{}汇总产品账户金额到项目账户失败", logStream);
            try {
                throw new VersionException("汇总产品账户金额到项目账户失败");
            } catch (VersionException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            logger.info("{}汇总产品账户金额到项目账户成功", logStream);
            return true;
        }
    }

    @Override
    public boolean changeDelayAccount(String companyId, String projectId, BigDecimal money, ChargingType delayEnum, String houseCodeNew, String streamId, String logStream, String operator){
        logger.info("{}开始查询项目账户信息", logStream);
        Date time = new Date();
        ProjectAccount projectAccount = queryOrCreateProjectAccount(companyId, projectId,time,logStream);
        ProjectDelayAccount projectDelayAccount = queryOrCreateProjectDelayAccount(projectAccount.getId(), time,  delayEnum.getCode(),logStream);
        logger.info("{}开始创建项目滞纳金明细",logStream);
        createProjectDelayDetail(projectDelayAccount.getId(), money, time, operator,delayEnum, houseCodeNew, streamId);
        logger.info("{}项目滞纳金明细创建完成,汇总金额到项目滞纳金账户");
        projectDelayAccount.setAmount(projectDelayAccount.getAmount().add(money));
        int count = projectDelayAccountMapper.updateByPrimaryKeySelective(projectDelayAccount);
        if (count == 0) {
            logger.error("{}项目滞纳金账户汇总失败!", logStream);
            throw new ECPBusinessException("项目滞纳金账户汇总失败");
        } else {
            logger.info("{}项目滞纳金账户汇总成功,开始汇总到项目账户", logStream);
            projectAccount.setLateAmount((projectAccount.getLateAmount().add(money)));
            projectAccount.setTotalAmount(projectAccount.getCycleAmount().add(projectAccount.getFineAmount())
                    .add(projectAccount.getLateAmount()).add(projectAccount.getPredepositAmount().add(projectAccount.getProductAmount()).subtract(projectAccount.getRefundAmount())));
            projectAccount.setUpdateTime(time);
            int updateCount = projectAccountMapper.updateByIdAndVersion(projectAccount);
            if (updateCount == 0) {
                logger.error("{}汇总滞纳金账户金额到项目账户失败", logStream);
                try {
                    throw new VersionException("汇总滞纳金账户金额到项目账户失败");
                } catch (VersionException e) {
                    e.printStackTrace();
                }
                return false;
            } else {
                logger.info("{}汇总滞纳金账户金额到项目账户成功", logStream);
                return true;
            }
        }
    }

    @Override
    public boolean changePrestoreAccount(String companyId, String projectId, BusinessType businessType,AccountType accountType, ChargingType chargingType, BigDecimal money, String buildingCode, String streamId, String logStream, String operator, String orderId){
        logger.info("{}开始查询项目账户信息", logStream);
        Date time = new Date();
        ProjectAccount projectAccount = queryOrCreateProjectAccount(companyId, projectId,time,logStream);
        String projectAccountId=projectAccount.getId();
        ProjectPrestoreAccount  projectPrestoreAccount = queryOrCreateProjectPrestoreAccount(projectAccountId, time, accountType.getCode(),logStream);
        logger.info("{}开始创建项目预存账户明细", logStream);
        String projectPrestoreAccountId=projectPrestoreAccount.getId();
        if(businessType.equals(BusinessType.REFUND)){
            createRefundDetail(projectAccountId,money,1,orderId,operator,chargingType,streamId);
        }
        createProjectPrestoreDetail(projectPrestoreAccountId, money,businessType, chargingType, buildingCode, streamId, time, operator,orderId);
        logger.info("{}项目预存账户明细创建成功,开始汇总到项目预存账户", logStream);
        projectPrestoreAccount.setAmount(projectPrestoreAccount.getAmount().add(money));
        projectPrestoreAccount.setPrestoreType(accountType.getCode());
        int count = projectPrestoreAccountMapper.updateByPrimaryKeySelective(projectPrestoreAccount);
        if (count == 0) {
            logger.error("{}汇总到项目预存账户失败", logStream);
            throw new ECPBusinessException("汇总到项目预存账户失败");
        } else {
            logger.info("{}汇总到项目预存账户成功，开始汇总到项目账户", logStream);
            projectAccount.setId(projectAccountId);
            projectAccount.setUpdateTime(time);
            projectAccount.setProjectId(projectId);
            projectAccount.setCompanyId(companyId);
            if(businessType.equals(BusinessType.DEDUCTIBLE)||businessType.equals(BusinessType.REFUND)){
                projectAccount.setPredepositAmount(projectAccount.getPredepositAmount().subtract(money));
            }else {
                projectAccount.setPredepositAmount(projectAccount.getPredepositAmount().add(money));
            }
            projectAccount.setTotalAmount(projectAccount.getCycleAmount().add(projectAccount.getFineAmount())
                    .add(projectAccount.getLateAmount()).add(projectAccount.getPredepositAmount().add(projectAccount.getProductAmount())));
            int updateCount = projectAccountMapper.updateByIdAndVersion(projectAccount);
            if (updateCount == 0) {
                logger.error("{}汇总到项目账户失败!", logStream);
                try {
                    throw new VersionException("汇总到项目账户失败");
                } catch (VersionException e) {
                    e.printStackTrace();
                }
                return false;
            } else {
                logger.info("{}汇总到项目账户成功!", logStream);
                return true;
            }
        }
    }

    /*退费
     */
    @Override
    public boolean changeRefundAccount(String companyId, String projectId, BigDecimal money  ,
                                       ChargingType chargingType,AccountType accountType, String orderId, String houseCodeNew, String logStream,
                                       String operator,String businessOperaId){
        logger.info("{}开始查询项目账户信息", logStream);
        Date time = new Date();
        ProjectAccount projectAccount = queryOrCreateProjectAccount(companyId, projectId,time,logStream);
        ProjectPrestoreAccount prestoreAccount= projectPrestoreAccountMapper.selectByProjectIdAndType(projectId, accountType.getCode());
        createProjectPrestoreDetail(prestoreAccount.getId(),money,BusinessType.REFUND,chargingType,houseCodeNew,businessOperaId,time,operator,orderId);
        prestoreAccount.setAmount(prestoreAccount.getAmount().subtract(money));
        projectPrestoreAccountMapper.updateByPrimaryKey(prestoreAccount);
        createRefundDetail(projectAccount.getId(), money, accountType.getCode(), orderId, operator,chargingType,businessOperaId);
        logger.info("{}创建退费账户详情成功!", logStream);
        logger.info("{}开始向项目账户汇总", logStream);
        projectAccount.setRefundAmount(projectAccount.getRefundAmount().add(money));
        projectAccount.setPredepositAmount(projectAccount.getPredepositAmount().subtract(money));
        projectAccount.setTotalAmount(projectAccount.getCycleAmount().add(projectAccount.getFineAmount())
                .add(projectAccount.getLateAmount()).add(projectAccount.getPredepositAmount().add(projectAccount.getProductAmount())));
        projectAccount.setUpdateTime(time);
        int updateCount = projectAccountMapper.updateByIdAndVersion(projectAccount);
        if (updateCount == 0) {
            logger.error("{}汇总退费账户金额到项目账户失败", logStream);
            try {
                throw new VersionException("汇总退费账户金额到项目账户失败");
            } catch (VersionException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            logger.info("{}汇总退费账户金额到项目账户成功", logStream);
            return true;
        }
    }


    /*
     * 罚金
     */
    @Override
    public boolean changeFineAccount(String companyId, String projectId, BigDecimal money, String logStream, String orderId,String streamId, String operator){
        logger.info("{}开始查询项目账户信息", logStream);
        Date time = new Date();
        ProjectAccount projectAccount = queryOrCreateProjectAccount(companyId, projectId,time,logStream);
        createFineDetail(projectAccount.getId(), money, orderId,streamId, operator, time);
        logger.info("{}创建项目罚金明细成功,开始项目汇总罚金金额",logStream);
        projectAccount.setFineAmount(projectAccount.getFineAmount().add(money));
        projectAccount.setTotalAmount(projectAccount.getCycleAmount().add(projectAccount.getFineAmount())
                .add(projectAccount.getLateAmount()).add(projectAccount.getPredepositAmount().add(projectAccount.getProductAmount()).subtract(projectAccount.getRefundAmount())));
        int updateCount = projectAccountMapper.insertSelective(projectAccount);
        if (updateCount == 0) {
            logger.info("{}修改项目账户罚金总额失败", logStream);
            try {
                throw new VersionException("修改项目账户罚金总额失败");
            } catch (VersionException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            logger.info("{}修改项目账户罚金成功", logStream);
            return true;
        }
    }

    private void createFineDetail(String projectAccountId, BigDecimal money, String orderId,String streamId, String operator,
                                  Date time) {
        String id = UUID.randomUUID().toString();
        ProjectFineDetail projectFineDetail = new ProjectFineDetail();
        projectFineDetail.setId(id);
        projectFineDetail.setProjectAccountId(projectAccountId);
        projectFineDetail.setAmount(money);
        projectFineDetail.setCreateTime(time);
        projectFineDetail.setOrderId(orderId);
        projectFineDetail.setBusinessOperaDetailId(streamId);
        projectFineDetail.setCreateBy(operator);
        projectFineDetailMapper.insert(projectFineDetail);

    }

    private void createRefundDetail(String projectAccountId, BigDecimal money, int type,String orderId, String operator,ChargingType chargingType,String businessOperaId) {
        String detailId = UUID.randomUUID().toString();
        ProjectRefundDetail projectRefundDetail = new ProjectRefundDetail();
        projectRefundDetail.setId(detailId);
        projectRefundDetail.setProjectAccountId(projectAccountId);
        projectRefundDetail.setRefundType(type);
        if(chargingType!=null) {
            projectRefundDetail.setAccountType(chargingType.getCode());
        }
        projectRefundDetail.setAmount(money);
        projectRefundDetail.setCreateTime(new Date());
        projectRefundDetail.setCreateBy(operator);
        projectRefundDetail.setOrderId(orderId);
        projectRefundDetail.setBusinessOperaDetailId(businessOperaId);
        projectRefundDetailMapper.insert(projectRefundDetail);
    }

    private synchronized ProjectPrestoreAccount queryOrCreateProjectPrestoreAccount(String projectAccountId, Date time, int prestoreType,String logStream){
        ProjectPrestoreAccount temp = projectPrestoreAccountMapper.selectByProjectAccountIdAndType(projectAccountId, prestoreType);
        if (temp == null) {
            logger.info("{}未查询到项目预存账户，开始创建", logStream);
            temp = new ProjectPrestoreAccount();
            temp.setProjectAccountId(projectAccountId);
            temp.setAmount(new BigDecimal(0));
            temp.setCreateTime(time);
            temp.setUpdateTime(time);
            //  for (AccountType accountType : AccountType.values()) {
            //  int type=accountType.getCode();
            String projectPrestoreAccountId = UUID.randomUUID().toString();
            temp.setId(projectPrestoreAccountId);
            temp.setPrestoreType(prestoreType);
            //if (type == prestoreType) {
            //  temp = (ProjectPrestoreAccount) projectPrestoreAccount.clone();
            //}
            projectPrestoreAccountMapper.insertSelective(temp);

        } else {
            logger.info("{}查询到项目预存账户", logStream);
        }
        return temp;
    }


    private synchronized ProjectAccount queryOrCreateProjectAccount(String companyId, String projectId, Date time,String logStream){
        logger.info("{}获取到项目账户redis锁", logStream);
        ProjectAccount projectAccount = projectAccountMapper.selectByCompanyIdAndProjectId(companyId, projectId);
        if (projectAccount == null) {
            logger.info("{}未查询到项目账户,开始创建", logStream);
            projectAccount = new ProjectAccount();
            String projectAccountId = UUID.randomUUID().toString();
            projectAccount.setId(projectAccountId);
            projectAccount.setCompanyId(companyId);
            projectAccount.setCreateTime(time);
            projectAccount.setProjectId(projectId);
            projectAccount.setUpdateTime(time);
            projectAccount.setCycleAmount(new BigDecimal(0));
            projectAccount.setFineAmount(new BigDecimal(0));
            projectAccount.setLateAmount(new BigDecimal(0));
            projectAccount.setPredepositAmount(new BigDecimal(0));
            projectAccount.setProductAmount(new BigDecimal(0));
            projectAccount.setRefundAmount(new BigDecimal(0));
            projectAccount.setTotalAmount(new BigDecimal(0));
            projectAccount.setVersion(0);
            projectAccountMapper.insertSelective(projectAccount);
        } else {
            logger.info("{}查询到项目账户", logStream);
        }
        return projectAccount;
    }


    private void createProjectPrestoreDetail(String projectPrestoreAccountId, BigDecimal amount,BusinessType businessType,ChargingType chargingType, String buildingCode, String streamId, Date time, String operator,String orderId) {
        String detailId = UUID.randomUUID().toString();
        ProjectPrestoreDetail projectPrestoreDetail = new ProjectPrestoreDetail();
        projectPrestoreDetail.setPrestoreAccount(projectPrestoreAccountId);
        projectPrestoreDetail.setId(detailId);
        projectPrestoreDetail.setAmount(amount);
        projectPrestoreDetail.setHouseCodeNew(buildingCode);
        projectPrestoreDetail.setBusinessOperaDetailId(streamId);
        projectPrestoreDetail.setCreateTime(time);
        projectPrestoreDetail.setCreateBy(operator);
        if(businessType!=null){
            projectPrestoreDetail.setBusinessType(businessType.getCode());
        }
        if(chargingType!=null) {
            projectPrestoreDetail.setType(chargingType.getCode());
        }
        projectPrestoreDetail.setOrderId(orderId);
        projectPrestoreDetailMapper.insertSelective(projectPrestoreDetail);
    }

    private synchronized ProjectCycleAccount queryOrCreateProjectCycleAccount(String projectAccountId, Date time, int code,String logStream){
        ProjectCycleAccount temp = projectCycleAccountMapper.checkExists(projectAccountId, code);
        if (temp == null) {
            logger.info("{}未查询到项目周期性账户,开始创建", logStream);
            temp = new ProjectCycleAccount();
            temp.setAmount(new BigDecimal(0));
            temp.setCreateTime(time);
            temp.setProjectAccountId(projectAccountId);
            temp.setUpdateTime(time);
            //for (ChargingType chargingType : ChargingType.values()) {
            //int type=chargingType.getCode();
            String id = UUID.randomUUID().toString();
            temp.setId(id);
            temp.setAccountType(code);



            projectCycleAccountMapper.insert( temp);

        } else {
            logger.info("{}查询到项目周期性账户", logStream);
        }
        return temp;
    }

    private void createProjectCycleDetail(String id, BigDecimal money, ChargingType chargingType, BusinessType businessType,AccountType accountType,PayChannel payChannel, String buildingCode, String streamId, Date time, String operator, BigDecimal rate) {
        String detailId = UUID.randomUUID().toString();
        ProjectCycleDetail projectCycleDetail = new ProjectCycleDetail();
        projectCycleDetail.setId(detailId);
        projectCycleDetail.setCycleId(id);
        if(chargingType!=null) {
            projectCycleDetail.setChargeType(chargingType.getCode());
        }
        projectCycleDetail.setAmount(money);
        projectCycleDetail.setBusinessOperaDetail(streamId);
        if(businessType!=null) {
            projectCycleDetail.setBusinessType(businessType.getCode());
        }
        if(accountType!=null){
            projectCycleDetail.setAccountType(accountType.getCode());
        }
        projectCycleDetail.setCreateBy(operator);
        projectCycleDetail.setCreateTime(time);
        projectCycleDetail.setHouseCodeNew(buildingCode);
        projectCycleDetail.setRate(rate);
        if(payChannel!=null) {
            projectCycleDetail.setPayChannel(payChannel.getCode());
        }
        BigDecimal rateFee=rateUtils.calculateRate(rate,money);
        projectCycleDetail.setRateFee(rateFee);
        BigDecimal rateAfter = money.subtract(rateFee);
        projectCycleDetail.setRateAfter(rateAfter);
        projectCycleDetailMapper.insert(projectCycleDetail);
    }

    private void createProjectProductDetail(String id, BigDecimal money, String orderId, String orderJson, int isAsset, String buildingCode, BigDecimal rate, Date time, String operator) {
        String detailId = UUID.randomUUID().toString();
        ProjectProductDetail projectProductDetail = new ProjectProductDetail();
        projectProductDetail.setId(detailId);
        projectProductDetail.setProjectAccountId(id);
        projectProductDetail.setMoney(money);
        projectProductDetail.setOrderId(orderId);
        projectProductDetail.setOrderDetail(orderJson);
        projectProductDetail.setHouseCodeNew(buildingCode);
        projectProductDetail.setIsAsset(isAsset);
        projectProductDetail.setCreateBy(operator);
        projectProductDetail.setRate(rate);
        projectProductDetail.setCreateTime(time);
        BigDecimal rateFee=rateUtils.calculateRate(rate,money);
        BigDecimal rateAfter = money.subtract(rateFee);
        projectProductDetail.setRateFee(rateFee);
        projectProductDetail.setRateAfter(rateAfter);
        projectProductDetailMapper.insert(projectProductDetail);
    }

    private void createProjectDelayDetail(String id, BigDecimal money, Date time, String operator, ChargingType delayEnum, String houseCodeNew, String streamId) {
        String detailId = UUID.randomUUID().toString();
        ProjectDelayDetail projectDelayDetail = new ProjectDelayDetail();
        projectDelayDetail.setId(detailId);
        projectDelayDetail.setDelayAccountId(id);
        projectDelayDetail.setAmount(money);
        projectDelayDetail.setCreateBy(operator);
        projectDelayDetail.setAccountType(delayEnum.getCode());
        projectDelayDetail.setHouseCodeNew(houseCodeNew);
        projectDelayDetail.setBusinessOperaDetailId(streamId);
        projectDelayDetail.setCreateTime(time);
        projectDelayDetailMapper.insert(projectDelayDetail);
    }

    private synchronized ProjectDelayAccount queryOrCreateProjectDelayAccount(String projectAccountId, Date time, int code,String logStream){
        ProjectDelayAccount temp = projectDelayAccountMapper.selectByProjectAccountIdAndType(projectAccountId, code);
        if (temp == null) {
            logger.info("{}未查询到项目滞纳金账户，开始创建", logStream);
            temp = new ProjectDelayAccount();
            temp.setAmount(new BigDecimal(0));
            temp.setCreateTime(time);
            temp.setUpdateTime(time);
            temp.setProjectAccountId(projectAccountId);


            String delayAccountId = UUID.randomUUID().toString();
            temp.setAccountType(code);
            temp.setId(delayAccountId);
            projectDelayAccountMapper.insert(temp);

        } else {
            logger.info("{}查询到项目滞纳金账户", logStream);
        }
        return temp;
    }

    @Override
    public BaseDto<ProjectAccount, Object> getProjectAccountById(String companyId, String projectId) {
        if( CommonUtils.isEmpty( projectId )) {
            return new BaseDto<>(new MessageMap(MessageMap.INFOR_ERROR, "请求参数为空"));
        }
        List<ProjectAccount> resultList = projectAccountMapper.selectByProjectId(projectId);
        if( CommonUtils.isEmpty( resultList ) )  {return new BaseDto<>( new MessageMap(MessageMap.INFOR_ERROR,"查询结果为空") );}
        BaseDto<ProjectAccount, Object> baseDto = new BaseDto<>();
        baseDto.setLstDto(resultList);
        baseDto.setMessageMap( new MessageMap( MessageMap.INFOR_SUCCESS,"查询成功" ) );
        return baseDto ;
    }
}
