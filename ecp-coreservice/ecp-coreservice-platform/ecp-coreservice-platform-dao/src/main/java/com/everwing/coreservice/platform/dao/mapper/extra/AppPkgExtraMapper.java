package com.everwing.coreservice.platform.dao.mapper.extra;

import com.everwing.coreservice.common.platform.entity.extra.AppPkgDto;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface AppPkgExtraMapper {
	
	@Update("UPDATE app_pkg SET `status` = 0 WHERE type = #{type}")
	int banAllPkg(int type);

    @Select("SELECT t.is_force isForce,t.version currVersion,t.md5,t.description versionDescription,t.pkg_file_id packageUrl FROM app_pkg t where t.type=#{type} and status=1")
    AppPkgDto selectByType(String type);

}
