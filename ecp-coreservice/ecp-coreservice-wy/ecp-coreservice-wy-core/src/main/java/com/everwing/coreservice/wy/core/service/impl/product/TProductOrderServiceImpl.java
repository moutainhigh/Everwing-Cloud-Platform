package com.everwing.coreservice.wy.core.service.impl.product;/**
 * Created by wust on 2017/9/28.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.utils.cache.DataDictionaryUtil;
import com.everwing.coreservice.common.wy.common.enums.LookupItemEnum;
import com.everwing.coreservice.common.wy.entity.product.*;
import com.everwing.coreservice.common.wy.entity.system.lookup.TSysLookupSelectSearch;
import com.everwing.coreservice.common.wy.entity.system.lookup.TSysLookupSelectView;
import com.everwing.coreservice.common.wy.service.delivery.TJgAccountReceivableService;
import com.everwing.coreservice.common.wy.service.product.TProductOrderService;
import com.everwing.coreservice.wy.dao.mapper.delivery.TJgAccountReceivableMapper;
import com.everwing.coreservice.wy.dao.mapper.product.TDepositMapper;
import com.everwing.coreservice.wy.dao.mapper.product.TProductOrderDetailMapper;
import com.everwing.coreservice.wy.dao.mapper.product.TProductOrderMapper;
import com.everwing.coreservice.wy.dao.mapper.product.TProductPaymentDetailMapper;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysLookupMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/28
 * @author wusongti@lii.com.cn
 */
@Service("tProductOrderServiceImpl")
public class TProductOrderServiceImpl implements TProductOrderService {
    @Autowired
    private TProductOrderMapper tProductOrderMapper;

    @Autowired
    private TProductOrderDetailMapper tProductOrderDetailMapper;

    @Autowired
    private TJgAccountReceivableService tJgAccountReceivableService;

    @Autowired
    private TDepositMapper tDepositMapper;

    @Autowired
    private TJgAccountReceivableMapper tJgAccountReceivableMapper;

    @Autowired
    private TProductPaymentDetailMapper tProductPaymentDetailMapper;

    @Autowired
    private TSysLookupMapper tSysLookupMapper;


    @Override
    public BaseDto listPage(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch) {
        BaseDto baseDto = new BaseDto<>();
        List<TProductOrderList> tProductOrderLists = tProductOrderMapper.listPage(tProductOrderSearch);
        if(CollectionUtils.isNotEmpty(tProductOrderLists)){
            for (TProductOrderList tProductOrderList : tProductOrderLists) {
                tProductOrderList.setStatusName(
                        DataDictionaryUtil.getLookupItemNameByParentCodeAndCode(
                                ctx.getCompanyId(),
                                "productOrderState",
                                tProductOrderList.getStatus()
                        )
                );
            }
        }
        baseDto.setLstDto(tProductOrderLists);
        baseDto.setPage(tProductOrderSearch.getPage());
        return baseDto ;
    }

    @Override
    public List<TProductOrderList> findByCondition(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch) {
        return tProductOrderMapper.findByCondition(tProductOrderSearch);
    }

    @Override
    public List<TProductOrderDetailList> findTProductOrderDetailByCondition(WyBusinessContext ctx, TProductOrderDetailSearch tProductOrderDetailSearch){
        return tProductOrderDetailMapper.findByCondition(tProductOrderDetailSearch);
    }

    @Override
    public List<TProductOrderDetailList> findSoldProductByProductCode(WyBusinessContext ctx, String productCode) {
        return tProductOrderDetailMapper.findSoldProductByProductCode(productCode);
    }

