package com.everwing.autotask.core.dao;/**
 * Created by wust on 2017/9/26.
 */

import com.everwing.coreservice.common.wy.entity.product.TProductOrder;
import com.everwing.coreservice.common.wy.entity.product.TProductOrderList;
import com.everwing.coreservice.common.wy.entity.product.TProductOrderSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/26
 * @author wusongti@lii.com.cn
 */
public interface TProductOrderMapper {

    List<TProductOrderList> listPage(TProductOrderSearch tProductOrderSearch) throws DataAccessException;

    List<TProductOrderList> findByCondition(TProductOrderSearch tProductOrderSearch) throws DataAccessException;

    int batchInsert(List<TProductOrder> list) throws DataAccessException;

    int modify(TProductOrder tProductOrder) throws DataAccessException;
     
    List<Map<String, Object>> getProductInfos(Map<String, String> paramMap) throws DataAccessException;

    /**
     * 产品收款报表
     * @param tProductOrderSearch
     * @return
     */
    List<Map> productCollectingReports(TProductOrderSearch tProductOrderSearch) throws DataAccessException;

    /**
     * 产品收款报表（流水）
     * @param tProductOrderSearch
     * @return
     */
    List<Map> productCollectingSerialNumberReports(TProductOrderSearch tProductOrderSearch) throws DataAccessException;

    /**
     * 产品收款方式汇总报表
     * @param tProductOrderSearch
     * @return
     */
    List<Map> productPayTypeReports(TProductOrderSearch tProductOrderSearch) throws DataAccessException;
}
