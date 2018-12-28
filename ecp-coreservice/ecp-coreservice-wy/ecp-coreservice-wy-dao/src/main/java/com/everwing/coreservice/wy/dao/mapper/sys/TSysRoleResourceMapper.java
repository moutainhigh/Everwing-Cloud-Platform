package com.everwing.coreservice.wy.dao.mapper.sys;

import com.everwing.coreservice.common.wy.entity.system.relation.TSysRoleResource;
import org.springframework.dao.DataAccessException;

import java.util.List;



public interface TSysRoleResourceMapper {
	List<TSysRoleResource> findByCondition(TSysRoleResource tSysRoleResource) throws DataAccessException;

	/**
	 * 获取角色已被分配的菜单id
	 * @return
	 */
	List<TSysRoleResource> findMenuIdGroupByRoleId() throws DataAccessException;

    int batchInsert(List<TSysRoleResource> list) throws DataAccessException;
    
	int deleteRoleResourceByRoleId(String roleId) throws DataAccessException;
	
	int deleteRoleResourceByRoleIds(List<String> roleIds) throws DataAccessException;

	int deleteRoleResourceBySrcId(String srcType,String srcId) throws DataAccessException;

	int deleteRoleResourceByRoleIdAndSrcId(String roleId,String srcId) throws DataAccessException;
}
