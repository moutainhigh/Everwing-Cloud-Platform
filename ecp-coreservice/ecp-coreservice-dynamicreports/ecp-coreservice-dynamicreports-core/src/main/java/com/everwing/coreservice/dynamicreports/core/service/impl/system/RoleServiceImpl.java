package com.everwing.coreservice.dynamicreports.core.service.impl.system;/**
 * Created by wust on 2018/1/31.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRole;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRoleQO;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsRoleVO;
import com.everwing.coreservice.common.dynamicreports.service.system.RoleService;
import com.everwing.coreservice.dynamicreports.dao.mapper.system.rights.TRightsRoleMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/31
 * @author wusongti@lii.com.cn
 */
@Service("roleServiceImpl")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private TRightsRoleMapper tRightsRoleMapper;

    @Override
    public BaseDto listPage(TRightsRoleQO tRightsRoleQO) {
        List<TRightsRoleVO> tRightsUserVOList = tRightsRoleMapper.listPage(tRightsRoleQO);
        BaseDto baseDto =  new BaseDto();
        baseDto.setLstDto(tRightsUserVOList);
        baseDto.setPage(tRightsRoleQO.getPage());
        return baseDto;
    }

    @Override
    public BaseDto findByCondition(TRightsRoleQO tRightsRoleQO) {
        List<TRightsRoleVO> tRightsUserVOList = tRightsRoleMapper.findByCondition(tRightsRoleQO);
        BaseDto baseDto =  new BaseDto();
        baseDto.setLstDto(tRightsUserVOList);
        return baseDto;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap save(TRightsRole tRightsRole) {
        MessageMap mm = new MessageMap();
        if(StringUtils.isNotBlank(tRightsRole.getRoleId())){
            tRightsRoleMapper.modify(tRightsRole);
        }else{
            tRightsRoleMapper.insert(tRightsRole);
        }
        return mm;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap batchSave(List<TRightsRole> tRightsRoles) {
        MessageMap mm = new MessageMap();
        List<TRightsRole> newList = new ArrayList<>(10);
        List<TRightsRole> oldList = new ArrayList<>(10);

        if(CollectionUtils.isNotEmpty(tRightsRoles)){
            for (TRightsRole tRightsRole : tRightsRoles) {
                if(StringUtils.isNotBlank(tRightsRole.getRoleId())){
                    oldList.add(tRightsRole);
                }else{
                    newList.add(tRightsRole);
                }
            }
        }

        if(CollectionUtils.isNotEmpty(oldList)){
            // TODO 批量更新
        }

        if(CollectionUtils.isNotEmpty(newList)){
            tRightsRoleMapper.batchInsert(newList);
        }
        return mm;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap delete(String id) {
        tRightsRoleMapper.delete(id);
        MessageMap mm = new MessageMap();
        return mm;
    }
}
