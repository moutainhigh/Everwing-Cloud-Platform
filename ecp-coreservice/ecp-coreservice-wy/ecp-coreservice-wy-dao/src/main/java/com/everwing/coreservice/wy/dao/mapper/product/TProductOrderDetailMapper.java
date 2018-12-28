package com.everwing.coreservice.wy.dao.mapper.product;/**
 * Created by wust on 2017/9/26.
 */

import com.everwing.coreservice.common.wy.entity.product.*;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/26
 * @author wusongti@lii.com.cn
 */
public interface TProductOrderDetailMapper {
    List<TProductOrderDetailList> listPage(TProductOrderDetailSearch tProductOrderDetailSearch) throws DataAccessException;

    List<TProductOrderDetailList> findByCondition(TProductOrderDetailSearch tProductOrderDetailSearch) throws DataAccessException;

    List<TProductOrderDetailList> findSoldProductByProductCode(String productCode) throws DataAccessException;

    List<TProductOrderDetailList> findRecentProductOrderByProductCode(String productCode) throws DataAccessException;

    int batchInsert(List<TProductOrderDetail> tProductOrderDetails) throws DataAccessException;
}
