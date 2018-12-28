package com.everwing.coreservice.wy.dao.mapper.common;/**
 * Created by wust on 2017/8/30.
 */

import org.springframework.dao.DataAccessException;

/**
 *
 * Function:
 * Reason:
 * Date:2017/8/30
 * @author wusongti@lii.com.cn
 */
public interface TFieldsMapper {
    int getFieldsCountByTableId(String tableId) throws DataAccessException;
}
