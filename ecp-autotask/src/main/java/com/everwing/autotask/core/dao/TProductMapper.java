package com.everwing.autotask.core.dao;/**
 * Created by wust on 2017/8/30.
 */

import com.everwing.coreservice.common.wy.entity.product.*;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/30
 * @author wusongti@lii.com.cn
 */
public interface TProductMapper {

    List<TProductDetailList> findProductDetailByCondition(TProductDetailSearch tProductDetailSearch) throws DataAccessException;


    int modifyProductDetail(TProductDetailModify tProductModify) throws DataAccessException;

}
