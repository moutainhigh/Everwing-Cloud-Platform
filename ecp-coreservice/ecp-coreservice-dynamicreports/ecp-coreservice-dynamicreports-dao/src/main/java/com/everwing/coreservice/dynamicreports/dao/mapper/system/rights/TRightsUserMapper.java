package com.everwing.coreservice.dynamicreports.dao.mapper.system.rights;/**
 * Created by wust on 2018/1/29.
 */

import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUser;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUserQO;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUserVO;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/29
 * @author wusongti@lii.com.cn
 */
public interface TRightsUserMapper {
    List<TRightsUserVO> listPage(TRightsUserQO tRightsUserQO);

    List<TRightsUserVO> findByCondition(TRightsUserQO tRightsUserQO);

    int insert(TRightsUser tRightsUser);

    int batchInsert(List<TRightsUser> tRightsUsers);

    int modify(TRightsUser tRightsUser);

    int delete(String userId);
}
