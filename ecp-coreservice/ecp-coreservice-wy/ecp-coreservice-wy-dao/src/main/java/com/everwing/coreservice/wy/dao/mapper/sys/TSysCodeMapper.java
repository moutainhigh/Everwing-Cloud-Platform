package com.everwing.coreservice.wy.dao.mapper.sys;/**
 * Created by wust on 2018/8/6.
 */

import com.everwing.coreservice.common.wy.entity.system.code.TSysCode;
import com.everwing.coreservice.common.wy.entity.system.code.TSysCodeSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/8/6
 * @author wusongti@lii.com.cn
 */
public interface TSysCodeMapper {
    List<TSysCode> findByCondition(TSysCodeSearch tSysCodeSearch) throws DataAccessException;;
    int insert(TSysCode tSysCode) throws DataAccessException;;
    int update(TSysCode tSysCode) throws DataAccessException;;
}
