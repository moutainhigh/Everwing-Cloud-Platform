package com.everwing.coreservice.common.wy.service.sys;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartment;
import com.everwing.coreservice.common.wy.entity.system.department.TSysDepartmentSearch;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
public interface TSysDepartmentService {
    BaseDto listPage(WyBusinessContext ctx, TSysDepartmentSearch condition);

    List<TSysDepartment> findByCondtion(WyBusinessContext ctx, TSysDepartmentSearch condition);

    MessageMap save(WyBusinessContext ctx, TSysDepartment entity);

    MessageMap delete(WyBusinessContext ctx, TSysDepartment entity);

    BaseDto findByCompanyIdAndProjectId(String companyId, String projectId);
}
