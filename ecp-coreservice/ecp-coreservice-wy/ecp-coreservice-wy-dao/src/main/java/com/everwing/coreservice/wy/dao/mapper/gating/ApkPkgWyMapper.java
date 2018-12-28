package com.everwing.coreservice.wy.dao.mapper.gating;

import com.everwing.coreservice.common.platform.entity.extra.AppPkgDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ApkPkgWyMapper {

    @Select("SELECT t.version currVersion,t.md5,t.version_description versionDescription,t.pkg_file_id packageUrl FROM app_pkg t where t.type=#{type}")
    AppPkgDto selectByType(String type);

}
