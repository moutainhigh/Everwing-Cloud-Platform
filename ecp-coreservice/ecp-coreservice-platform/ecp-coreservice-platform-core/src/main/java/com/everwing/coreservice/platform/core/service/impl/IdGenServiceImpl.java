package com.everwing.coreservice.platform.core.service.impl;

import com.everwing.coreservice.common.platform.constant.Dict;
import com.everwing.coreservice.common.platform.entity.extra.IdGen;
import com.everwing.coreservice.common.platform.service.IdGenService;
import com.everwing.coreservice.platform.core.util.Resources;
import org.springframework.stereotype.Service;

/**
 * Created by shiny on 2017/5/31.
 */
@Service
public class IdGenServiceImpl extends Resources implements IdGenService {

	@Override
    public int queryMaxIdByType(int type){
        boolean checkResult=Dict.checkAccountType(type);
        if(!checkResult){
            return -1;
        }
        IdGen idGen=new IdGen();
        idGen.setType(type);
        idGenExtraMapper.getMaxId(idGen);
        return idGen.getId();
    }
    
    @Override
    public int getIncreasedId(int type){
    	idGenExtraMapper.increaseId(type);//ID自增
    	return idGenExtraMapper.getId(type);
    }
}
