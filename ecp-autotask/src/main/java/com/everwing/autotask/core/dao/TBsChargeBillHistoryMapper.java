package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillHistory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shiny
 * Created by DELL on 2018/5/30.
 */
@Repository
public interface TBsChargeBillHistoryMapper {

    List<TBsChargeBillHistory> findAllByTotalId(String id);

    TBsChargeBillHistory findNextHistory(TBsChargeBillHistory history);

    Integer batchInsert(List<TBsChargeBillHistory> insertList);

    int batchUpdate(List<TBsChargeBillHistory> updateList);

    List<TBsChargeBillHistory> findCurrentBillByBuildingCode(String buildingCode);

    void updateZipCompleteByObj(TBsChargeBillHistory paramObj);

    int findNotZipByObj(TBsChargeBillHistory paramObj);

    Date findLastBillTime(String id);

    int updateChargeHistoryForShare(TBsChargeBillHistory entity);

    int updateLateFeeByProjectId(String projectId);

    List<TBsChargeBillHistory> findCurrentDetailBill(TBsChargeBillHistory paramBill);;
}
