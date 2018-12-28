package com.everwing.coreservice.wy.dao.mapper.sys;

import com.everwing.coreservice.common.wy.entity.system.user.TSysUser;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;


public interface TSysUserMapper {


	// 分页查询员工
	List<TSysUserList> listPageUser(TSysUserSearch conditon) throws DataAccessException;

	// 获取指定条件的员工
	List<TSysUserList> findByCondition(TSysUserSearch condition) throws DataAccessException;

	// 新增员工
	int insert(TSysUser entity) throws DataAccessException;

	// 修改员工
	int modify(TSysUser entity) throws DataAccessException;

	// 获取最大员工号
	String getMaxStaffNumber() throws DataAccessException;

	TSysUserList findUserByKey(String key) throws DataAccessException;

	List<TSysUserList> listPageOther(TSysUserSearch condition) throws DataAccessException;

	List listPageUserInJg(TSysUserSearch condition) throws DataAccessException;

    List<Map<String,String>> selectByCompanyId(String companyId) throws DataAccessException;

	TSysUser selectByPrimaryKey(String principalSysUserId) throws DataAccessException;
}
