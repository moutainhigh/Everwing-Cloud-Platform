package com.everwing.coreservice.platform.dao.mapper.extra;

import com.everwing.coreservice.platform.dao.mapper.extra.provider.GatingLinPhoneProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface GatingLinPhoneMapper {

    @Select("select distinct d.id gatingId,d.company_id companyId,d.gating_code gatingCode,d.district gatingAddress,d.employ_project projectName,d.project_id projectId from person_cust a left join person_building b on a.cust_id=b.cust_id" +
            " left join building_gate c on b.building_id=c.building_id left join gating d on c.gate_id=d.id where a.register_phone=#{mobile}")
    List<Map<String,Object>> selectListByMobile(String mobile);

    @Select("select distinct g.id gatingId,g.project_id projectId,g.company_id companyId,g.employ_project projectName,g.gating_code gatingCode,g.district gatingAddress from building_gate bg left join gating g on bg.gate_id=g.id where bg.building_id=#{buildingId}")
    List<Map<String,String>> selectGatingList(String buildingId);

    @Select("call pro_query_building_struct(#{gatingId})")
    @Options(statementType = StatementType.CALLABLE)
    List<Map<String,String>> selectBuildingStruct(@Param("gatingId") String gatingId);

    @UpdateProvider(type = GatingLinPhoneProvider.class,method = "updateStatus")
    int updateStatus(HashMap<String, String> hashMap);




}
