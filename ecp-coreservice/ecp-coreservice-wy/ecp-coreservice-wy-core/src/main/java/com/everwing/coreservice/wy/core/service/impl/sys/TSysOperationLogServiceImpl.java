package com.everwing.coreservice.wy.core.service.impl.sys;/**
 * Created by wust on 2017/6/2.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.operationLog.TSysOperationLog;
import com.everwing.coreservice.common.wy.entity.system.operationLog.TSysOperationLogList;
import com.everwing.coreservice.common.wy.entity.system.operationLog.TSysOperationLogSearch;
import com.everwing.coreservice.common.wy.service.sys.TSysOperationLogService;
import com.everwing.coreservice.wy.dao.mapper.sys.TSysOperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/2
 * @author wusongti@lii.com.cn
 */
@Service("tSysOperationLogServiceImpl")
public class TSysOperationLogServiceImpl implements TSysOperationLogService {

    @Autowired
    private TSysOperationLogMapper tSysOperationLogMapper;

    @Override
    public BaseDto listPage(String companyId, TSysOperationLogSearch condition) {
        List<TSysOperationLogList> list = tSysOperationLogMapper.listPage(condition);

        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(condition.getPage());
        return baseDto ;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap insert(String companyId,TSysOperationLog entity) {
        MessageMap messageMap = new MessageMap();
        int count = tSysOperationLogMapper.insert(entity);
        if(count > 0){
            messageMap.setFlag(MessageMap.INFOR_SUCCESS);
            messageMap.setMessage("更新了"+count+"条记录");
        }else{
            messageMap.setFlag(MessageMap.INFOR_WARNING);
            messageMap.setMessage("更新了0条记录");
        }
        return messageMap;
    }
}
