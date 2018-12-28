package com.everwing.utils.FastDFS;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.TrackerServer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * fdfs心跳检查
 *
 * @author DELL shiny
 * @create 2017/9/19
 */
public class HeartBeat {

    private static final Logger logger= LogManager.getLogger(HeartBeat.class);

    private ConnectionPool connectionPool=null;

    public static int HOUR_TO_SECOUNDS=1000*60*60*1;

    public static int waitTimes=200;

    public HeartBeat(ConnectionPool connectionPool) {
        this.connectionPool=connectionPool;
    }

    public void beat(){
        logger.info("心跳方法");
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                String logId= UUID.randomUUID().toString();
                logger.info("对连接池中的trackerServer进行检测:{}",logId);
                LinkedBlockingQueue<TrackerServer> idleConnectionPool=connectionPool.getIdleConnectionPool();
                TrackerServer trackerServer=null;
                for(int i=0;i<idleConnectionPool.size();i++){
                    try {
                        trackerServer=idleConnectionPool.poll(waitTimes, TimeUnit.SECONDS);
                        if(trackerServer!=null){
                            ProtoCommon.activeTest(trackerServer.getSocket());
                        }else {
                            break;
                        }
                    } catch (Exception e) {
                        logger.error("心跳任务方法当前连接不可用将重新获取连接!:{}",logId);
                        connectionPool.drop(trackerServer,logId);
                    }
                }
            }
        };
        Timer timer=new Timer();
        timer.schedule(timerTask,HOUR_TO_SECOUNDS,HOUR_TO_SECOUNDS);
    }
}
