package com.everwing.coreservice.wy.dao.mapper.product;/**
 * Created by wust on 2017/9/28.
 */

import com.everwing.coreservice.common.wy.entity.product.*;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/28
 * @author wusongti@lii.com.cn
 */
public interface TProductPaymentDetailMapper {
    List<TProductPaymentDetailList> findByCondition(TProductPaymentDetailSearch tProductPaymentDetailSearch) throws DataAccessException;
    int delete(String orderNo) throws DataAccessException;
    int batchInsert(List<TProductPaymentDetail> list) throws DataAccessException;
}
