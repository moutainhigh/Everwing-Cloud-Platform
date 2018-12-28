package com.everwing.coreservice.wy.dao.mapper.product;/**
 * Created by wust on 2017/11/14.
 */

import com.everwing.coreservice.common.wy.entity.product.*;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/11/14
 * @author wusongti@lii.com.cn
 */
public interface TDepositMapper {
    List<TDepositList> listPage(TDepositSearch tDepositSearch);

    List<TDepositList> findByCondition(TDepositSearch tDepositSearch);

    int batchInsert(List<TDeposit> tDepositList);

    int modify(TDeposit tDeposit);

    List<TDepositDetailList> findTDepositDetailByCondition(TDepositDetailSearch tDepositDetailSearch);

    List<TDepositDetailList> findDepositDetailByDepositId(String depositId);

    int insertTDepositDetail(TDepositDetail tDepositDetail);
}
