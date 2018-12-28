package com.everwing.coreservice.wy.fee.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.fee.constant.ClientType;
import com.everwing.coreservice.common.wy.fee.constant.PersonType;
import com.everwing.coreservice.common.wy.fee.dto.AcBusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderCycleDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderHistoryDto;
import com.everwing.coreservice.common.wy.fee.entity.AcOrder;
import com.everwing.coreservice.common.wy.fee.entity.ProjectAccount;
import com.everwing.coreservice.common.wy.fee.entity.ProjectPrestoreAccount;
import com.everwing.coreservice.common.wy.fee.entity.ProjectPrestoreDetail;
import com.everwing.coreservice.common.wy.fee.service.AcOrderService;
import com.everwing.coreservice.wy.fee.core.utils.OrderNoUtil;
import com.everwing.coreservice.wy.fee.dao.mapper.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产账户订单业务
 *
 * @author DELL shiny
 * @create 2018/5/29
 */
@Service
@Component
public class AcOrderServiceImpl implements AcOrderService {

    private static final Logger logger= LogManager.getLogger(AcOrderServiceImpl.class);

    @Autowired
    private AcOrderMapper acOrderMapper;

    @Autowired
    private AcCycleOrderDetailMapper cycleOrderDetailMapper;

    @Autowired
    private OrderNoUtil orderNoUtil;

    @Autowired
    private TSysProjectMapper tSysProjectMapper;

    @Autowired
    private ProjectAccountMapper projectAccountMapper;

    @Autowired
    private ProjectPrestoreAccountMapper projectPrestoreAccountMapper;

    @Autowired
    private ProjectPrestoreDetailMapper projectPrestoreDetailMapper;

    @Autowired
    private AcBusinessOperaDetailMapper acBusinessOperaDetailMapper;

    @Override
    public AcOrderDto queryByNo(String companyId, String orderNo) {
        return acOrderMapper.queryByNo(orderNo);
    }

    @Override
    public Integer updateOrderPayState(String companyId, String orderNo, int payState, int orderState,String payChannelTradeNo) {
        return acOrderMapper.updateOrderPayState(orderNo,payState,orderState,payChannelTradeNo);
    }

    @Override
    public Integer updateOrderRcorded(String companyId, String orderNo, int rcorded) {
        return acOrderMapper.updateOrderRcorded(orderNo,rcorded);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public String createCycleOrderInfo(String companyId, AcOrderDto orderDto, AcBusinessOperaDetailDto acBusinessOperaDetailDto) {
        String orderNo = orderNoUtil.createOrderNo(orderDto,acBusinessOperaDetailDto);
        orderDto.setOrderNo(orderNo);
        acOrderMapper.insertAcOrderDto(orderDto);
        String orderId = orderDto.getId();
        List<AcOrderCycleDetailDto> list = orderDto.getOrderCycleDetailList();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setOrderId(orderId);
        }
        acOrderMapper.insertCycleOrderInfoList(list);
        return orderNo;
    }

    @Override
    public List<AcOrderHistoryDto> queryCompleteOrder(String companyId, String houseCode, String month,String userId) {
        return acOrderMapper.queryOrderByStateAndDate(houseCode,month,userId);
    }

    @Override
    public List<AcOrderHistoryDto> queryOrderByYear(String companyId, String houseCode, String year) {
        return acOrderMapper.queryOrderByYear(houseCode,year);
    }

