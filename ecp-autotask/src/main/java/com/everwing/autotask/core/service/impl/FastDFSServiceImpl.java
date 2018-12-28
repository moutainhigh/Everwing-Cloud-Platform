package com.everwing.autotask.core.service.impl;

import com.everwing.autotask.core.dao.UploadFileMapper;
import com.everwing.autotask.core.service.FastDFSService;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.utils.FastDFS.FastDFSUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @author DELL shiny
 * @create 2018/6/4
 */
@Service
public class FastDFSServiceImpl implements FastDFSService{

    @Autowired
    private UploadFileMapper uploadFileMapper;

    @Override
    public UploadFile uploadFile(File file){
        UploadFile uploadFile=null;
        try {
            InputStream inputStream=new FileInputStream(file);
            String fileName=file.getName();
            long fileLength=inputStream.available();
            String path= FastDFSUtils.uploadFile(inputStream,fileName,fileLength);
            if(StringUtils.isNotEmpty(path)) {
                uploadFile= new UploadFile();
                uploadFile.setUploadFileId(UUID.randomUUID().toString());
                uploadFile.setFileName(fileName);
                uploadFile.setMd5(DigestUtils.md5Hex(inputStream));
                uploadFile.setPath(path);
                uploadFile.setSize(fileLength);
                uploadFile.setSuffix(FilenameUtils.getExtension(fileName));
                uploadFile.setUploadTime(new Date());
                // 获取请求人的信息 暂时用-1测试
                //uploadFile.setAccountId("-1");
                uploadFileMapper.insert(uploadFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadFile;
    }
}