    @Override
    public List<TProductOrderDetailList> findRecentProductOrderByProductCode(WyBusinessContext ctx, String productCode) {
        return tProductOrderDetailMapper.findRecentProductOrderByProductCode(productCode);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap createOrderInfo(WyBusinessContext ctx, TProductOrder tProductOrder, List<TProductOrderDetail> tProductOrderDetails){
        MessageMap mm = new MessageMap();
        if(tProductOrder != null){
            List<TProductOrder> tProductOrders = new ArrayList<>(1);
            tProductOrders.add(tProductOrder);

            // 创建订单
            if(CollectionUtils.isNotEmpty(tProductOrders)){
                tProductOrderMapper.batchInsert(tProductOrders);
            }

            if(CollectionUtils.isNotEmpty(tProductOrderDetails)){
                tProductOrderDetailMapper.batchInsert(tProductOrderDetails);
            }
        }
        return mm;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap disableProductOrder(WyBusinessContext ctx, TProductOrder tProductOrder) {
        MessageMap mm = new MessageMap();
        tProductOrderMapper.modify(tProductOrder);
        return mm;
    }




    @Override
    public List<Map> productCollectingReports(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch) {
        return tProductOrderMapper.productCollectingReports(tProductOrderSearch);
    }

    @Override
    public List<Map> productCollectingSerialNumberReports(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch) {
        List<Map> list = tProductOrderMapper.productCollectingSerialNumberReports(tProductOrderSearch);
        if(CollectionUtils.isNotEmpty(list)){
            Map<String,String> groupLookupItemByCode = groupLookupItemByCode(ctx);
            for (Map map : list) {
                StringBuffer sb = new StringBuffer();
                String payTypeStr = map.get("pay_type").toString();
                String[] payTypes = payTypeStr.split(",");
                for (String payType : payTypes) {
                    String payTypeName = groupLookupItemByCode.get(payType);
                    if(sb != null && StringUtils.isNotBlank(sb)){
                        sb.append(",").append(payTypeName);
                    }else{
                        sb.append(payTypeName);
                    }
                }
                map.put("pay_type_name",sb.toString());
            }
        }
        return list;
    }

    @Override
    public List<Map> productPayTypeReports(WyBusinessContext ctx, TProductOrderSearch tProductOrderSearch) {
        return tProductOrderMapper.productPayTypeReports(tProductOrderSearch);
    }

   

    /**
     * 找出支付类型
     * @return
     */
    private Map<String,String> groupLookupItemByCode(WyBusinessContext ctx){
        Map<String,String> groupLookupItemByCode = new HashMap<>(20);
        TSysLookupSelectSearch tSysLookupSelectSearch = new TSysLookupSelectSearch();
        tSysLookupSelectSearch.setParentCode("payType");
        tSysLookupSelectSearch.setLan(ctx.getLan());
        List<TSysLookupSelectView> tSysLookupSelectViews = tSysLookupMapper.findLookupItem(tSysLookupSelectSearch);
        if(CollectionUtils.isNotEmpty(tSysLookupSelectViews)){
            for (TSysLookupSelectView tSysLookupSelectView : tSysLookupSelectViews) {
                groupLookupItemByCode.put(tSysLookupSelectView.getCode(),tSysLookupSelectView.getName());
            }
        }
        return groupLookupItemByCode;
    }


    @Override
    public MessageMap rollbackProductOrder(WyBusinessContext ctx, String orderNo) {
        MessageMap mm = new MessageMap();
        TProductOrderSearch tProductOrderSearch = new TProductOrderSearch();
        tProductOrderSearch.setBatchNo(orderNo);
        List<TProductOrderList> tProductOrderLists = this.tProductOrderMapper.findByCondition(tProductOrderSearch);
        if(CollectionUtils.isNotEmpty(tProductOrderLists)){
            TProductOrderList tProductOrderList = tProductOrderLists.get(0);
            tProductOrderList.setStatus(LookupItemEnum.productOrderState_draft.getStringValue());
            tProductOrderList.setDiscountPrice(new BigDecimal(0));
            tProductOrderList.setModifyId(ctx.getUserId());
            tProductOrderList.setModifyName(ctx.getStaffName());
            tProductOrderMapper.modify(tProductOrderList);
        }
        this.tProductPaymentDetailMapper.delete(orderNo);
        return mm;
    }
}
