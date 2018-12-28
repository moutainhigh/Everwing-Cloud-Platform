package com.everwing.coreservice.wy.dao.mapper.sys;


import com.everwing.coreservice.common.wy.entity.system.dataPrivilege.TSysDataPrivilege;
import com.everwing.coreservice.common.wy.entity.system.dataPrivilege.TSysDataPrivilegeList;
import com.everwing.coreservice.common.wy.entity.system.dataPrivilege.TSysDataPrivilegeSearch;

import java.util.List;


public interface TSysDataPrivilegeMapper {

    List<TSysDataPrivilegeList> listPage(TSysDataPrivilegeSearch condition);

    List<TSysDataPrivilegeList> findByCondition(TSysDataPrivilegeSearch condition);

    int insert(TSysDataPrivilege entity);

    int delete(TSysDataPrivilege entity);
}
