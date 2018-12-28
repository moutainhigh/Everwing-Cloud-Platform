/**
 *
 */
package com.everwing.cache.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service("redisDataOperator")
public class SpringRedisTools {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据key获取缓存值
     *
     * @param key
     * @return
     */
    public Object getByKey(String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Object obj = ops.get(key);
        return obj;
    }

    /**
     * 根据key值删除缓存值
     *
     * @param key
     */
    public void deleteByKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 根据key值更新缓存
     *
     * @param key
     * @param value
     */
    public void updateDataByKey(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 往缓存中增加值，不会覆盖原来的key
     * Set {@code value} for {@code key}, only if {@code key} does not exist.
     *
     * @param key
     * @param value
     */
    public void addData(String key, Object value) {
        ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
        valueops.setIfAbsent(key, value);
    }


    /**
     * 往缓存中增加值,带过期时间
     *
     * @param key
     * @param value
     * @param timeout
     */
    public void addData(String key, Object value, long timeout, TimeUnit unit) {
        ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
        valueops.set(key, value);
        redisTemplate.expire(key, timeout, unit);
    }

    public void updateExpire(final String key, long expireTime, TimeUnit timeUnit) {
        redisTemplate.expire(key, expireTime, timeUnit);
    }

    /**
     * 基于long类型的原子自增操作
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    public Long incrementForLong(String key, long value, long timeout, TimeUnit unit){
        ValueOperations<String, Object> valueops = redisTemplate.opsForValue();
        Long returnValue = valueops.increment(key,value);
        if(timeout > 0){
            redisTemplate.expire(key, timeout, unit);
        }
        return returnValue;
    }



    /**
     * 添加Map
     * @param key
     * @param value map对象
     */
    public void addMap(String key, Map value) {
        HashOperations<String, Object,Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.putAll(key,value);
    }

    /**
     * 往指定的map中添加一个字段
     * @param key
     * @param fieldName
     * @param value
     */
    public void addFieldToMap(String key, String fieldName,Object value) {
        HashOperations<String, Object,Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.put(key,fieldName,value);
    }

    /**
     * 根据key获取Map
     * @param key
     * @return
     */
    public Map getMap(String key) {
        HashOperations<String, Object,Object> opsForHash = redisTemplate.opsForHash();
        return opsForHash.entries(key);
    }

    /**
     * 根据fieldName获取value
     * @param key
     * @param fieldName
     * @return
     */
    public Object getMapValueByFieldName(String key,String fieldName) {
        HashOperations<String, Object,Object> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key,fieldName);
    }

    /**
     * 删除map
     * @param key
     */
    public void deleteMap(String key) {
        HashOperations<String, Object,Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.getOperations().delete(key);
    }

    /**
     * 从map里面删除指定的字段
     * @param key
     * @param fieldNames
     */
    public void deleteFieldFromMap(String key,String... fieldNames) {
        HashOperations<String, Object,Object> opsForHash = redisTemplate.opsForHash();
        opsForHash.delete(key,fieldNames);
    }
}
