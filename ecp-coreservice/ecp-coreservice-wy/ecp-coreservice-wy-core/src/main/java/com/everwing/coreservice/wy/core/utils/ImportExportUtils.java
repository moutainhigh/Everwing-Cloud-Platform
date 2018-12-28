package com.everwing.coreservice.wy.core.utils;/**
 * Created by wust on 2017/10/16.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.dao.mapper.common.ImportExportMapper;
import com.everwing.utils.CreateFileUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/10/16
 * @author wusongti@lii.com.cn
 */
public class ImportExportUtils {
    static Logger logger = LogManager.getLogger(ImportExportUtils.class);


    /**
     * 上传导出结果信息
     * @param importExportMapper
     * @param batchNo
     * @param msg
     */
    public static void uploadMessage(ImportExportMapper importExportMapper,FastDFSApi fastDFSApi,String batchNo, String msg) {
        FileOutputStream os = null;
        File tempFile = null;
        try {
            String tempFilePath = createTempFile("message",batchNo,".txt");
            tempFile = new File(tempFilePath);
            os = new FileOutputStream(tempFile, true);
            os.write((msg + "\n").getBytes());
        } catch (Exception e) {
            logger.error("上传导出结果失败："+e);
        }finally {
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {

                }
            }

            try {
                RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.uploadFile(tempFile);
                if(remoteModelResult.isSuccess()) {
                    UploadFile uploadFile = remoteModelResult.getModel();
                    if (uploadFile != null) {
                        TSysImportExportSearch tSysImportExportSearch = new TSysImportExportSearch();
                        tSysImportExportSearch.setBatchNo(batchNo);
                        List<TSysImportExportList> tSysImportExportLists =  importExportMapper.findByCondtion(tSysImportExportSearch);
                        if(CollectionUtils.isNotEmpty(tSysImportExportLists)) {
                            TSysImportExportList tSysImportExportList = tSysImportExportLists.get(0);
                            tSysImportExportList.setUploadMessageId(uploadFile.getUploadFileId());
                            tSysImportExportList.setEndTime(new Date());
                            importExportMapper.modify(tSysImportExportList);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("上传导出结果失败："+e);
            }
        }
    }

    /**
     * 创建临时文件
     * @param batchNo
     * @param fileType
     * @return
     */
    public static String createTempFile(String dir,String batchNo,String fileType) throws Exception {
        // 创建临时目录
        String dirName = getRootPathBySystem() + File.separator + "importExport" + File.separator + "temp" + File.separator + dir;
        // 创建临时文件
        String prefix = batchNo;
        String surfix = "." + fileType;
        return CreateFileUtil.createTempFile(prefix, surfix, dirName);
    }

    /**
     * 根据系统类别获取盘符
     * @return
     */
    public static String getRootPathBySystem(){
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            return "C:";
        }else {
            return "tmp";
        }
    }
}
