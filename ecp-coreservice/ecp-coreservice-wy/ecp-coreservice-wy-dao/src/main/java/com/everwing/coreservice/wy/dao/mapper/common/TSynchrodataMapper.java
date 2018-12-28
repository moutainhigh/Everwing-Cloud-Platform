package com.everwing.coreservice.wy.dao.mapper.common;/**
 * Created by wust on 2018/12/19.
 */

import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodata;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodataList;
import com.everwing.coreservice.common.wy.entity.common.synchrodata.TSynchrodataSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 *
 * Function:同步数据状态记录表
 * Reason:
 * Date:2018/12/19
 * @author wusongti@lii.com.cn
 */
public interface TSynchrodataMapper {
    List<TSynchrodataList> listPage(TSynchrodataSearch tSynchrodataSearch) throws DataAccessException;

    List<TSynchrodataList> findByCondition(TSynchrodataSearch tSynchrodataSearch) throws DataAccessException;

    List<Map> findDestinationTableDataBySQL(Map map);

    int batchInsert(List<TSynchrodata> tSynchrodatas) throws DataAccessException;

    int batchModify(List<TSynchrodata> tSynchrodatas) throws DataAccessException;
}
