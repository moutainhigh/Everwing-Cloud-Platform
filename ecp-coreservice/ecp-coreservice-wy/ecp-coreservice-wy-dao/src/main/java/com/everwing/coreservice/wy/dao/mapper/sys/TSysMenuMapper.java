package com.everwing.coreservice.wy.dao.mapper.sys;

import com.everwing.coreservice.common.wy.entity.system.menu.TSysMenu;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;


public interface TSysMenuMapper {
	int batchInsert(List<TSysMenu> menus) throws DataAccessException;
	
	int clearMenu() throws DataAccessException;

	/**
	 * 非超级管理员查询
	 * @param params
	 * @return
	 */
	List<TSysMenu> findMenu(Map<String,Object> params) throws DataAccessException;


	/**
	 * 超级管理员查询
	 * @return
	 */
	List<TSysMenu> findAllMenus4SystemAdmin() throws DataAccessException;
}
