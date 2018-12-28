package com.everwing.server.wy.api.controller;

import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.platform.util.Base64DecodedMultipartFile;
import com.everwing.coreservice.platform.api.FastDFSApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

/**
 * 重复平台文件下载
 *
 * @author DELL shiny
 * @create 2017/7/18
 */
@RestController
@RequestMapping("file")
public class FileController {

    private static final Logger logger= LogManager.getLogger(FileController.class);

    @Autowired
    private FastDFSApi fastDFSApi;

    @PostMapping("/get")
    public LinphoneResult download(String fileId) throws IOException {
        RemoteModelResult<UploadFile> remoteModelResult=fastDFSApi.loadFilePathById(fileId);
        if(remoteModelResult.isSuccess()){
            UploadFile uploadFile = remoteModelResult.getModel();
            HashMap<String,String> hashMap=new HashMap<>(2);
            hashMap.put("imgUrl",uploadFile.getPath());
            hashMap.put("fileId",fileId);
            return new LinphoneResult(hashMap);
        }
        return new LinphoneResult(remoteModelResult);
    }

    @PostMapping("/uploadFile")
    public LinphoneResult uploadFile(String uploadFile,String code) throws Exception{
        logger.info("收到上传文件请求开始解密");
        byte[] imgContent= org.apache.commons.codec.binary.Base64.decodeBase64(uploadFile);
        logger.info("开始上传");
        RemoteModelResult<UploadFile> remoteModelResult=fastDFSApi.uploadFile(new Base64DecodedMultipartFile(imgContent));
        if(remoteModelResult.isSuccess()){
            UploadFile uploaded=remoteModelResult.getModel();
            HashMap<String,String> hashMap=new HashMap(1);
            hashMap.put("fileId",uploaded.getUploadFileId());
            hashMap.put("code",code);
            logger.info("上传成功");
            return new LinphoneResult(hashMap);
        }else {
            logger.info("文件上传失败!");
            return new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
        }
    }

}
