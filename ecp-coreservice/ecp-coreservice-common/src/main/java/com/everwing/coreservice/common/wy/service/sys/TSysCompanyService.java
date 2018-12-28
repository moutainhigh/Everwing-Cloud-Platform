package com.everwing.coreservice.common.wy.service.sys;/**
 * Created by wust on 2017/6/13.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompany;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompanyList;
import com.everwing.coreservice.common.wy.entity.system.company.TSysCompanySearch;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/6/13
 * @author wusongti@lii.com.cn
 */
public interface TSysCompanyService {
    BaseDto listPage(String companyId, TSysCompanySearch condition);

    List<TSysCompanyList> findByCondtion(String companyId, TSysCompanySearch condition);

    MessageMap save(String companyId, TSysCompany entity);

    MessageMap delete(String companyId, TSysCompany entity);
}
