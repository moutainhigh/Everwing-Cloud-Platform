package com.everwing.coreservice.platform.dao.mapper.extra;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BuildingExtraMapper {

    @Select("select distinct f.account_name houseAccount,c.company_id companyId,c.project_id projectId,c.id buildingId,c.`password`,c.building_code buildingCode,c.building_full_name buildingFullName,g.employ_project projectName,c.building_type buildingType from account_and_house a" +
            " left join tc_building c on a.building_code=c.building_code left join account f on c.building_code=f.account_code and f.type=1  join gating g on c.project_id=g.project_id and c.company_id = g.company_id where a.mobile=#{mobile} and a.status=1")
    List<Map<String,String>> selectByMobile(String mobile);

    @Update("UPDATE tc_building set password=#{password} WHERE building_code=#{buildingCode}")
    int updatePwdByBuildingCode(@Param("buildingCode") String buildingCode, @Param("password") String password);

    @Select("select c.building_code buildingCode,c.house_code roomNumber,c.finish_date completionTime,c.join_date cccupationTime,'' buildingAttributes,'' houseType,c.building_area buildingArea" +
            ",c.usable_area innerArea,c.project_id project_id,'' projectName,c.company_id companyId,g.company_name companyName from tc_building c  " +
            "  LEFT JOIN company g ON c.company_id=g.company_id WHERE c.id=#{buildingId}")
    Map<String,Object> selectById(String buildingId);

    @Select("select pc.`name` name,pc.register_phone phone FROM person_building pb LEFT JOIN person_cust pc on pb.cust_id=pc.cust_id where " +
            " pb.building_code = (SELECT building_code FROM tc_building WHERE id = #{buildingId})")
    List<Map<String,String>> queryOwnersByBId(String buildingId);
}