    @Override
    public BaseDto listPageOrderData(String companyId,AcOrder acOrder){
        BaseDto bto = new BaseDto();
        List<AcOrder> list =  acOrderMapper.listPageOrderData(acOrder);
        bto.setPage(acOrder.getPage());
        bto.setLstDto(list);
        return bto;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap returnOrConfirmGiveInfo(String companyId,String ids, AcOrder acOrder){

        if(CommonUtils.isEmpty(ids) || CommonUtils.isEmpty(acOrder.getIsRcorded())){
            return new MessageMap(MessageMap.INFOR_ERROR,"请求参数不能为空");
        }
        List<String> list= CommonUtils.str2List(ids, ",");
        Map<String, Object> paramMap=new HashMap<String, Object>();
        paramMap.put("status", acOrder.getIsRcorded());
        paramMap.put("list", list);
        acOrderMapper.returnOrConfirmGiveInfo(paramMap);
        if( 2 == acOrder.getIsRcorded()) {
            return new MessageMap(MessageMap.INFOR_SUCCESS,"退回操作成功");
        }else if(1 == acOrder.getIsRcorded()) {
            return new MessageMap(MessageMap.INFOR_SUCCESS,"确认操作成功");
        }
        return new MessageMap(MessageMap.INFOR_ERROR,"操作异常");
    }
    /**
     * 获取一个订单号给提供给别的系统
     */
    public String getOrderNo(String companyId,String projectId,int businessType,int personType,int clientType,int orderType ){
        if(CommonUtils.isEmpty(businessType) || CommonUtils.isEmpty(personType) || CommonUtils.isEmpty(clientType)||CommonUtils.isEmpty(orderType)){
            throw new ECPBusinessException("请求参数不能为空");
        }

        TSysProject tSysProject =  tSysProjectMapper.findByProjectId(projectId);
        if(CommonUtils.isEmpty(tSysProject.getCode())){
            throw new ECPBusinessException("请求参数为空");
        }
        AcBusinessOperaDetailDto acBusinessOperaDetailDto = new AcBusinessOperaDetailDto();
        acBusinessOperaDetailDto.setPersonType(personType);
        acBusinessOperaDetailDto.setClientType(clientType);
        acBusinessOperaDetailDto.setBusinessType(businessType);
        acBusinessOperaDetailDto.setProjectId(tSysProject.getCode());
        AcOrderDto orderDto =new AcOrderDto();
        orderDto.setOrderType(orderType);
        String orderNo = orderNoUtil.createOrderNo(orderDto,acBusinessOperaDetailDto);
        return orderNo;
    }

    /**
     * 插入一条订单
     */
    @Transactional(rollbackFor=Exception.class)
    public MessageMap insertOrder(String companyId,AcOrderDto orderDto,ProjectPrestoreDetail record){
        MessageMap mm = new MessageMap();
        if(CommonUtils.isEmpty(orderDto)){
            throw new ECPBusinessException("创建订单失败，参数不能为空");
        }

        acOrderMapper.insertAcOrderDto(orderDto);

        if( CommonUtils.isNotEmpty(record.getAmount()) && record.getAmount().compareTo(BigDecimal.ZERO) != 0){
            if(CommonUtils.isEmpty(record.getProjectId())){
                throw new ECPBusinessException("请求参数为空");
            }

            TSysProject tSysProject = tSysProjectMapper.findByProjectId(record.getProjectId());
            if(CommonUtils.isEmpty(tSysProject.getCode())){
                throw new ECPBusinessException("请求参数为空");
            }

            //查出项目账户
            ProjectAccount projectAccount =  projectAccountMapper.getProjectAccountById(tSysProject.getCode());

            if(CommonUtils.isEmpty(projectAccount)){
                throw new ECPBusinessException("项目账户不存在");
            }
            //查出预存账户
            ProjectPrestoreAccount projectPrestoreAccount = projectPrestoreAccountMapper.selectByProjectAccountIdAndType(projectAccount.getId(),3);

            if(CommonUtils.isEmpty(projectPrestoreAccount)){
                throw new ECPBusinessException("项目预存账户不存在");
            }
            //插入资金业务操作明细
            AcBusinessOperaDetailDto acBusinessOperaDetailDto = new AcBusinessOperaDetailDto();
            acBusinessOperaDetailDto.setId(CommonUtils.getUUID());
            acBusinessOperaDetailDto.setOperationId(record.getCreateBy());
            acBusinessOperaDetailDto.setOperationTime(new Date());
            acBusinessOperaDetailDto.setBusinessType(record.getBusinessType());
            acBusinessOperaDetailDto.setAmount(record.getAmount().doubleValue());
            acBusinessOperaDetailDto.setProjectId(projectAccount.getProjectId());
            acBusinessOperaDetailDto.setProjectName(projectAccount.getProjectName());
            acBusinessOperaDetailDto.setPersonType(PersonType.QT.getCode());
            acBusinessOperaDetailDto.setClientType(ClientType.PC.getCode());
            acBusinessOperaDetailMapper.insertOperaDetail( acBusinessOperaDetailDto);

            //插入预存账户明细
            record.setCreateTime(new Date());
            if(CommonUtils.isEmpty(record.getId())){
                record.setId(CommonUtils.getUUID());
            }
            record.setPrestoreAccount(projectAccount.getId());
            record.setType(record.getType());
            record.setBusinessType(record.getBusinessType());
            record.setBusinessOperaDetailId(acBusinessOperaDetailDto.getId());
            projectPrestoreDetailMapper.insert(record);

            //修改预存账户金额
            projectPrestoreAccount.setAmount( projectPrestoreAccount.getAmount().add(record.getAmount()));

            projectPrestoreAccountMapper.updateByPrimaryKey(projectPrestoreAccount);

            //修改项目账户金额
            projectAccount.setPredepositAmount(projectAccount.getPredepositAmount().add(record.getAmount()));

            projectAccountMapper.updateByPrimaryKey(projectAccount);
        }
        return mm;
    }
}
