package com.everwing.coreservice.common.lock;/**
 * Created by wust on 2018/8/3.
 */

import java.util.concurrent.TimeUnit;

/**
 * Function:分布式锁接口
 * Reason:
 * Date:2018/8/3
 *
 * @author wusongti@lii.com.cn
 */
public interface DistributedLock {

    /**
     * 尝试获取锁,不进行等待。得到返回true,
     *
     * @return
     * @throws Exception
     */
    boolean tryLock() throws Exception;

    /**
     * 阻塞等待获取锁
     *
     * @throws Exception
     */
    void lock() throws Exception;

    /**
     * 在规定时间内等待获取锁
     *
     * @param time
     * @param unit
     * @return
     * @throws Exception
     */
    boolean lock(long time, TimeUnit unit) throws Exception;

    /**
     * 释放锁
     *
     * @throws Exception
     */
    void unLock() throws Exception;

}
