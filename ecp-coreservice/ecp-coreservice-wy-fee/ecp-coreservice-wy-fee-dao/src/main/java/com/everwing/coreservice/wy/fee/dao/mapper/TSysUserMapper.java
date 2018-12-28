package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.entity.system.user.TSysUserList;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUserSearch;

import java.util.List;


public interface TSysUserMapper {


	// 获取指定条件的员工
	List<TSysUserList> findByCondition(TSysUserSearch condition);

}
