package com.everwing.coreservice.common.wy.service.common;/**
 * Created by wust on 2017/9/6.
 */

import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/6
 * @author wusongti@lii.com.cn
 */
public interface TFieldsService {
    MessageMap getFieldsCountByTableId(WyBusinessContext ctx, String tableId);
}
