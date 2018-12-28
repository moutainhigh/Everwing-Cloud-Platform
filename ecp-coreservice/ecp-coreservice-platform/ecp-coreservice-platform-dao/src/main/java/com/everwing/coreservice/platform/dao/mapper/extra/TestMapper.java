package com.everwing.coreservice.platform.dao.mapper.extra;

import com.everwing.coreservice.common.admin.util.PageBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface TestMapper {
	@Select("select 'haha' from dual")
	String test();

	@Select("select u.* from app_pkg ap JOIN user u on 1=1 where 1=#{pageBean.params.test}")
	List<Map> testByPage(@Param("pageBean") PageBean pageBean);
}