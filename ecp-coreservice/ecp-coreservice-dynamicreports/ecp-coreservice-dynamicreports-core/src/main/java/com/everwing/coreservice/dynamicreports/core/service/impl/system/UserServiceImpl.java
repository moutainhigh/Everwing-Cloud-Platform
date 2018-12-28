package com.everwing.coreservice.dynamicreports.core.service.impl.system;/**
 * Created by wust on 2018/1/31.
 */

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUser;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUserQO;
import com.everwing.coreservice.common.dynamicreports.entity.system.rights.TRightsUserVO;
import com.everwing.coreservice.common.dynamicreports.service.system.UserService;
import com.everwing.coreservice.dynamicreports.dao.mapper.system.rights.TRightsUserMapper;
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
@Service("userServiceImpl")
public class UserServiceImpl implements UserService{
    @Autowired
    private TRightsUserMapper tRightsUserMapper;

    @Override
    public BaseDto listPage(TRightsUserQO tRightsUserQO) {
        List<TRightsUserVO> tRightsUserVOList = tRightsUserMapper.listPage(tRightsUserQO);
        BaseDto baseDto =  new BaseDto();
        baseDto.setLstDto(tRightsUserVOList);
        baseDto.setPage(tRightsUserQO.getPage());
        return baseDto;
    }

    @Override
    public BaseDto findByCondition(TRightsUserQO tRightsUserQO) {
        List<TRightsUserVO> tRightsUserVOList = tRightsUserMapper.findByCondition(tRightsUserQO);
        BaseDto baseDto =  new BaseDto();
        baseDto.setLstDto(tRightsUserVOList);
        return baseDto;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap save(TRightsUser tRightsUser) {
        MessageMap mm = new MessageMap();
        if(StringUtils.isNotBlank(tRightsUser.getUserId())){
            tRightsUserMapper.modify(tRightsUser);
        }else{
            tRightsUserMapper.insert(tRightsUser);
        }
        return mm;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap batchSave(List<TRightsUser> tRightsUserList) {
        MessageMap mm = new MessageMap();
        List<TRightsUser> newList = new ArrayList<>(10);
        List<TRightsUser> oldList = new ArrayList<>(10);

        if(CollectionUtils.isNotEmpty(tRightsUserList)){
            for (TRightsUser tRightsUser : tRightsUserList) {
                if(StringUtils.isNotBlank(tRightsUser.getUserId())){
                    oldList.add(tRightsUser);
                }else{
                    newList.add(tRightsUser);
                }
            }
        }

        if(CollectionUtils.isNotEmpty(oldList)){
           // TODO 批量更新
        }

        if(CollectionUtils.isNotEmpty(newList)){
            tRightsUserMapper.batchInsert(newList);
        }
        return mm;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public MessageMap delete(String id) {
        tRightsUserMapper.delete(id);
        MessageMap mm = new MessageMap();
        return mm;
    }
}
