package com.everwing.coreservice.platform.dao.mapper.extra.provider;

import com.everwing.coreservice.common.platform.entity.extra.Project;
import org.apache.ibatis.jdbc.SQL;

/**
 * project sql
 *
 * @author DELL shiny
 * @create 2018/4/3
 */
public class ProjectProvider {

    public String update(Project project){
        SQL sql=new SQL();
        sql.UPDATE("t_sys_project");
        if(project.getCode()!=null){
            sql.SET("code=#{code}");
        }
        if(project.getName()!=null){
            sql.SET("name=#{name}");
        }
        if(project.getDescription()!=null){
            sql.SET("description=#{description}");
        }
        if(project.getAddress()!=null){
            sql.SET("address=#{address}");
        }
        if(project.getStatus()!=null){
            sql.SET("status=#{status}");
        }
        if(project.getLeader()!=null){
            sql.SET("leader=#{leader}");
        }
        sql.SET("ground_parking_count=#{groundParkingCount}");
        sql.SET("underground_parking_count=#{undergroundParkingCount}");
        sql.SET("bill_status=#{billStatus}");
        if(project.getCompanyId()!=null){
            sql.SET("company_id=#{companyId}");
        }
        if(project.getZipCode()!=null){
            sql.SET("zip_code=#{zipCode}");
        }
        if(project.getModifyId()!=null){
            sql.SET("modify_id=#{modifyId}");
        }
        sql.SET("update_time=now()");
        sql.WHERE("project_id=#{projectId}");
        return  sql.toString();
    }
}
