package com.everwing.coreservice.wy.dao.mapper.sys;

import com.everwing.coreservice.common.wy.entity.system.resource.TSysResource;
import com.everwing.coreservice.common.wy.entity.system.resource.TSysResourceSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface TSysResourceMapper {
	/**
	 * 增加资源
	 * @param entitys
	 * @return
	 */
	int batchInsert(List<TSysResource> entitys) throws DataAccessException;

	/**
	 * 清空资源集合
	 * @return
	 */
	int clearResource() throws DataAccessException;


	List<TSysResource> findByCondition(TSysResourceSearch tSysResourceSearch) throws DataAccessException;


	/**
	 * 超级管理员：获取非白名单资源权限
	 * @return
	 */
	List<TSysResource> findAllResources4SystemAdmin() throws DataAccessException;


	/**
	 * 超级管理员：获取白名单资源权限
	 * @return
	 */
	List<TSysResource> findAllAnonResources4SystemAdmin() throws DataAccessException;


	/**
	 * 非超级管理员：获取非白名单资源权限
	 * @param params
	 * @return
	 */
	List<TSysResource> findResources(Map<String,Object> params) throws DataAccessException;


	/**
	 * 非超级管理员：获取白名单资源权限
	 * @param params
	 * @return
	 */
	List<TSysResource> findAnonResources(Map<String,Object> params) throws DataAccessException;

	/**
	 * 根据菜单获取其下面的资源集合
	 * @param menuId
	 * @return
	 */
	List<TSysResource> findAnonResourcesByMenuId(String menuId) throws DataAccessException;

	/**
	 * 获取公共白名单集合
	 * @return
	 */
	List<TSysResource> findCommonAnonResources() throws DataAccessException;
}
