package com.everwing.coreservice.wy.dao.mapper.product;/**
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

    List<TProductDetailList> listPageProductDetail(TProductDetailSearch tProductDetailSearch) throws DataAccessException;

    List<TProductDetailList> findProductDetailByCondition(TProductDetailSearch tProductDetailSearch) throws DataAccessException;

    /**
     * 分页查询产品销售记录
     * @param productSaleHistorySearch
     * @return
     */
    List<ProductSaleHistoryList> listPageProductSaleHistory(ProductSaleHistorySearch productSaleHistorySearch);

    int batchInsertProductDetail(TProductDetailInsert tProductInsert) throws DataAccessException;

    int modifyProductDetail(TProductDetailModify tProductModify) throws DataAccessException;

    int deleteProductDetailByProductCode(String projectId,String batchNo,String productCode) throws DataAccessException;
}
