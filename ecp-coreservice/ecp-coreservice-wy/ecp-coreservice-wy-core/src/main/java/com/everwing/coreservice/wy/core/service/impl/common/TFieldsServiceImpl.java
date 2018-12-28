package com.everwing.coreservice.wy.core.service.impl.common;/**
 * Created by wust on 2017/9/6.
 */

import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.service.common.TFieldsService;
import com.everwing.coreservice.wy.dao.mapper.common.TFieldsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/6
 * @author wusongti@lii.com.cn
 */
@Service("tFieldsServiceImpl")
public class TFieldsServiceImpl implements TFieldsService {
    @Autowired
    private TFieldsMapper tFieldsMapper;

    @Override
    public MessageMap getFieldsCountByTableId(WyBusinessContext ctx, String tableId) {
        MessageMap mm = new MessageMap();
        int count = tFieldsMapper.getFieldsCountByTableId(tableId);
        mm.setObj(count);
        return mm;
    }
}
