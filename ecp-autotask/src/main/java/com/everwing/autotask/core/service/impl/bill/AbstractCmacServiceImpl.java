package com.everwing.autotask.core.service.impl.bill;

import com.everwing.autotask.core.dao.TBsAssetAccountMapper;
import com.everwing.autotask.core.dao.TBsAssetAccountStreamMapper;
import com.everwing.autotask.core.dao.TBsChargeBillHistoryMapper;
import com.everwing.autotask.core.dao.TBsOwedHistoryMapper;
import com.everwing.autotask.core.service.bill.CmacService;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DELL shiny
 * @create 2018/8/10
 */
@Service
public abstract class AbstractCmacServiceImpl implements CmacService{

    @Autowired
    protected TBsOwedHistoryMapper tBsOwedHistoryMapper;

    @Autowired
    protected TBsAssetAccountMapper tBsAssetAccountMapper;

    @Autowired
    protected TBsAssetAccountStreamMapper tBsAssetAccountStreamMapper;

    @Autowired
    protected TBsChargeBillHistoryMapper tBsChargeBillHistoryMapper;

    @Override
    public void batchInsertAccount(List<TBsAssetAccount> subList) {
        tBsAssetAccountMapper.batchInsert(subList);
    }

    @Override
    public void batchUpdateAccount(List<TBsAssetAccount> subList) {
        tBsAssetAccountMapper.batchUpdate(subList);
    }

    @Override
    public void batchInsertAccountStream(List<TBsAssetAccountStream> subList) {
        tBsAssetAccountStreamMapper.batchInsert(subList);
    }

    @Override
    public void batchInsertOwedHistory(List<TBsOwedHistory> subList) {
        tBsOwedHistoryMapper.batchInsert(subList);
    }

    @Override
    public void batchUpdateOwedHistory(List<TBsOwedHistory> subList) {
        tBsOwedHistoryMapper.batchUpdate(subList);
    }

    @Override
    public void batchUpdateChargeBillHistory(List<TBsChargeBillHistory> subList) {
        tBsChargeBillHistoryMapper.batchUpdate(subList);
    }

}
