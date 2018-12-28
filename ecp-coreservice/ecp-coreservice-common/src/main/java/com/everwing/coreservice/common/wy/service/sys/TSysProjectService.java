package com.everwing.coreservice.common.wy.service.sys;/**
 * Created by wust on 2017/6/20.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProject;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;

import java.util.Map;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/20
 * @author wusongti@lii.com.cn
 */
public interface TSysProjectService {
    BaseDto listPage(WyBusinessContext ctx, TSysProjectSearch condition);

    BaseDto findByCondition(WyBusinessContext ctx, TSysProjectSearch condition);

    Map getSummaryInformationByProjectId(WyBusinessContext ctx, String projectId);

    MessageMap save(String companyId, TSysProject entity);

    MessageMap delete(String companyId, TSysProject entity);

    MessageMap changeState(String companyId, TSysProject entity);

    BaseDto findAllProject(String companyId, TSysProjectSearch search);

	BaseDto listPageAndCountMeters(String companyId, TSysProjectSearch condition);
}
