package com.everwing.coreservice.wy.dao.mapper.sys;

import com.everwing.coreservice.common.wy.entity.system.operationLog.TSysOperationLog;
import com.everwing.coreservice.common.wy.entity.system.operationLog.TSysOperationLogList;
import com.everwing.coreservice.common.wy.entity.system.operationLog.TSysOperationLogSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * 操作日志
 */
public interface TSysOperationLogMapper {
    /**
     * 分页查询
     * @param condition
     * @return
     */
    List<TSysOperationLogList> listPage(TSysOperationLogSearch condition) throws DataAccessException;

    /**
     * 新增记录
     * @param entity
     * @return
     */
    int insert(TSysOperationLog entity) throws DataAccessException;
}
