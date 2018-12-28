package com.everwing.coreservice.dynamicreports.dao.mapper.system.datasource;/**
 * Created by wust on 2018/1/29.
 */

import com.everwing.coreservice.common.dynamicreports.entity.system.datasource.TSysDataSource;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/29
 * @author wusongti@lii.com.cn
 */
public interface TSysDataSourceMapper {
    List<TSysDataSource> findByCondition(TSysDataSource tSysDataSource);

    int insert(TSysDataSource tSysDataSource);

    int batchInsert(List<TSysDataSource> tSysDataSources);

    int delete(String id);

    int batchDelete(List<String> ids);

    int deleteByUserId(String userId);

    int deleteByLookupId(String lookupId);
}
