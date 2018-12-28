package com.everwing.coreservice.wy.core.service.impl.product;/**
 * Created by wust on 2017/12/8.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.entity.delivery.TJgAccountReceivable;
import com.everwing.coreservice.common.wy.entity.product.*;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.common.wy.service.delivery.TJgAccountReceivableService;
import com.everwing.coreservice.common.wy.service.product.TDepositService;
import com.everwing.coreservice.wy.dao.mapper.product.TDepositMapper;
import com.everwing.coreservice.wy.dao.mapper.product.TProductPaymentDetailMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysProjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/12/8
 * @author wusongti@lii.com.cn
 */
@Service("tDepositServiceImpl")
public class TDepositServiceImpl implements TDepositService {
    @Autowired
    private TDepositMapper tDepositMapper;

    @Autowired
    private TJgAccountReceivableService tJgAccountReceivableService;

    @Autowired
    private TSysProjectMapper tSysProjectMapper;

    @Autowired
    private TProductPaymentDetailMapper tProductPaymentDetailMapper;

    @Override
    public BaseDto listPage(WyBusinessContext ctx, TDepositSearch tDepositSearch) {
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(tDepositMapper.listPage(tDepositSearch));
        baseDto.setPage(tDepositSearch.getPage());
        return baseDto ;
    }

    @Override
    public List<TDepositList> findByCondition(WyBusinessContext ctx, TDepositSearch tDepositSearch) {
        return tDepositMapper.findByCondition(tDepositSearch);
    }

    @Override
    public List<TDepositDetailList> findTDepositDetailByCondition(WyBusinessContext ctx, TDepositDetailSearch tDepositDetailSearch) {
        return tDepositMapper.findTDepositDetailByCondition(tDepositDetailSearch);
    }

    @Override
    public List<TDepositDetailList> findDepositDetailByDepositId(WyBusinessContext ctx, String depositId) {
        return tDepositMapper.findDepositDetailByDepositId(depositId);
    }

    /**
     * 退押金
     * @param ctx
     * @param tDepositDetail
     * @param productPaymentDetails
     * @return
     */
    @Override
    public MessageMap rollBackDeposit(WyBusinessContext ctx, TDepositDetail tDepositDetail,List<TProductPaymentDetail> productPaymentDetails) {
        MessageMap mm = new MessageMap();
        tDepositMapper.insertTDepositDetail(tDepositDetail);

        TDepositSearch tDepositSearch = new TDepositSearch();
        tDepositSearch.setId(tDepositDetail.getDepositId());
        List<TDepositList> tDepositLists = tDepositMapper.findByCondition(tDepositSearch);
        if(CollectionUtils.isNotEmpty(tDepositLists)){
            TDepositList tDepositList = tDepositLists.get(0);

            if(tDepositList.getTotalAmount() <= (tDepositList.getReturnedPrice() + tDepositList.getDeductAmount())){
                TDeposit tDeposit = new TDeposit();
                tDeposit.setId(tDepositDetail.getDepositId());
                tDeposit.setStatus(LookupItemEnum.depositState_done.getStringValue());
                tDeposit.setModifyId(ctx.getUserId());
                tDeposit.setModifyName(ctx.getStaffName());
                tDepositMapper.modify(tDeposit);
            }else{
                // 更新操作人和操作时间信息
                TDeposit tDeposit = new TDeposit();
                tDeposit.setId(tDepositDetail.getDepositId());
                tDeposit.setStatus(tDepositList.getStatus());
                tDeposit.setModifyId(ctx.getUserId());
                tDeposit.setModifyName(ctx.getStaffName());
                tDepositMapper.modify(tDeposit);
            }


            String projectCode = "";
            TSysProjectSearch tSysProjectSearch = new TSysProjectSearch();
            tSysProjectSearch.setProjectId(tDepositList.getProjectId());
            List<TSysProjectList> tSysProjectLists = tSysProjectMapper.findByCondition(tSysProjectSearch);
            if(CollectionUtils.isNotEmpty(tSysProjectLists)){
                TSysProjectList tSysProjectList = tSysProjectLists.get(0);
                projectCode = tSysProjectList.getCode();
            }
            // ##################银帐交割 S
            TJgAccountReceivable tJgAccountReceivableProduct = new TJgAccountReceivable();
            tJgAccountReceivableProduct.setProjectId(projectCode);
            tJgAccountReceivableProduct.setPayedType("1");
            tJgAccountReceivableProduct.setBusinessType(3);// 退押金
            tJgAccountReceivableProduct.setPayerName("");
            tJgAccountReceivableProduct.setOprId(ctx.getUserId());
            tJgAccountReceivableProduct.setOprName(ctx.getStaffName());
            tJgAccountReceivableProduct.setStatus(2);
            tJgAccountReceivableProduct.setTradNo(ctx.getStaffNumber());
            tJgAccountReceivableProduct.setRelationId(tDepositDetail.getOrderNumber());
            for (TProductPaymentDetail productPaymentDetail : productPaymentDetails) {
                if(LookupItemEnum.productOrderPayType_cash.getStringValue().equalsIgnoreCase(productPaymentDetail.getPayType())){
                    tJgAccountReceivableProduct.setPayCash(productPaymentDetail.getPrice().toString());
                }else if(LookupItemEnum.productOrderPayType_charge.getStringValue().equalsIgnoreCase(productPaymentDetail.getPayType())){
                    tJgAccountReceivableProduct.setPayUnion(productPaymentDetail.getPrice().toString());
                }else if(LookupItemEnum.productOrderPayType_alipay.getStringValue().equalsIgnoreCase(productPaymentDetail.getPayType())){
                    tJgAccountReceivableProduct.setAlipay(productPaymentDetail.getPrice().toString());
                }else if(LookupItemEnum.productOrderPayType_weixinpay.getStringValue().equalsIgnoreCase(productPaymentDetail.getPayType())){
                    tJgAccountReceivableProduct.setPayWx(productPaymentDetail.getPrice().toString());
                }else if(LookupItemEnum.productOrderPayType_bank.getStringValue().equalsIgnoreCase(productPaymentDetail.getPayType())){
                    tJgAccountReceivableProduct.setBankReceipts(productPaymentDetail.getPrice().toString());
                }
            }
            tJgAccountReceivableService.addAccountReceivable(ctx.getCompanyId(),tJgAccountReceivableProduct);
            // ##################银帐交割 E



            // 创建支付记录
            tProductPaymentDetailMapper.batchInsert(productPaymentDetails);
        }
        return mm;
    }
}
