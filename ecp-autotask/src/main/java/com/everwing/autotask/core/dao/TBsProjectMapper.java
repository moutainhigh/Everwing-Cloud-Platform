package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.configuration.project.TBsProject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2018/5/11.
 */
@Repository
public interface TBsProjectMapper {

//    @Select("SELECT *  FROM t_bs_project WHERE DATE_FORMAT(billing_time,'%Y-%m') = DATE_FORMAT(SYSDATE(),'%Y-%m')" +
//            " AND common_status = 0 " +
//            " AND `status` = 0")
//    
    List<TBsProject> findCanBillingCmacProject();

    List<TBsProject> findAllByObj(TBsProject project);

    List<TBsProject> findCanGenBillProject();

    TBsProject findById(String projectId);

    void updateGenBill(TBsProject tBsProject);

    List<TBsProject> findNeedGenProjects();

    int batchInsert(List<TBsProject> projects);

    TBsProject findByObj(TBsProject paramObj);

    List<TBsProject> findShareBillingProjects(Map<String,Object> paramObj);

    void flushItems(@Param("projectId") String projectId);

    List<TBsProject> findCanBillingProjects(Map<String, Object> paramMap);

    void update(TBsProject tBsProject);

    List<TBsProject> findCanNewBillingProjects(Map<String, Object> paramMap);
}
