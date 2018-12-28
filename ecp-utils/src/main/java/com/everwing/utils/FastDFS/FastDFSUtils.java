package com.everwing.utils.FastDFS;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;


/**
 * @description fastDFS工具类
 * @author  shiny
 */
public class FastDFSUtils {

    private static final Logger logger=LogManager.getLogger(FastDFSUtils.class);

    private static TrackerClient trackerClient;

    private static TrackerServer trackerServer;

    private static StorageServer storageServer;

    private static StorageClient1 storageClient1;

    private static final String OS_CONFIG_PATH="/app/fastdfs/fdfs_client.properties";

    static {
        try {
            ClientGlobal.initByProperties(OS_CONFIG_PATH);
            trackerClient=new TrackerClient();
            trackerServer=trackerClient.getConnection();
            storageServer=null;
            storageClient1=new StorageClient1(trackerServer,storageServer);
        } catch (IOException e) {
            logger.debug("未找到服务器上的配置文件。启用项目的配置文件");
            initByProject();
        } catch (MyException e) {
            logger.debug("未找到服务器上的配置文件。启用项目的配置文件");
            initByProject();
        }
    }

    private static void initByProject(){
        try {
            ClientGlobal.initByProperties("fdfs_client.properties");
            trackerClient=new TrackerClient();
            trackerServer=trackerClient.getConnection();
            storageServer=null;
            storageClient1=new StorageClient1(trackerServer,storageServer);
        } catch (IOException e1) {
            logger.error("项目配置文件错误。文件服务器使用失败");
        } catch (MyException e1) {
            logger.error("项目配置文件错误。文件服务器使用失败");
        }
    }

    /**
     * 上传文件并返回fileCode
     * @param inputStream  输入流
     * @param fileName     文件名
     * @param fileLength   文件长度
     * @return 文件服务器存储位置
     * @throws Exception fastdfs抛出的异常
     */
    public static String uploadFile(InputStream inputStream,String fileName,long fileLength)throws Exception{
        NameValuePair[] metaList = new NameValuePair[1];
        metaList[0] = new NameValuePair("fileName", fileName);
        return storageClient1.upload_file1(getFileBuffer(inputStream,fileLength), FilenameUtils.getExtension(fileName),metaList);
    }

    public static boolean deleteFile(String fileId){
        try {
            return storageClient1.delete_file1(fileId)>0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static byte[] downloadFile(String path){
        try {
            return storageClient1.download_file1(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String testUploadLoadFile()throws Exception{
        File file=new File("E:\\lingxin\\cmder1.3.2_full\\icons\\cmder.ico");
        StorageClient1 client1=new StorageClient1(trackerServer,storageServer);
        NameValuePair[] metaList = new NameValuePair[1];
        metaList[0] = new NameValuePair("fileName", file.getName());
        InetSocketAddress inetSocketAddress=ClientGlobal.getG_tracker_group().tracker_servers[ClientGlobal.getG_tracker_group().tracker_server_index];
        String ipPort=inetSocketAddress.getHostString()+":"+ClientGlobal.getG_tracker_http_port()+"/";
        return ipPort+client1.upload_file1(file.getAbsolutePath(), FilenameUtils.getExtension(file.getName()),metaList);
    }



    private static byte[] getFileBuffer(InputStream inputStream,long fileLength) throws IOException {
        byte[] buffer=new byte[256*1024];
        byte[] fileBuffer=new byte[(int)fileLength];
        int count=0;
        int length=0;
        while ((length=inputStream.read(buffer))!=-1){
            for (int i=0;i<length;i++){
                fileBuffer[count+i]=buffer[i];
            }
            count+=length;
        }
        return fileBuffer;
    }
    public static void main(String[] args) throws Exception{
        System.out.println(testUploadLoadFile());
    }
}
