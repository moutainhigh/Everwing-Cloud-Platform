package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.platform.api.util.ServiceResources;
import org.springframework.stereotype.Service;

/**
 * @description 获取自增ID
 * @author MonKong
 * @date 2017年8月29日
 */
@Service
public class IncreasedIdApi extends ServiceResources {
    
    public RemoteModelResult<Integer> generateIncreasedId(int type){
    	return new RemoteModelResult<Integer>(idGenService.getIncreasedId(type));
    }
}
