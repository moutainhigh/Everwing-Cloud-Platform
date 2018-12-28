package com.everwing.coreservice.platform.dao.mapper.extra;

import com.everwing.coreservice.common.platform.entity.extra.GatingLog;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * Created by DELL on 2017/7/20.
 */
@Repository
public interface GatingLogExtraMapper {

    @Insert("insert into mkj_log(id,company_id,project_id,toBuilding_code,form_code,gating_code,type,gating_account,create_time,frombuilding_code) " +
            " values(UUID(),#{companyId},#{projectId},#{toBuildingCode},#{formCode},#{gatingCode},#{type},#{gatingAccount},#{createTime},#{fromBuildingCode})")
    int insertLog(GatingLog gatingLog);
}
;