package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.assetaccount.TBsAssetAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shiny
 * Created by DELL on 2018/6/1.
 */
@Repository
public interface TBsAssetAccountMapper {

    TBsAssetAccount lookupByBuildCodeAndType(String buildingCode, Integer intV);

    void singleInsert(TBsAssetAccount commonAccount);

    void update(TBsAssetAccount commonAccount);

    void batchInsert(List<TBsAssetAccount> list);

    int batchUpdate(List<TBsAssetAccount> list);

    int addLateFee(@Param("accountId") String accountId, @Param("lateFee") double lateFee);
}
