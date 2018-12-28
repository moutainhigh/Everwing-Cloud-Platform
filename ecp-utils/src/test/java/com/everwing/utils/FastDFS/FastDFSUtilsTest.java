package com.everwing.utils.FastDFS;

import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import java.util.UUID;


/**
 * Created by DELL on 2017/12/4.
 */
public class FastDFSUtilsTest {

    @Test
    public void testConnectionPool(){
        final ConnectionPool connectionPool=new ConnectionPool(10,30,200);
        for (int i=0;i<50;i++) {
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    UUID uuid = UUID.randomUUID();
                    TrackerServer trackerServer = connectionPool.checkout(uuid.toString());
                    connectionPool.checkin(trackerServer, uuid.toString());
                }
            });
            thread.start();
        }
    }

}