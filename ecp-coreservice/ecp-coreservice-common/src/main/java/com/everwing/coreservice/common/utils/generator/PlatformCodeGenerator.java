package com.everwing.coreservice.common.utils.generator;/**
 * Created by wust on 2018/8/14.
 */

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 *
 * Function:平台专用编码生成器
 * Reason:可在分布式并发环境下使用
 * Date:2018/8/14
 * @author wusongti@lii.com.cn
 */
@Component
public class PlatformCodeGenerator {


    private PlatformCodeGenerator(){}



    private static long getNewValueByKey(String key,long value,long timeout,TimeUnit timeUnit,String desc){
        SpringRedisTools springRedisTools = SpringContextHolder.getBean("redisDataOperator");

        long newValue = 0;

        int tryCount = 5;

        do {
            newValue = springRedisTools.incrementForLong(key, value, timeout, timeUnit);
            if(newValue < 1 && tryCount > 0){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                tryCount --;
            }else{
                break;
            }
        }while (true);

        if(newValue < 1){
            throw new ECPBusinessException("生成["+desc+"]失败");
        }

        return newValue;
    }
}
