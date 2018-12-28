package com.everwing.coreservice.wy.fee.dao.mapper;

import com.everwing.coreservice.common.wy.entity.delivery.TJgStaffGrop;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/***
 * @describe  银账交割的员工组表接口
 * @author qhc
 * @ date 2017-08-31 
 */
public interface TJgStaffGropMapper {
    int addStaffGrop(TJgStaffGrop entity) throws DataAccessException;

    TJgStaffGrop getPidInfoByUserId(String userId,String projectId);

    int deleteById(String id) throws DataAccessException;

    String findCListStr(TJgStaffGrop entity);

    List<TJgStaffGrop> findByIds(List<String> ids);

    TJgStaffGrop findParentByObj(TJgStaffGrop entity);

    List<TJgStaffGrop> findByObj(TJgStaffGrop entity);

    List<TJgStaffGrop> findChildrenByObj(TJgStaffGrop grop);

    List<String> loadInfosToTree(String projectId);

    TJgStaffGrop findFirstNode();

    int batchDel(List<String> ids);

    TJgStaffGrop getMyselfInfoByUserId(String userId,String projectId);

    Map<String, String> getStaffGroupInfo(String userId,String projectId);

    List<TJgStaffGrop> getProjectListForRole(String userId);

    List<TJgStaffGrop> findCanPayRole(String createId);

    String notExistsInOtherProject(@Param("userId") String userId, @Param("projectId") String projectId);

    TJgStaffGrop getTJgStaffGropByUserId(String userId,String projectId);
}
