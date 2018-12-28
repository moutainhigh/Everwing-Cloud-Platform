package com.everwing.coreservice.platform.dao.mapper.extra;

import com.everwing.coreservice.common.platform.entity.generated.LyAuthorizationAccount;
import com.everwing.coreservice.platform.dao.mapper.extra.provider.AuthorizationProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AuthorizationExtraMapper {

    @Insert("insert into ly_authorization_account(id,authorized_account_id,authorizee_account_id,start_time,end_time,build_ids)" +
            " values(UUID(),#{authorizedAccountId},#{authorizeeAccountId},#{startTime},#{endTime},#{buildIds})")
    int insert(LyAuthorizationAccount lyAuthorizationAccount);

    @Select("SELECT  g.*,h.account_name authorizeeAccountName  FROM " +
            "(select distinct d.gating_code gatingCode,d.company_id companyId,e.authorizee_account_id authorizeeAccountCode,DATE_FORMAT(e.start_time,'%Y-%m-%d %H:%i:%S') startTime,DATE_FORMAT(e.end_time,'%Y-%m-%d %H:%i:%S') endTime,f.id buildingId,f.building_code buildingCode,f.building_full_name buildingName,f.project_id projectId,d.employ_project projectName,d.id gatingId,d.district gatingAddress " +
            "            from ly_authorization_account e left join account t on t.account_code=e.authorized_account_id " +
            "            LEFT JOIN tc_building f on e.build_ids=f.id left join building_gate c on e.build_ids=c.building_id left join gating d on c.gate_id=d.id where t.account_code=#{accountCode} GROUP BY d.gating_code) g " +
            "LEFT JOIN account h ON  g.authorizeeAccountCode=h.account_code;")
    List<Map<String,String>> selectAuthorizedInfo(String accountCode);

    @Select("SELECT  g.*,h.account_name authorizedAccountName  FROM " +
            "(select distinct d.gating_code gatingCode,d.company_id companyId,e.authorized_account_id authorizedAccountCode,DATE_FORMAT(e.start_time,'%Y-%m-%d %H:%i:%S') startTime,DATE_FORMAT(e.end_time,'%Y-%m-%d %H:%i:%S') endTime,f.id buildingId,f.building_code buildingCode,f.building_full_name buildingName,f.project_id projectId,d.employ_project projectName,d.id gatingId,d.district gatingAddress " +
            "            from ly_authorization_account e left join account t on t.account_code=e.authorizee_account_id " +
            "            LEFT JOIN tc_building f on e.build_ids=f.id left join building_gate c on e.build_ids=c.building_id left join gating d on c.gate_id=d.id where t.account_code=#{accountCode}) g " +
            "LEFT JOIN account h ON  g.authorizedAccountCode=h.account_code;")
    List<Map<String,String>> selectAuthorizeeInfo(String accountCode);

    @Select("select count(1) from account where account_code=#{authorizeeAccountCode}")
    int checkAuthorizeeExists(String authorizeeAccountCode);

    @Select("select count(1) from  ly_authorization_account where authorized_account_id=#{authorizedAccountId} and authorizee_account_id=#{authorizeeAccountId} and build_ids=#{buildIds}")
    int checkAuthExists(LyAuthorizationAccount lyAuthorizationAccount);

    @DeleteProvider(type = AuthorizationProvider.class,method = "batchDelete")
    void deleteAuthInfo(@Param("authorizedAccountCode") String authorizedAccountCode,@Param("authAccountCodeArray") String[] authAccountCodeArray,@Param("buildingId") String buildingId);
}
