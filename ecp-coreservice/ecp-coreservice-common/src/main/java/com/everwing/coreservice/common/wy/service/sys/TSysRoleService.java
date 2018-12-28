package com.everwing.coreservice.common.wy.service.sys;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.relation.PopMenuResourceTreeMap;
import com.everwing.coreservice.common.wy.entity.system.relation.TSysRoleResource;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRole;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRoleList;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRoleSearch;

import java.util.List;


public interface TSysRoleService {


	BaseDto listPageRole(WyBusinessContext ctx, TSysRoleSearch condition);


	MessageMap save(WyBusinessContext ctx, TSysRole entity);


	TSysRoleList findByGuid(String companyId, String guid);


	MessageMap delete(String companyId, String guids);


	MessageMap changeStatus(WyBusinessContext ctx, TSysRole entity);


	MessageMap initResources(String companyId);


	List<PopMenuResourceTreeMap> findResourceTreeByRoleId(String companyId, String toRoleId);


	MessageMap authorize(WyBusinessContext ctx, TSysRoleResource rr);
}
