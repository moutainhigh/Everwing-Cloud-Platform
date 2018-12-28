package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.bill.TBsChargeBillTotal;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shiny
 * Created by DELL on 2018/5/30.
 */
@Repository
public interface TBsChargeBillTotalMapper {

    List<TBsChargeBillTotal> findCmacCanbilling(TBsChargeBillTotal paramTotal);

    List<TBsChargeBillTotal> findByObj(TBsChargeBillTotal paramTotal);

    void update(TBsChargeBillTotal total);

    Integer getAuditedCountByProjectIdAndTypes(@Param("projectId") String projectId, @Param("types") List<String> types);

    List<TBsChargeBillTotal> findCurrentBillTotal(TBsChargeBillTotal paramTotal);

    TBsChargeBillTotal selectById(String id);

    TBsChargeBillTotal findTbsTotalbyId(String totalProjectId);

    List<TBsChargeBillTotal> findCurrentBillTotalForShare(TBsChargeBillTotal paramTotal);

    void insertChargeBillTotal(TBsChargeBillTotal tBsChargeBillTotal);
}
