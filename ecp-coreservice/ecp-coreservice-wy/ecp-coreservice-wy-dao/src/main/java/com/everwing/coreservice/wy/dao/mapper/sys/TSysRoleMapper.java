package com.everwing.coreservice.wy.dao.mapper.sys;

import com.everwing.coreservice.common.wy.entity.system.menu.TSysMenuTree;
import com.everwing.coreservice.common.wy.entity.system.relation.ResourceTreeMap;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRole;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRoleList;
import com.everwing.coreservice.common.wy.entity.system.role.TSysRoleSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 *  系统角色用mapper文件
 * @author wust
 *
 */
public interface TSysRoleMapper {

    List<TSysRoleList> listPageRole(TSysRoleSearch condition) throws DataAccessException;
    
    List<TSysRoleList> findByCondition(TSysRoleSearch entity) throws DataAccessException;
	
	int insert(TSysRole role) throws DataAccessException;
	
	int modify(TSysRole entity) throws DataAccessException;
	
	int deleteRoleByRoleIds(List<String> roleIds) throws DataAccessException;
	
	List<TSysMenuTree> findMenuTreeByRoleId(String toRoleId) throws DataAccessException;
	
	List<ResourceTreeMap> findResourceTreeByRoleId(String toRoleId) throws DataAccessException;
}
