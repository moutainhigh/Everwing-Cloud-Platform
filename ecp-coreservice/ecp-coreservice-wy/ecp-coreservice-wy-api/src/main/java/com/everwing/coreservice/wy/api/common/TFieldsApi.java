package com.everwing.coreservice.wy.api.common;/**
 * Created by wust on 2017/9/6.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.service.common.TFieldsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/6
 * @author wusongti@lii.com.cn
 */
@Component
public class TFieldsApi {
    @Autowired
    private TFieldsService tFieldsService;

    public RemoteModelResult<MessageMap> getFieldsCountByTableId(WyBusinessContext ctx, String tableId) {
        RemoteModelResult<MessageMap> result = new RemoteModelResult<>();
        result.setModel(tFieldsService.getFieldsCountByTableId(ctx,tableId));
        return result;
    }
}
