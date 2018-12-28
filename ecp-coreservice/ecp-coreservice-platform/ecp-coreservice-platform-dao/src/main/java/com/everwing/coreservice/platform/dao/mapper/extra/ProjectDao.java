package com.everwing.coreservice.platform.dao.mapper.extra;

import com.everwing.coreservice.common.platform.entity.extra.Project;
import com.everwing.coreservice.platform.dao.mapper.extra.provider.ProjectProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

/**
 * @author shiny
 * Created by DELL on 2018/4/3.
 */
@Repository
public interface ProjectDao {

    @Insert("INSERT INTO t_sys_project(project_id, code, name, description, address, status, leader, civil_air_defence_area, basement_area, building_area, ground_parking_count, underground_parking_count, ` zip_code`, company_id, creater_id, creater_name, create_time)" +
            "    VALUES (#{projectId}, #{code}, #{name}, #{description}, #{address}, #{status}, #{leader}, #{civilAirDefenceArea}, #{basementArea}, #{buildingArea}, #{groundParkingCount}, #{undergroundParkingCount}, #{zipCode}, #{companyId}, #{createrId}, #{createrName}, now()")
    int insert(Project project);

    @UpdateProvider(type = ProjectProvider.class,method = "update")
    int update(Project project);

    @Update("update t_sys_project set status='disable' where project_id=#{projectId}")
    int delete(Project project);

    @Select("select count(1) from t_sys_project where project_id=#{projectId}")
    int checkExists(Project project);
}
