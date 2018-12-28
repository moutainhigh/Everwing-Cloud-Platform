package com.everwing.utils.FastDFS;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * fastdfs连接池
 *
 * @author DELL shiny
 * @create 2017/9/19
 */
public class ConnectionPool {

    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);

    private static final String OS_CONFIG_PATH="/app/fastdfs/fdfs_client.properties";

    /**
     * 空闲连接池
     */
    private LinkedBlockingQueue<TrackerServer> idleConnectionPool=null;

    /**
     * 连接池默认最小连接数
     */
    private long minPoolSize = 10;

    /**
     * 连接池默认最大连接数
     */
    private long maxPoolSize = 30;

    /**
     * 当前创建的连接数
     */
    private volatile long nowPoolSize = 0;

    /**
     * 默认等待时间
     */
    private long waitTimes = 200;

    /**
     * fsfs 客户端创建默认1次
     */
    private static final int COUNT = 1;

    public ConnectionPool(long minPoolSize,long maxPoolSize,long waitTimes){
        String logId = UUID.randomUUID().toString();
        logger.info("线程池默认构造方法");
        this.minPoolSize=minPoolSize;
        this.maxPoolSize=maxPoolSize;
        this.waitTimes=waitTimes;
        /**
         * 初始化连接池
         */
        initPool(logId);
        /**
         * 注册心跳
         */
        HeartBeat heartBeat=new HeartBeat(this);
        heartBeat.beat();
    }

    private void initPool(String logId){
        try {
            initClientGlobal();
            idleConnectionPool = new LinkedBlockingQueue<TrackerServer>();
            for(int i=0;i<minPoolSize;i++){
                createTrackerServer(logId,COUNT);
            }
        }catch (Exception e){
            logger.error("fdfs初始化异常:{}",logId);
        }
    }

    private void initClientGlobal() {
        try {
            ClientGlobal.initByProperties(OS_CONFIG_PATH);
        } catch (IOException e) {
            logger.info("未找到服务器配置文件,开始通过项目初始化client");
            initClientByProject();
        } catch (MyException e) {
            logger.info("未找到服务器配置文件,开始通过项目初始化client");
            initClientByProject();
        }

    }

    private void initClientByProject(){
        try {
            ClientGlobal.initByProperties("fdfs_client.properties");
        } catch (IOException e1) {
            logger.error("初始化fdfs失败，未找到配置文件");
        } catch (MyException e1) {
            logger.error("初始化fdfs失败，未找到配置文件");
        }
    }

    public void createTrackerServer(String logId,int retryCount){
        logger.info("创建TrackerServer:{}",logId);
        TrackerServer trackerServer = null;
        try {
            TrackerClient trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            while (trackerServer==null&& retryCount<5){
                logger.info("创建TrackerServer:{}第{}次重建",logId,retryCount);
                retryCount++;
                initClientGlobal();
                trackerServer = trackerClient.getConnection();
            }
            boolean isAlive = ProtoCommon.activeTest(trackerServer.getSocket());
            if(!isAlive){
                logger.error("初始化TrackerServer失败:{}",logId);
                throw new RuntimeException();
            }
            idleConnectionPool.add(trackerServer);
            /**
             * 不予许对nowPoolSize并发操作
             */
            synchronized (this){
                nowPoolSize++;
            }
        } catch (IOException e) {
            logger.error("创建trackerServer异常:{}",logId);
        }
    }


    public TrackerServer checkout(String logId){
        logger.info("获取空闲连接{},连接池大小{}。",logId,idleConnectionPool.size());
        TrackerServer trackerServer=idleConnectionPool.poll();
        if(trackerServer==null){
            if(nowPoolSize<minPoolSize){
                createTrackerServer(logId,COUNT);
                try {
                    trackerServer = idleConnectionPool.poll(waitTimes, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    logger.error("获取空闲连接,获取连接超时:{},异常:{}",logId,e);
                    throw new RuntimeException("获取空闲连接,获取连接超时");
                }
            }
            if(trackerServer==null){
                logger.error("获取空闲连接,获取连接超时:{}",logId);
                throw new RuntimeException("获取空闲连接,获取连接超时");
            }
            synchronized (this){
                nowPoolSize--;
            }
        }
        logger.info("获取空闲连接成功!:{}",logId);
        return trackerServer;
    }

    public void checkin(TrackerServer trackerServer,String logId){
        logger.info("释放当前连接:{},连接池大小{},nowPoolSize:{}",logId,idleConnectionPool.size(),nowPoolSize);
        if (trackerServer != null) {
            if (idleConnectionPool.size() < minPoolSize) {
                idleConnectionPool.add(trackerServer);
                synchronized (this){
                    nowPoolSize++;
                }
            } else {
                try {
                    trackerServer.close();
                } catch (IOException e) {
                }
                synchronized (this) {
                    if (nowPoolSize != 0) {
                        nowPoolSize--;
                    }
                }
            }
        }
    }

    public void drop(TrackerServer trackerServer,String logId){
        logger.info("删除不可用连接方法:{}",logId);
        if(trackerServer!=null){
            try {
                synchronized (this){
                    if(nowPoolSize!=0){
                        nowPoolSize--;
                    }
                }
                idleConnectionPool.remove(trackerServer);
                if(idleConnectionPool.size()<minPoolSize){
                    createTrackerServer(logId,COUNT);
                }
            } catch (Exception e) {
                logger.info("删除不可用连接关闭trackerServer异常:{}.{}",logId,e);
            }
        }
    }

    public LinkedBlockingQueue<TrackerServer> getIdleConnectionPool() {
        return idleConnectionPool;
    }

    public long getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(long minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public long getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(long maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public long getWaitTimes() {
        return waitTimes;
    }

    public void setWaitTimes(long waitTimes) {
        this.waitTimes = waitTimes;
    }
}
