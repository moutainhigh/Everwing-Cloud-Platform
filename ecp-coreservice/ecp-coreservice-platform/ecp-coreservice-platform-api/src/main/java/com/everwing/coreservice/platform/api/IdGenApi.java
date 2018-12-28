package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.extra.IdGen;
import com.everwing.coreservice.platform.api.util.ServiceResources;
import org.springframework.stereotype.Service;

/**
 * Created by shiny on 2017/5/31.
 */
@Service
public class IdGenApi extends ServiceResources {

    public RemoteModelResult<IdGen> queryMaxId(int type){
        int id=idGenService.queryMaxIdByType(type);
        IdGen idGen=null;
        if(id!=-1){//type 存在
            idGen = new IdGen();
            idGen.setId(id);
            idGen.setType(type);
        }
        return new RemoteModelResult<>(idGen);
    }
    
}
