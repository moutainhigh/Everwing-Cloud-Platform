package com.everwing.coreservice.common.wy.service.sys;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUser;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;

import java.util.List;
import java.util.Map;

/**
 * 系统用户业务层接口
 */
public interface TSysUserService {

	BaseDto listPageUser(WyBusinessContext ctx, TSysUserSearch condition);

	List<TSysUserList> findByCondition(WyBusinessContext ctx, TSysUserSearch condition);

	MessageMap insert(WyBusinessContext ctx, TSysUser entity);


	MessageMap modify(WyBusinessContext ctx, TSysUser entity);


	MessageMap login(String loginName, String password);


	BaseDto listPageUserOther(String companyId, TSysUserSearch condition);


	BaseDto listPageUserInJg(String companyId, TSysUserSearch condition);

    List<Map<String,String>> findByCompanyId(String companyId);

    TSysUser findByPrimaryKey(String companyIdByCurrRequest, String userId);
}
