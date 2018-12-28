package com.everwing.autotask.core.service.bill;

import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.stream.TBsAssetAccountStream;
import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import com.everwing.coreservice.common.wy.entity.configuration.owed.TBsOwedHistory;

import java.util.List;

/**
 * @author DELL shiny
 * @create 2018/8/10
 */
public interface CmacService{

    void batchInsertAccount(List<TBsAssetAccount> subList);

    void batchUpdateAccount(List<TBsAssetAccount> subList);

    void batchInsertAccountStream(List<TBsAssetAccountStream> subList);

    void batchInsertOwedHistory(List<TBsOwedHistory> subList);

    void batchUpdateOwedHistory(List<TBsOwedHistory> subList);

    void batchUpdateChargeBillHistory(List<TBsChargeBillHistory> subList);
}
